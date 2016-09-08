package com.vunun.librestreaming;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import me.lake.librestreaming.client.RESClient;
import me.lake.librestreaming.core.listener.RESConnectionListener;
import me.lake.librestreaming.filter.softaudiofilter.BaseSoftAudioFilter;
import me.lake.librestreaming.model.RESConfig;
import me.lake.librestreaming.model.Size;
import me.lake.librestreaming.sample.audiofilter.SetVolumeAudioFilter;
import me.lake.librestreaming.sample.hardfilter.SkinBlurHardVideoFilter;
import me.lake.librestreaming.sample.hardfilter.WhiteningHardVideoFilter;

/**
 * Created by damly on 16/9/7.
 */
public class RNLrsPublisher {

    private static final RNLrsPublisher ourInstance = new RNLrsPublisher();

    private RESClient resClient = null;
    private RESConfig resConfig = RESConfig.obtain();

    public static RNLrsPublisher getInstance() {
        return ourInstance;
    }


    private RNLrsPublisher() {
        resConfig.setFilterMode(RESConfig.FilterMode.HARD);
        resConfig.setRenderingMode(RESConfig.RenderingMode.OpenGLES);
        resConfig.setDefaultCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        resConfig.setTargetVideoSize(new Size(640, 384));
        resConfig.setVideoFPS(24);
        resConfig.setBitRate(500 * 1024);
    }

    public void setCameraPortrait() {
        resConfig.setBackCameraDirectionMode(RESConfig.DirectionMode.FLAG_DIRECTION_FLIP_VERTICAL);
        resConfig.setFrontCameraDirectionMode(RESConfig.DirectionMode.FLAG_DIRECTION_FLIP_VERTICAL);
    }

    public void setCameraLandsapce() {
        resConfig.setBackCameraDirectionMode(RESConfig.DirectionMode.FLAG_DIRECTION_FLIP_HORIZONTAL);
        resConfig.setFrontCameraDirectionMode(RESConfig.DirectionMode.FLAG_DIRECTION_FLIP_HORIZONTAL);
    }

    public void setTargetVideoSize(int width, int height) {
        resConfig.setTargetVideoSize(new Size(width, height));
    }

    public void setBitRate(int bitRate) {
        resConfig.setBitRate(bitRate * 1024);
    }

    public void setVideoFPS(int fps) {
        resConfig.setVideoFPS(fps);
    }

    public void setVolume(int step) {

        if (resClient == null) {
            return;
        }

        BaseSoftAudioFilter audioFilter = resClient.acquireSoftAudioFilter();
        if (audioFilter != null) {
            if (audioFilter instanceof SetVolumeAudioFilter) {
                SetVolumeAudioFilter blackWhiteFilter = (SetVolumeAudioFilter) audioFilter;
                blackWhiteFilter.setVolumeScale((float) (step / 10.0));
            }
        }
        resClient.releaseSoftAudioFilter();
    }


    public void stopStreaming() {
        if (resClient == null) {
            return;
        }
        resClient.stopPreview();
        resClient.stopStreaming();
        resClient.destroy();
        resClient = null;
    }

    private boolean prepare() {

        if (resClient == null || !resClient.prepare(resConfig)) {
            return false;
        }

        resClient.setSoftAudioFilter(new SetVolumeAudioFilter());
        resClient.setConnectionListener(new RESConnectionListener() {
            @Override
            public void onOpenConnectionResult(int i) {
            }

            @Override
            public void onWriteError(int errno) {
                if (errno == 9) {
                    resClient.stopStreaming();
                    resClient.startStreaming();
                }
            }

            @Override
            public void onCloseConnectionResult(int i) {
            }
        });

        return true;
    }

    public void startStreaming(String rtmpAddr) {

        resClient = new RESClient();

        resConfig.setRtmpAddr(rtmpAddr);
        if (!prepare()) {
            resClient = null;
            return;
        }

        resClient.startStreaming();
    }

    public void startPreview(SurfaceTexture surface, int width, int height) {
        if (resClient == null) {
            return;
        }
        resClient.startPreview(surface, width, height);
    }

    public void updatePreview(int width, int height) {
        if (resClient == null) {
            return;
        }
        resClient.updatePreview(width, height);
    }

    public void swapCamera() {
        if (resClient == null) {
            return;
        }
        resClient.swapCamera();
    }

    public void setSkinBlurFilter(int stepScale) {
        if (resClient == null) {
            return;
        }
        resClient.setHardVideoFilter(new SkinBlurHardVideoFilter(stepScale));
    }

    public void setWhiteningFilter() {
        if (resClient == null) {
            return;
        }
        resClient.setHardVideoFilter(new WhiteningHardVideoFilter());
    }

    public void setZoomByPercent(int percent) {
        if (resClient == null) {
            return;
        }
        resClient.setZoomByPercent(percent / 100.0f);
    }
}
