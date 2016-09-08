package com.vunun.librestreaming;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

/**
 * Created by damly on 16/9/6.
 */
public class RNLrsViewManager  extends SimpleViewManager<RNLrsView> {
    public static final String REACT_CLASS = "RNLibReStreamingView";
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected RNLrsView createViewInstance(ThemedReactContext reactContext) {
        return new RNLrsView(reactContext);
    }
}
