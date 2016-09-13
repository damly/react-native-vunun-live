package com.vunun.yasea;

import android.util.Log;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.vunun.librestreaming.RNLrsPublisher;
import com.vunun.librestreaming.RNLrsView;

/**
 * Created by damly on 16/9/6.
 */
public class RNYaseaViewManager  extends SimpleViewManager<RNYaseaView> {
    public static final String REACT_CLASS = "RNYaseaView";
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected RNYaseaView createViewInstance(ThemedReactContext reactContext) {
        return new RNYaseaView(reactContext);
    }

    @ReactProp(name="streamUrl")
    public void setStreamUrl(RNLrsView view, String streamUrl){
        Log.e(">>>>>>>>>>>>>>>>>", streamUrl);
        RNLrsPublisher.getInstance().setStreamUrl(streamUrl);
    }

    @ReactProp(name="streamKey")
    public void setStreamKey(RNLrsView view, String streamKey){
        Log.e(">>>>>>>>>>>>>>>>>", streamKey);
        RNLrsPublisher.getInstance().setStreamKey(streamKey);
    }

    @ReactProp(name="orientation")
    public void setOrientation(RNLrsView view, String orientation){
        Log.e(">>>>>>>>>>>>>>>>>", orientation);
        RNLrsPublisher.getInstance().setOrientation(orientation);
    }

    @ReactProp(name="camera")
    public void setCamera(RNLrsView view, String camera){
        Log.e(">>>>>>>>>>>>>>>>>", camera);
        RNLrsPublisher.getInstance().setCamera(camera);
    }

    @ReactProp(name="quality")
    public void setQuality(RNLrsView view, String quality){
        Log.e(">>>>>>>>>>>>>>>>>", quality);
        RNLrsPublisher.getInstance().setQuality(quality);
    }
}
