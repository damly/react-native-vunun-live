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
    public void startPublish() {
        RNLrsPublisher.getInstance().startStreaming();
        RNLrsPublisher.getInstance().setWhiteningFilter();
    }

    @ReactMethod
    public void stopPublish() {
        RNLrsPublisher.getInstance().stopStreaming();
    }
}