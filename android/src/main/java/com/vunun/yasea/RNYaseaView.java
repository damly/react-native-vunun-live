package com.vunun.yasea;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by damly on 16/9/6.
 */
public class RNYaseaView extends SurfaceView {
    public RNYaseaView(Context context) {
        super(context);

        RNYaseaPublisher.getPublisher().setSurfaceView(this);
    }
}
