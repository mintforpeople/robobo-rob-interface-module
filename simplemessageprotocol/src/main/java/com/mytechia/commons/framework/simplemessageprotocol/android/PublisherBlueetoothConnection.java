package com.mytechia.commons.framework.simplemessageprotocol.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by julio on 1/04/16.
 */
public class PublisherBlueetoothConnection extends Thread{

    public static final  String TAG_PUBLISHER_BLUETOOTH_CONNECTION =PublisherBlueetoothConnection.class.getName();

    private final String applicationName;

    private final UUID uuidConnection;

    private BluetoothServerSocket bluetoothServerSocket;

    private List<ConnectionAcceptedCallback> callbacks= new ArrayList();



    public PublisherBlueetoothConnection(String applicationName, UUID uuidConnection) throws IOException {

        this.applicationName= applicationName;

        this.uuidConnection= uuidConnection;

        BluetoothAdapter defaultAdapter=BluetoothAdapter.getDefaultAdapter();

        if(defaultAdapter==null){
            throw new BluetoothNotSupported("Device does not support Bluetooth");
        }

        bluetoothServerSocket=defaultAdapter.listenUsingRfcommWithServiceRecord(applicationName, uuidConnection);

    }


    public void addReceivedBluetoothConnectionRequestCallback(ConnectionAcceptedCallback callback){

        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }

    }

    public void removeReceivedBluetoothConnectionRequestCallback(ConnectionAcceptedCallback callback){
        callbacks.remove(callback);
    }




    public BluetoothSocket accept() throws IOException {
        BluetoothSocket bluetoothSocket=bluetoothServerSocket.accept();
        bluetoothServerSocket.close();
        return bluetoothSocket;
    }


    public void close() throws IOException {
        bluetoothServerSocket.close();
    }


    public void run(){

        while (true){

            try {

                BluetoothSocket  bluetoothSocket= this.accept();

                for (ConnectionAcceptedCallback callback : callbacks) {
                    callback.bluetoothConnectionAccepted(bluetoothSocket);
                }

                break;

            } catch (IOException e) {
                Log.w(TAG_PUBLISHER_BLUETOOTH_CONNECTION, e);
                return;
            }

        }
    }


}
