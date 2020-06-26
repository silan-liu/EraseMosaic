//
//  EraseMosaicView.h
//  EraseMosaic
//
//  Created by silan on 2020/6/25.
//  Copyright © 2020 summer. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

// 马赛克擦除视图
@interface EraseMosaicView : UIView

// 马赛克图
@property (nonatomic, strong) UIImage *mosaicImage;

// 原图
@property (nonatomic, strong) UIImage *originImage;

// 马赛克图 url
@property (nonatomic, copy) NSString *mosaicImageUrl;

// 原图 url
@property (nonatomic, copy) NSString *originImageUrl;

@end

NS_ASSUME_NONNULL_END
