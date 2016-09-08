package com.vunun.yasea;


import net.ossrs.yasea.SrsPublisher;

/**
 * Created by damly on 16/9/6.
 */
public class RNYaseaPublisher {
    private static final SrsPublisher mPublisher = new SrsPublisher();

    private RNYaseaPublisher() {
    }

    public static SrsPublisher getPublisher() {
        return mPublisher;
    }
}
