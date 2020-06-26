//
//  ViewController.m
//  EraseMosaic
//
//  Created by silan on 2020/6/25.
//  Copyright © 2020 summer. All rights reserved.
//

#import "ViewController.h"
#import "EraseMosaicView.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    // 反向清除马赛克
    EraseMosaicView *eraseView = [[EraseMosaicView alloc] initWithFrame:CGRectMake(0, 100, self.view.frame.size.width, 400)];

//    eraseView.originImage = [UIImage imageNamed:@"girl"];
//    eraseView.mosaicImage = [UIImage imageNamed:@"mosaic-girl"];
        
    // or，设置 url
    eraseView.originImageUrl = @"http://192.168.31.20:8080/girl.png";
    eraseView.mosaicImageUrl = @"http://192.168.31.20:8080/mosaic-girl.png";
    
    [self.view addSubview:eraseView];
}


@end
