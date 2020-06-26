package com.example.erasemosaic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * 马赛克清除 view
 */
@SuppressLint("AppCompatCustomView")
public class EraseMosaicView extends View {

    // 图片宽高
    private int mViewWidth, mViewHeight;

    // 路径绘制画笔
    private Paint mPathPaint;

    // 底图绘制画笔
    private Paint mBitmapPaint;

    // 合并图层
    private Paint mPaint;

    // 马赛克图
    private Bitmap mMoasicBitmap;

    // 原图
    private Bitmap mOriginBitmap;

    private DrawPath mLastPath;
    private float tempX, tempY;

    // 触摸路径
    private ArrayList<DrawPath> mPaths = new ArrayList<>();

    public EraseMosaicView(Context context) {
        super(context);
        init();
    }

    public EraseMosaicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mViewWidth <= 0 || mViewHeight <= 0) {
            return;
        }

        // 最底层是马赛克图
        if (mMoasicBitmap != null) {
            canvas.drawBitmap(mMoasicBitmap, 0, 0, mBitmapPaint);
        }

        // 画原图 + 触摸图层路径
        drawPath(canvas);
    }

    /**
     * 画马赛克内容
     */
    private void drawPath(Canvas canvas) {
        if (mOriginBitmap != null) {

            // 保存图层
            int layerCount = canvas.saveLayer(0, 0, mViewWidth, mViewHeight, null, Canvas.ALL_SAVE_FLAG);

            if (mPaths.size() > 0) {

                for (DrawPath path : mPaths) {
                    canvas.drawPath(path.path, mPathPaint);
                }
            }

            // 进行图层的合并
            canvas.drawBitmap(mOriginBitmap, 0, 0, mPaint);
            canvas.restoreToCount(layerCount);
        }
    }

    // 设置马赛克图
    public void setMosaicBitmap(@NonNull Bitmap bitmap) {
        mMoasicBitmap = bitmap;
        updateBitmap();
    }

    /**
     * 设置原始的截图
     *
     * @param originBitmap drawable
     */
    public void setOriginBitmap(@NonNull Bitmap originBitmap) {
        mOriginBitmap = originBitmap;
        updateBitmap();
    }

    private void init() {
        // 画底层的图
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        // 画合并图层
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(false);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 画路径
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setAntiAlias(true);
        mPathPaint.setDither(true);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setTextAlign(Paint.Align.CENTER);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeWidth(32);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mViewWidth = w;
            mViewHeight = h;

            updateBitmap();
        }
    }

    // 缩放图片
    private void updateBitmap() {
        if (mViewHeight > 0 && mViewWidth > 0) {
            if (mOriginBitmap != null) {
                mOriginBitmap = Bitmap.createScaledBitmap(mOriginBitmap, mViewWidth, mViewHeight, true);
            }

            if (mMoasicBitmap != null) {
                mMoasicBitmap = Bitmap.createScaledBitmap(mMoasicBitmap, mViewWidth, mViewHeight, true);
            }

            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();

                mLastPath = new DrawPath();
                mLastPath.moveTo(downX, downY);
                mPaths.add(mLastPath);

                invalidate();

                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                if (Math.abs(moveX - tempX) > 5 || Math.abs(moveY - tempY) > 5) {
                    mLastPath.quadTo(tempX, tempY, moveX, moveY);
                    invalidate();
                }

                tempX = moveX;
                tempY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                mLastPath.up();
                break;
        }
        return true;
    }

    /**
     * 封装路径
     */
    public class DrawPath {
        Path path;
        float downX;
        float downY;
        boolean isQuad;

        DrawPath() {
            path = new Path();
        }

        void moveTo(float x, float y) {
            downX = x;
            downY = y;
            path.moveTo(x, y);
        }

        void quadTo(float x1, float y1, float x2, float y2) {
            isQuad = true;
            path.quadTo(x1, y1, x2, y2);
        }

        void up() {
            if (!isQuad) {
                //画点
                path.lineTo(downX, downY + 0.1f);
                invalidate();
            }
        }
    }
}
