package com.vunun.yasea;


import net.ossrs.yasea.SrsPublisher;

/**
 * Created by damly on 16/9/6.
 */
public class RNYaseaPublisher {
    private static final SrsPublisher mPublisher = new SrsPublisher();
    private static final RNYaseaPublisher ourInstance = new RNYaseaPublisher();

    private String streamUrl = "";
    private String streamKey = "";

    private String camera = "front";
    private String quality = "d1";
    private String orientation = "landscape";

    public static RNYaseaPublisher getInstance() {
        return ourInstance;
    }

    private RNYaseaPublisher() {
        setDefault();
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

    public static SrsPublisher getPublisher() {
        return mPublisher;
    }
}
