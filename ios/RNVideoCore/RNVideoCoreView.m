//
//  RNVideoCore.m
//  RNVideoCore
//
//  Created by damly on 16/9/9.
//  Copyright © 2016年 damly. All rights reserved.
//

#import "RNVideoCoreView.h"

#import <GPUImage/GPUImageFilter.h>
#import <AVFoundation/AVFoundation.h>
#import <GPUImage/GPUImageVideoCamera.h>
#import <GPUImage/GPUImageRawDataOutput.h>
#import "GDLRawDataOutput.h"
#import "GPUImageMovieWriter.h"
#import "GPUImageLanczosResamplingFilter.h"
#import "GPUImageBeautifyFilter.h"
#import "GDLFilterUtil.h"

#import "RCTEventDispatcher.h"


#define LIVE_VIDEO_DEF_FRAMERATE 25
#define LIVE_VIEDO_SIZE_HORIZONTAL_CIF  (CGSizeMake(640, 16*23))
#define LIVE_VIEDO_SIZE_HORIZONTAL_D1   (CGSizeMake(960, 16*34))
#define LIVE_VIEDO_SIZE_HORIZONTAL_720P (CGSizeMake(1280, 16*45))

#define LIVE_VIEDO_SIZE_CIF  (CGSizeMake(16*23, 640))
#define LIVE_VIEDO_SIZE_D1   (CGSizeMake(16*34, 960))
#define LIVE_VIEDO_SIZE_720P (CGSizeMake(16*45, 1280))

typedef NS_ENUM(NSUInteger, LIVE_BITRATE) {
    LIVE_BITRATE_1Mbps=1500000,
    LIVE_BITRATE_800Kbps=800000,
    LIVE_BITRATE_600Kbps=600000,
    LIVE_BITRATE_300Kbps=300000
};

typedef NS_ENUM(NSUInteger, LIVE_QUALITY) {
    LIVE_QUALITY_CIF=1,
    LIVE_QUALITY_D1=2,
    LIVE_QUALITY_720P=3
};

@implementation RNVideoCoreView

static GPUImageVideoCamera *_videoCamera = nil;
static GDLRawDataOutput *_rtmpOutput = nil;
static NSString *_outputStreamUrl = nil;
static NSString *_outputStreamKey = nil;
static int _outputQuality = LIVE_QUALITY_CIF;
static UIInterfaceOrientation _outputOrientation = UIInterfaceOrientationLandscapeRight;
static bool _outputUseSkinSmoothing = true;
static RNVideoCoreView *_cameraPreview = nil;
static GPUImageOutput<GPUImageInput> *_outputGPUImageFilter = nil;
static AVCaptureDevicePosition _avCaptureDevicePosition = AVCaptureDevicePositionFront;


- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher
{
    _videoCamera = nil;
    _rtmpOutput = nil;
    _outputStreamUrl = nil;
    _outputStreamKey = nil;
    _outputQuality = LIVE_QUALITY_CIF;
    _outputOrientation = UIInterfaceOrientationLandscapeRight;
    _outputUseSkinSmoothing = true;
    _outputGPUImageFilter = nil;
    _avCaptureDevicePosition = AVCaptureDevicePositionFront;
    _cameraPreview = self;
    _eventDispatcher = eventDispatcher;
    return [super initWithFrame:CGRectZero];
}

- (void)setStreamUrl:(NSString *)streamUrl
{
    NSLog(@"---------------- -----------setStreamUrl %@", streamUrl);
    _outputStreamUrl = [streamUrl copy];
}

- (void)setStreamKey:(NSString *)streamKey
{
    NSLog(@"---------------- -----------streamKey %@", streamKey);
    _outputStreamKey = [streamKey copy];
}

- (void)setOrientation:(NSString *)orientation
{
    NSLog(@"---------------- -----------setOrientation %@", orientation);
    _orientation = [orientation copy];
    if ([orientation caseInsensitiveCompare: @"Portrait"] == NSOrderedSame) {
        _outputOrientation = UIDeviceOrientationPortrait;
        NSLog(@"---------------- -----------setOrientation UIDeviceOrientationPortrait");
    }
    else {
        _outputOrientation = UIInterfaceOrientationLandscapeRight;
        NSLog(@"---------------- -----------setOrientation UIInterfaceOrientationLandscapeRight");
    }
}

- (void)setQuality:(NSString *)quality
{
    NSLog(@"---------------- -----------setQuality %@", quality);
    if ([quality caseInsensitiveCompare: @"CIF"] == NSOrderedSame) {
        _outputQuality = LIVE_QUALITY_CIF;
    }
    else if ([quality caseInsensitiveCompare: @"D1"] == NSOrderedSame) {
        _outputQuality = LIVE_QUALITY_D1;
    }
    else if ([quality caseInsensitiveCompare: @"720P"] == NSOrderedSame) {
        _outputQuality = LIVE_QUALITY_720P;
    }
    else {
        _outputQuality = LIVE_QUALITY_CIF;
    }
}

