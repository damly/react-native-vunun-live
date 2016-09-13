//
//  RNVideoCore.m
//  RNVideoCore
//
//  Created by damly on 16/9/9.
//  Copyright © 2016年 damly. All rights reserved.
//

#import "RNVideoCoreViewManager.h"
#import "RNVideoCoreView.h"

@implementation RNVideoCoreViewManager

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (UIView *) view
{
      return [[RNVideoCoreView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
}

RCT_EXPORT_VIEW_PROPERTY(streamUrl, NSString)
RCT_EXPORT_VIEW_PROPERTY(streamKey, NSString)
RCT_EXPORT_VIEW_PROPERTY(orientation, NSString)
RCT_EXPORT_VIEW_PROPERTY(quality, NSString)
RCT_EXPORT_VIEW_PROPERTY(camera, NSString)

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}


RCT_EXPORT_METHOD(configureSetPortrait) {
    
}

RCT_EXPORT_METHOD(configureSetLandsapce) {
    
}

RCT_EXPORT_METHOD(configureSetBitRate:(int)bitRate) {
    
}

RCT_EXPORT_METHOD(configureSetVideoFPS:(int)bitRate) {
    
}

RCT_EXPORT_METHOD(configureSetTargetVideoSize:(int)width height:(int)height) {
    
}

RCT_EXPORT_METHOD(startPublish) {
    [RNVideoCoreView startPublish];
}

RCT_EXPORT_METHOD(stopPublish) {
    [RNVideoCoreView stopPublish];
}

@end
