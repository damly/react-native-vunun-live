//
//  RNVideoCore.h
//  RNVideoCore
//
//  Created by damly on 16/9/9.
//  Copyright © 2016年 damly. All rights reserved.
//
#ifndef RNVideoCoreView_h
#define RNVideoCoreView_h


#import "RCTEventDispatcher.h"
#import <GPUImage/GPUImageView.h>
#import <Foundation/Foundation.h>

@interface RNVideoCoreView :  GPUImageView

@property (nonatomic) RCTEventDispatcher *eventDispatcher;
@property (nonatomic, copy) NSString *streamUrl;
@property (nonatomic, copy) NSString *streamKey;
@property (nonatomic, copy) NSString *orientation;
@property (nonatomic, copy) NSString *quality;
@property (nonatomic, copy) NSString *camera;

+ (void) startPublish;
+ (void) stopPublish;

- (instancetype) initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher NS_DESIGNATED_INITIALIZER;


@end

#endif