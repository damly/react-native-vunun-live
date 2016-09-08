package com.vunun.librestreaming;

import com.facebook.react.bridge.*;

public class RNLrsModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext mReactContext;
    public RNLrsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNLibReStreamingModule";
    }

    @ReactMethod
    public void configureSetPortrait() {
        RNLrsPublisher.getInstance().setCameraPortrait();
    }

    @ReactMethod
    public void configureSetLandsapce() {
        RNLrsPublisher.getInstance().setCameraLandsapce();
    }

    @ReactMethod
    public void configureSetBitRate(int bitRate) {
        RNLrsPublisher.getInstance().setBitRate(bitRate);
    }

    @ReactMethod
    public void configureSetVideoFPS(int fps) {
        RNLrsPublisher.getInstance().setVideoFPS(fps);
    }

    @ReactMethod
    public void configureSetTargetVideoSize(int w, int h) {
        RNLrsPublisher.getInstance().setTargetVideoSize(w, h);
    }

    @ReactMethod
    public void configureSetZoomByPercent(int percent) {
        RNLrsPublisher.getInstance().setZoomByPercent(percent);
    }

    @ReactMethod
    public void swapCamera() {
        RNLrsPublisher.getInstance().swapCamera();
    }

    @ReactMethod
    public void setSkinBlur(int stepScale) {
        RNLrsPublisher.getInstance().setSkinBlurFilter(stepScale);
    }

    @ReactMethod
    public void setVolume(int step) {
        RNLrsPublisher.getInstance().setVolume(step);
    }

    @ReactMethod
    public void setWhitening() {
        RNLrsPublisher.getInstance().setWhiteningFilter();
    }

    @ReactMethod
    public void startPublish(String url) {
        RNLrsPublisher.getInstance().startStreaming(url);
        RNLrsPublisher.getInstance().setWhiteningFilter();
    }

    @ReactMethod
    public void stopPublish() {
        RNLrsPublisher.getInstance().stopStreaming();
    }
}