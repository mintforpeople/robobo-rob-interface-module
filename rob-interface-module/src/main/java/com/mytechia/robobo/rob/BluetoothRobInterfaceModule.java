/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Gervasio Varela <gervasio.varela@mytechia.com>
 *
 *   This file is part of Robobo Framework Library.
 *
 *   Robobo Framework Library is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo Framework Library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo Framework Library.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package com.mytechia.robobo.rob;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.commons.framework.simplemessageprotocol.android.AndroidBluetoothSPPChannel;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.rob.comm.RoboCommandFactory;
import com.mytechia.robobo.rob.comm.SmpRobComm;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;

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

    private BluetoothDevice actualBluetoothDevice;

    private AndroidBluetoothSPPChannel androidBluetoothSPPChannel;
    private BluetoothSocket mmSocket;
    private SmpRobComm smpRoboCom;

    private RoboCommandFactory roboCommandFactory = new RoboCommandFactory();
    private DefaultRob defaultRob;


    /** Returns an instance of the IRob interface for send/receive commands to a Robobo-ROB
     *
     * @return the instanfe of the IRob interface for the connected ROB
     */
    public IRob getRobInterface() {
        return this.defaultRob;
    }


    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {

        if(this.actualBluetoothDevice!=null){

            if(androidBluetoothSPPChannel!=null) {

                try {
                    androidBluetoothSPPChannel.close();
                } catch (IOException e) {
                    throw new InternalErrorException(e);
                }

            }
        }

        Log.d("ROB-INTERFACE", "Looking for Robobo-ROB devices via bluetooth.");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        for(BluetoothDevice btDev : pairedDevices) {
            if (btDev.getName().equals(ROB_NAME)) {
                this.actualBluetoothDevice = btDev;
            }
        }

        if (this.actualBluetoothDevice == null) {
            throw new InternalErrorException("Unable to find a Robobo-ROB bluetooth device.");
        }

        try {
            BluetoothSocket mmSocket = this.actualBluetoothDevice.createRfcommSocketToServiceRecord(UUID_BLUETOOTH_CONNECTION);

            if (smpRoboCom != null) {
                smpRoboCom.stop();
            }

            this.androidBluetoothSPPChannel = new AndroidBluetoothSPPChannel(mmSocket, roboCommandFactory);

            androidBluetoothSPPChannel.connect();

            this.smpRoboCom = new SmpRobComm(androidBluetoothSPPChannel, roboCommandFactory);

            this.defaultRob = new DefaultRob(smpRoboCom);

            this.smpRoboCom.start();

            this.smpRoboCom.setRobStatusPeriod(1000);

            this.smpRoboCom.setLEDsMode((byte)8);

        }
        catch(IOException e) {
            throw new InternalErrorException(e);
        }


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
