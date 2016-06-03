/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Gervasio Varela <gervasio.varela@mytechia.com>
 *   Copyright 2016 Julio Gómez <julio.gomez@mytechia.com>
 *
 *   This file is part of Robobo ROB Interface Module.
 *
 *   Robobo ROB Interface Module is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo ROB Interface Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo ROB Interface Module.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package com.mytechia.robobo.rob;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.commons.framework.simplemessageprotocol.bluetooth.android.AndroidBluetoothSPPChannel;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.rob.comm.RoboCommandFactory;
import com.mytechia.robobo.rob.comm.SmpRobComm;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


/** Default implementation of the IRobInterfaceModule interface using bluetooth for communication
 * with a Robobo-ROB.
 *
 * @author Gervasio Varela
 */
public class BluetoothRobInterfaceModule implements IRobInterfaceModule {


    private static final String MODULE_INFO = "Rob Interface Module";
    private static final String MODULE_VERSION = "0.1.0";

    private static final String ROB_NAME = "HC-06";

    private static final UUID UUID_BLUETOOTH_CONNECTION = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String ROBOBO_BT_NAME_OPTION = "robobo.bluetooth.name";

    private BluetoothDevice actualBluetoothDevice;

    private AndroidBluetoothSPPChannel androidBluetoothSPPChannel;
    private BluetoothSocket mmSocket;
    private SmpRobComm smpRoboCom;

    private RoboCommandFactory roboCommandFactory = new RoboCommandFactory();
    private DefaultRob defaultRob;


    private Bundle options;


    /** Returns an instance of the IRob interface for send/receive commands to a Robobo-ROB
     *
     * @return the instanfe of the IRob interface for the connected ROB
     */
    public IRob getRobInterface() {
        return this.defaultRob;
    }


    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {

        this.options = manager.getOptions();

        if(this.actualBluetoothDevice!=null){

            if(androidBluetoothSPPChannel!=null) {

                try {
                    androidBluetoothSPPChannel.close();
                } catch (IOException e) {
                    throw new InternalErrorException(e);
                }

            }
        }

        //look for a Robobo bluetooth device paired with the phone
        Log.d("ROB-INTERFACE", "Looking for Robobo-ROB devices via bluetooth.");
        this.actualBluetoothDevice = lookForRoboboDevice();

        if (this.actualBluetoothDevice == null) {
            throw new InternalErrorException("Unable to find a Robobo-ROB bluetooth device.");
        }

        try {
            //open a bluetoth connection with the device
            BluetoothSocket mmSocket = this.actualBluetoothDevice.createRfcommSocketToServiceRecord(UUID_BLUETOOTH_CONNECTION);

            if (smpRoboCom != null) {
                smpRoboCom.stop();
            }

            //create and configure the communciation channel
            this.androidBluetoothSPPChannel = new AndroidBluetoothSPPChannel(mmSocket, roboCommandFactory);

            androidBluetoothSPPChannel.connect();

            this.smpRoboCom = new SmpRobComm(androidBluetoothSPPChannel, roboCommandFactory);

            this.defaultRob = new DefaultRob(smpRoboCom);

            this.smpRoboCom.start();

            //set the default operation mode to secure-movement
            this.smpRoboCom.setOperationMode((byte)0);

            //set the default rob status period to 1 sec
            this.smpRoboCom.setRobStatusPeriod(1000);

        }
        catch(IOException e) {
            throw new InternalErrorException("Unable to connect to Robobo platform: "+getRobName());
        }


    }


    private BluetoothDevice lookForRoboboDevice() {



        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        String robName = getRobName();
        for(BluetoothDevice btDev : pairedDevices) {
            if (btDev.getName().equals(robName)) {
                return btDev;
            }
        }

        return null;

    }


    private String getRobName() {

        String robName = this.options.getString(ROBOBO_BT_NAME_OPTION);

        if (robName == null) {
            robName = ROB_NAME;
        }

        return robName;

    }



    @Override
    public void shutdown() throws InternalErrorException {

        if (this.smpRoboCom != null) {
            this.smpRoboCom.stop();
        }

        if (this.androidBluetoothSPPChannel != null) {
            try {
                this.androidBluetoothSPPChannel.close();
            } catch (IOException e) {
                throw new InternalErrorException(e);
            }
        }

    }



    @Override
    public String getModuleInfo() {
        return MODULE_INFO;
    }

    @Override
    public String getModuleVersion() {
        return MODULE_VERSION;
    }
}
