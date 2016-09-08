package com.vunun.librestreaming;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by damly on 16/9/6.
 */
public class RNLrsView extends TextureView {

    public RNLrsView(Context context) {
        super(context);

        this.setKeepScreenOn(true);
        this.setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                RNLrsPublisher.getInstance().startPreview(surface, width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                RNLrsPublisher.getInstance().updatePreview(width, height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

   }

    public RNLrsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RNLrsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
