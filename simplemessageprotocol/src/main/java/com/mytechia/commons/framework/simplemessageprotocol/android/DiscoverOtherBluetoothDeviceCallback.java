package com.mytechia.commons.framework.simplemessageprotocol.android;

import android.bluetooth.BluetoothDevice;

/**
 * Created by julio on 1/04/16.
 */
public interface DiscoverOtherBluetoothDeviceCallback {

    void startDiscovering();

    void discovered(BluetoothDevice bluetoothDevice);

    void endDiscovering();
}
