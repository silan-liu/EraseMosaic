//
//  EraseMosaicView.m
//  EraseMosaic
//
//  Created by silan on 2020/6/25.
//  Copyright © 2020 summer. All rights reserved.
//

#import "EraseMosaicView.h"
#import "UIImageView+WebCache.h"

@interface EraseMosaicView()

// 马赛克图
@property (nonatomic, strong) UIImageView *mosaicImageView;

// 原图
@property (nonatomic, strong) UIImageView *originImageView;

// maskLayer
@property (nonatomic, strong) CAShapeLayer *shapeLayer;

// 滑动路径
@property (nonatomic, assign) CGMutablePathRef path;

@end

@implementation EraseMosaicView

- (instancetype)initWithFrame:(CGRect)frame{
    if (self = [super initWithFrame:frame]) {
        // 马赛克图在下
        self.mosaicImageView = [[UIImageView alloc] initWithFrame:self.bounds];
        [self addSubview:self.mosaicImageView];
        
        // 原图在上
        self.originImageView = [[UIImageView alloc] initWithFrame:self.bounds];
        [self addSubview:self.originImageView];
        
        // maskLayer
        self.shapeLayer = [CAShapeLayer layer];
        self.shapeLayer.frame = self.bounds;
        self.shapeLayer.lineCap = kCALineCapRound;
        self.shapeLayer.lineJoin = kCALineJoinRound;
        self.shapeLayer.lineWidth = 20;
        self.shapeLayer.strokeColor = [UIColor blueColor].CGColor;
        
        // 此处必须设为 nil，否则后边添加addLine的时候会自动填充
        self.shapeLayer.fillColor = nil;

        // 设置 mask
        self.originImageView.layer.mask = self.shapeLayer;
        self.path = CGPathCreateMutable();
    }
    
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.mosaicImageView.frame = self.bounds;
    self.originImageView.frame = self.bounds;
    self.shapeLayer.frame = self.bounds;
}

- (void)setMosaicImageUrl:(NSString *)mosaicImageUrl {
    _mosaicImageUrl = mosaicImageUrl;
    
    if (mosaicImageUrl && mosaicImageUrl.length > 0) {
        [self.mosaicImageView sd_setImageWithURL:[NSURL URLWithString:mosaicImageUrl]];
    }
}

- (void)setOriginImageUrl:(NSString *)originImageUrl {
    _originImageUrl = originImageUrl;
    
    if (originImageUrl && originImageUrl.length > 0) {
        [self.originImageView sd_setImageWithURL:[NSURL URLWithString:originImageUrl]];
    }
}

- (void)setMosaicImage:(UIImage *)mosaicImage{
    _mosaicImage = mosaicImage;
    self.mosaicImageView.image = mosaicImage;
}

- (void)setOriginImage:(UIImage *)originImage {
    _originImage = originImage;
    
    self.originImageView.image = originImage;
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    [super touchesBegan:touches withEvent:event];
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    CGPathMoveToPoint(self.path, nil, point.x, point.y);
    self.shapeLayer.path = self.path;
}

- (void)touchesMoved:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    [super touchesMoved:touches withEvent:event];
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    CGPathAddLineToPoint(self.path, nil, point.x, point.y);
    self.shapeLayer.path = self.path;
}

- (void)dealloc{
    if (self.path) {
        CGPathRelease(self.path);
    }
}
@end