- (void)setCamera:(NSString *)camera
{
    NSLog(@"---------------- -----------setCamera %@", camera);
    if ([camera caseInsensitiveCompare: @"BACK"] == NSOrderedSame) {
        _avCaptureDevicePosition = AVCaptureDevicePositionBack;
    }
    else {
        _avCaptureDevicePosition = AVCaptureDevicePositionFront;
    }
}

-(void)layoutSubviews
{
    [super layoutSubviews];
}

- (void)removeFromSuperview
{
    NSLog(@"---------------- REMOVE FROM SUPERVIEW");

    self.eventDispatcher = nil;
    [RNVideoCoreView stopPublish];
    [super removeFromSuperview];
}

+ (void) startPublish
{
    NSLog(@"------------------startPublish");
 
    if(!_outputStreamUrl || !_outputStreamKey || _videoCamera) {
        return;
    }
    
    CGSize outputSize;
    int outputBitrate = LIVE_BITRATE_600Kbps;
    int outputFramerate = LIVE_VIDEO_DEF_FRAMERATE;
    
    if(_outputOrientation == UIDeviceOrientationPortrait) {
        switch (_outputQuality) {
            case LIVE_QUALITY_D1:
                outputSize = LIVE_VIEDO_SIZE_D1;
                outputBitrate = LIVE_BITRATE_800Kbps;
                break;
            case LIVE_QUALITY_720P:
                outputSize = LIVE_VIEDO_SIZE_720P;
                outputBitrate = LIVE_BITRATE_1Mbps;
                break;
            case LIVE_QUALITY_CIF:
            default:
                outputSize = LIVE_VIEDO_SIZE_CIF;
                break;
        }
    }
    else {
        switch (_outputQuality) {
            case LIVE_QUALITY_D1:
                outputSize = LIVE_VIEDO_SIZE_HORIZONTAL_D1;
                outputBitrate = LIVE_BITRATE_800Kbps;
                break;
            case LIVE_QUALITY_720P:
                outputSize = LIVE_VIEDO_SIZE_HORIZONTAL_720P;
                outputBitrate = LIVE_BITRATE_1Mbps;
                break;
            case LIVE_QUALITY_CIF:
            default:
                outputSize = LIVE_VIEDO_SIZE_HORIZONTAL_CIF;
                break;
        }
    }
    
    NSLog(@"---------------- -----------outputStreamUrl %@", _outputStreamUrl);
    NSLog(@"---------------- -----------outputStreamKey %@", _outputStreamKey);
    NSLog(@"---------------- -----------outputSize %@", NSStringFromCGSize(outputSize));
    NSLog(@"---------------- -----------outputFramerate %d", outputFramerate);
    NSLog(@"---------------- -----------outputBitrate %d", outputBitrate);
    NSLog(@"---------------- -----------outputImageOrientation %d", _outputOrientation);
    NSLog(@"---------------- -----------AvCaptureDevicePosition %d", _avCaptureDevicePosition);
    
    _videoCamera = [[GPUImageVideoCamera alloc] initWithSessionPreset:AVCaptureSessionPreset1280x720 cameraPosition:_avCaptureDevicePosition];
    _videoCamera.outputImageOrientation = _outputOrientation;
    _videoCamera.frameRate = outputFramerate;
    [_videoCamera addTarget:_cameraPreview];
    
    GPUImageFilter *filter = [[GPUImageLanczosResamplingFilter alloc] init];
    [filter forceProcessingAtSize:outputSize];
    _rtmpOutput = [[GDLRawDataOutput alloc] initWithVideoCamera:_videoCamera withImageSize:outputSize withBitrate:outputBitrate];
    [_videoCamera addTarget:filter];
    [filter addTarget:_rtmpOutput];
    
    // 是否开启美颜
    if (_outputUseSkinSmoothing) {
        GPUImageBeautifyFilter *beautifyFilter = [[GPUImageBeautifyFilter alloc] init];
        [GDLFilterUtil insertFilter:beautifyFilter before:_cameraPreview toChain:_videoCamera];
    }
    
    [_videoCamera startCameraCapture];
    [_rtmpOutput startUploadStreamWithURL:_outputStreamUrl andStreamKey:_outputStreamKey];
}

+ (void) stopPublish
{
    if(!_rtmpOutput || !_videoCamera) {
        return;
    }
    
    NSLog(@"------------------stopPublish");
    [_rtmpOutput stopUploadStream];
    [_videoCamera stopCameraCapture];
    
    [[GPUImageContext sharedFramebufferCache] purgeAllUnassignedFramebuffers];
    
    if(_outputGPUImageFilter)
        [_videoCamera removeTarget:_outputGPUImageFilter];
    
    _rtmpOutput = nil;
    _videoCamera = nil;
    _outputGPUImageFilter = nil;
}

@end
