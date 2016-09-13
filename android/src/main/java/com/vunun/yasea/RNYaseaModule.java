package com.vunun.yasea;


import com.facebook.react.bridge.*;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import javax.annotation.Nullable;

import java.io.*;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import net.ossrs.yasea.SrsEncoder;
import net.ossrs.yasea.SrsMp4Muxer;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.rtmp.RtmpPublisher;


public class RNYaseaModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext mReactContext;

    public RNYaseaModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;

        RNYaseaPublisher.getPublisher().setPublishEventHandler(new RtmpPublisher.EventHandler() {
            @Override
            public void onRtmpConnecting(String msg) {
                sendEvent(mReactContext, "publish", "Connecting");
            }

            @Override
            public void onRtmpConnected(String msg) {
                sendEvent(mReactContext, "publish", "Connected");
            }

            @Override
            public void onRtmpVideoStreaming(String msg) {
                sendEvent(mReactContext, "publish", "VideoStreaming");
            }

            @Override
            public void onRtmpAudioStreaming(String msg) {
                sendEvent(mReactContext, "publish", "AudioStreaming");
            }

            @Override
            public void onRtmpStopped(String msg) {
                sendEvent(mReactContext, "publish", "Stopped");
            }

            @Override
            public void onRtmpDisconnected(String msg) {
                sendEvent(mReactContext, "publish", "Disconnected");
            }

            @Override
            public void onRtmpOutputFps(final double fps) {
                sendEvent(mReactContext, "publish", String.format("Output Fps: %f", fps));
            }
        });

        RNYaseaPublisher.getPublisher().setRecordEventHandler(new SrsMp4Muxer.EventHandler() {
            @Override
            public void onRecordPause(String msg) {
                sendEvent(mReactContext, "record", "Pause");
            }

            @Override
            public void onRecordResume(String msg) {
                sendEvent(mReactContext, "record", "Resume");
            }

            @Override
            public void onRecordStarted(String msg) {
                sendEvent(mReactContext, "record", "Started");
            }

            @Override
            public void onRecordFinished(String msg) {
                sendEvent(mReactContext, "record", "Finished");
            }
        });

        RNYaseaPublisher.getPublisher().setNetworkEventHandler(new SrsEncoder.EventHandler() {
            @Override
            public void onNetworkResume(final String msg) {
                sendEvent(mReactContext, "network", "Resume");
            }

            @Override
            public void onNetworkWeak(final String msg) {
                sendEvent(mReactContext, "network", "Weak");
            }
        });
    }

    @Override
    public String getName() {
        return "RNYaseaModule";
    }

    @ReactMethod
    public void startPublish(String url) {

        RNYaseaPublisher.getPublisher().setScreenOrientation(Configuration.ORIENTATION_LANDSCAPE);
        RNYaseaPublisher.getPublisher().setPreviewRotation(0);
        RNYaseaPublisher.getPublisher().startPublish(url);

        Activity activity = getCurrentActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Activity activity = getCurrentActivity();
                    if (activity != null) {
                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            });
        }

    }

    @ReactMethod
    public void stopPublish() {

        RNYaseaPublisher.getPublisher().stopPublish();

        Activity activity = getCurrentActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Activity activity = getCurrentActivity();
                    if (activity != null) {
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            });
        }
    }

    @ReactMethod
    public void startEncode() {
        RNYaseaPublisher.getPublisher().startEncode();
    }

    @ReactMethod
    public void stopEncode() {
        RNYaseaPublisher.getPublisher().stopEncode();
    }

    @ReactMethod
    public void startRecord(String recPath) {
        RNYaseaPublisher.getPublisher().startRecord(recPath);
    }

    @ReactMethod
    public void stopRecord() {
        RNYaseaPublisher.getPublisher().stopRecord();
    }

    @ReactMethod
    public void pauseRecord() {
        RNYaseaPublisher.getPublisher().pauseRecord();
    }

    @ReactMethod
    public void resumeRecord() {
        RNYaseaPublisher.getPublisher().resumeRecord();
    }

    @ReactMethod
    public void swithToSoftEncoder() {
        RNYaseaPublisher.getPublisher().swithToSoftEncoder();
    }

    @ReactMethod
    public void swithToHardEncoder() {
        RNYaseaPublisher.getPublisher().swithToHardEncoder();
    }

    @ReactMethod
    public boolean isSoftEncoder() {
        return RNYaseaPublisher.getPublisher().isSoftEncoder();
    }

    @ReactMethod
    public double getmSamplingFps() {
        return RNYaseaPublisher.getPublisher().getmSamplingFps();
    }

    @ReactMethod
    public int getCamraId() {
        return RNYaseaPublisher.getPublisher().getCamraId();
    }

    @ReactMethod
    public int getNumberOfCameras() {
        return RNYaseaPublisher.getPublisher().getNumberOfCameras();
    }

    @ReactMethod
    public void setPreviewResolution(int width, int height) {
        RNYaseaPublisher.getPublisher().setPreviewResolution(width, height);
    }

    @ReactMethod
    public void setOutputResolution(int width, int height) {
        RNYaseaPublisher.getPublisher().setOutputResolution(width, height);
    }

    @ReactMethod
    public void setScreenOrientation(int orientation) {
        RNYaseaPublisher.getPublisher().setScreenOrientation(orientation);
    }

    @ReactMethod
    public void setPreviewRotation(int rotation) {
        RNYaseaPublisher.getPublisher().setPreviewRotation(rotation);
    }

    @ReactMethod
    public void setVideoHDMode() {
        RNYaseaPublisher.getPublisher().setVideoHDMode();
    }

    public void setVideoSmoothMode() {
        RNYaseaPublisher.getPublisher().setVideoSmoothMode();
    }

    public void setSendAudioOnly(boolean flag) {
        RNYaseaPublisher.getPublisher().setSendAudioOnly(flag);
    }

    public void switchCameraFace(int id) {
        RNYaseaPublisher.getPublisher().switchCameraFace(id);
    }


    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable String message) {

        WritableMap params = Arguments.createMap();
        params.putString("message", message);
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}