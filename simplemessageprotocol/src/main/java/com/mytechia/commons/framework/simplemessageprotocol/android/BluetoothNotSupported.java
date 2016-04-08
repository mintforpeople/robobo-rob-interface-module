package com.mytechia.commons.framework.simplemessageprotocol.android;

/**
 * Created by julio on 1/04/16.
 */
public class BluetoothNotSupported extends RuntimeException{

    public BluetoothNotSupported(Throwable throwable) {
        super(throwable);
    }

    public BluetoothNotSupported(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BluetoothNotSupported(String detailMessage) {
        super(detailMessage);
    }
}
