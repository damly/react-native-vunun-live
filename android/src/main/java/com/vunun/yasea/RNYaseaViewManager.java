package com.vunun.yasea;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

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
}
