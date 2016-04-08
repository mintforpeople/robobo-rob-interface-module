package com.mytechia.commons.framework.simplemessageprotocol.android;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
import static android.bluetooth.BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
import static android.bluetooth.BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION;
import static android.bluetooth.BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE;
import static android.bluetooth.BluetoothAdapter.EXTRA_SCAN_MODE;
import static android.bluetooth.BluetoothDevice.ACTION_FOUND;

/**
 * Created by julio on 1/04/16.
 */
public class DiscovererBluetoothDevices {


    private final BluetoothAdapter defaultAdapter;

    private final Activity activity;

    private BroadcastReceiver mReceiver;

    private final List<DiscoverOtherBluetoothDeviceCallback> callbacks= new ArrayList<>();



    public DiscovererBluetoothDevices(Activity activity){

        if(activity==null){
            throw new NullPointerException("The parameter activity is required");
        }

        this.activity= activity;

        defaultAdapter=BluetoothAdapter.getDefaultAdapter();

        if(defaultAdapter==null){
            throw new BluetoothNotSupported("Device does not support Bluetooth");
        }


        this.mReceiver= new BroadcastReceiverDiscoveryBluetooth();

        IntentFilter filterActionFound = new IntentFilter(ACTION_FOUND);
        activity.registerReceiver(this.mReceiver, filterActionFound);

        IntentFilter  filterDiscoveryFinished = new IntentFilter(ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filterDiscoveryFinished);


    }



    public void addDiscoverOtherBluetoothDeviceCallback(DiscoverOtherBluetoothDeviceCallback callback){

        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }

    }

    public void removeDiscoverOtherBluetoothDeviceCallback(DiscoverOtherBluetoothDeviceCallback callback){
        callbacks.remove(callback);
    }


    public void startDiscovery() {
        boolean startedDiscovery=defaultAdapter.startDiscovery();

        if (startedDiscovery){

            for (DiscoverOtherBluetoothDeviceCallback callback : callbacks) {
                callback.startDiscovering();
            }
        }
    }

    public void cancelDiscovering(){
        defaultAdapter.cancelDiscovery();
    }


    private Intent createDiscoverableIntent(long time){

        Intent discoverableIntent = new Intent(ACTION_REQUEST_DISCOVERABLE);

        discoverableIntent.putExtra(EXTRA_DISCOVERABLE_DURATION, time);

        return discoverableIntent;

    }

    public void makeDiscoverable(long time){

        this.activity.startActivity(createDiscoverableIntent(time));

    }



    private class BroadcastReceiverDiscoverableMode extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //Posibles valores
            //SCAN_MODE_CONNECTABLE_DISCOVERABLE
            // SCAN_MODE_CONNECTABLE -> No es descubrible pero aun es capaz de recibir conexiones
            //SCAN_MODE_NONE
            intent.getBundleExtra(EXTRA_SCAN_MODE);

            intent.getBundleExtra(EXTRA_PREVIOUS_SCAN_MODE);


        }
    }


    private class BroadcastReceiverDiscoveryBluetooth extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String intentAction = intent.getAction();

            if (ACTION_FOUND.equals(intentAction)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                for (DiscoverOtherBluetoothDeviceCallback callback : callbacks) {
                    callback.discovered(device);
                }

            } else if (ACTION_DISCOVERY_FINISHED.equals(intentAction)) {
                for (DiscoverOtherBluetoothDeviceCallback callback : callbacks) {
                    callback.endDiscovering();
                }
            }

        }
    }


    public void free(){
        cancelDiscovering();
        activity.unregisterReceiver(this.mReceiver);
    }



}
