package com.mytechia.commons.framework.simplemessageprotocol.android;

import android.bluetooth.BluetoothSocket;

/**
 * Created by julio on 4/04/16.
 */
public interface ConnectionAcceptedCallback {

    void bluetoothConnectionAccepted(BluetoothSocket bluetoothSocket);

}
