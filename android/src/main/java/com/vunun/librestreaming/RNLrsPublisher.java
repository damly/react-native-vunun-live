package com.vunun.librestreaming;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

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
    private String streamUrl = "";
    private String streamKey = "";

    private String camera = "front";
    private String quality = "d1";
    private String orientation = "landscape";

    public static RNLrsPublisher getInstance() {
        return ourInstance;
    }

    private RNLrsPublisher() {
        resConfig.setFilterMode(RESConfig.FilterMode.HARD);
        resConfig.setRenderingMode(RESConfig.RenderingMode.OpenGLES);
    }

    public void setDefault() {
        streamUrl = "";
        streamKey = "";
        camera = "front";
        quality = "d1";
        orientation = "landscape";
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public void setStreamKey(String streamKey) {
        this.streamKey = streamKey;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setCameraPositionBack() {
        resConfig.setDefaultCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public void setCameraPositionFront() {
        resConfig.setDefaultCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    public void setCameraDirection() {
        int frontDirection, backDirection;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, cameraInfo);
        frontDirection = cameraInfo.orientation;
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, cameraInfo);
        backDirection = cameraInfo.orientation;
        if (orientation.equalsIgnoreCase("Portrait")) {
            resConfig.setFrontCameraDirectionMode((frontDirection == 90 ? RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_270 : RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_90) | RESConfig.DirectionMode.FLAG_DIRECTION_FLIP_HORIZONTAL);
            resConfig.setBackCameraDirectionMode((backDirection == 90 ? RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_90 : RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_270));
        } else {
            resConfig.setBackCameraDirectionMode((backDirection == 90 ? RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_0 : RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_180));
            resConfig.setFrontCameraDirectionMode((frontDirection == 90 ? RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_180 : RESConfig.DirectionMode.FLAG_DIRECTION_ROATATION_0) | RESConfig.DirectionMode.FLAG_DIRECTION_FLIP_HORIZONTAL);
        }
    }


    public void setTargetVideoSize(int width, int height) {
        resConfig.setTargetVideoSize(new Size(width, height));
    }

    public void setBitRate(int bitRate) {
        resConfig.setBitRate(bitRate);
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
        setDefault();
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

    public void startStreaming() {

        if (streamUrl.isEmpty() || streamKey.isEmpty())
            return;
        resClient = new RESClient();

        if (camera.equalsIgnoreCase("back")) {
            setCameraPositionBack();
        } else {
            setCameraPositionFront();
        }

        setCameraDirection();

        if (quality.equalsIgnoreCase("D1")) {
            setBitRate(800 * 1000);
            setTargetVideoSize(960, 16 * 34);
        } else if (quality.equalsIgnoreCase("720P")) {
            setBitRate(1500 * 1000);
            setTargetVideoSize(1280, 16 * 45);
        } else {
            setBitRate(600 * 1000);
            setTargetVideoSize(680, 16 * 23);
        }

        setVideoFPS(25);

        resConfig.setRtmpAddr(streamUrl + '/' + streamKey);
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
