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

package com.mytechia.robobo.rob.movement;

import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;

import com.mytechia.robobo.framework.RoboboManager;

import com.mytechia.robobo.rob.BatteryStatus;
import com.mytechia.robobo.rob.BumpStatus;
import com.mytechia.robobo.rob.FallStatus;
import com.mytechia.robobo.rob.GapStatus;
import com.mytechia.robobo.rob.IRob;
import com.mytechia.robobo.rob.IRobInterfaceModule;
import com.mytechia.robobo.rob.IRobStatusListener;
import com.mytechia.robobo.rob.MotorStatus;
import com.mytechia.robobo.rob.MoveMTMode;
import com.mytechia.robobo.rob.ObstacleSensorStatus;
import com.mytechia.robobo.framework.RoboboManager;


/**
 * The default implementation of the IRovMovementInterfaceModule.
 *
 * @author Gervasio Varela
 */
public class DefaultRobMovementModule implements IRobMovementModule {


    private static final String MODULE_INFO = "Robobo-ROB movement module.";
    private static final String MODULE_VERSION= "0.1.0";

    private static final short MAX_PANTILT_VELOCITY = (short) 6; //safe velocity

    private IRobInterfaceModule robModule;
    private IRob rob;


    @Override
    public void moveForwardsTime(short velocity, long time) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Move forwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)velocity, (byte)velocity, time);
    }

    @Override
    public void moveForwardsAngle(short velocity, int angle) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Move forwards: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)velocity, angle, (byte)velocity, angle);
    }

    @Override
    public void moveBackwardsTime(short velocity, long time) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Move backwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.REVERSE_REVERSE, (byte)velocity, (byte)velocity, time);
    }

    @Override
    public void moveBackwardsAngle(short velocity, int angle) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Move backwards: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.REVERSE_REVERSE, (byte)velocity, angle, (byte)velocity, angle);
    }

    @Override
    public void stop() throws InternalErrorException {
        Log.d("ROB-INTERFACE", "Stop.");
        this.rob.moveMT(MoveMTMode.STOP_STOP, (byte)0, (byte)0, 0);
    }

    @Override
    public void turnLeftTime(short velocity, long time) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Turn left: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)0, (byte)velocity, time);
    }

    @Override
    public void turnLeftAngle(short velocity, int angle) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Turn left: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (short) 0, 0, velocity, angle);
    }

    @Override
    public void turnLeftBackwardsTime(short velocity, long time) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Turn left backwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)0,(byte) velocity, time);
    }

    @Override
    public void turnRightTime(short velocity, long time) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Turn right: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)velocity, (byte)0, time);
    }

    @Override
    public void turnRightAngle(short velocity, int angle) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Turn right: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, velocity, angle, (short) 0, 0);
    }

    @Override
    public void turnRightBackwardsTime(short velocity, long time) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Turn right backwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)0, (byte)velocity, time);
    }

    @Override
    public void movePan(int angle) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Move pan: %d ", angle));
        this.rob.movePan(MAX_PANTILT_VELOCITY, angle);
    }

    @Override
    public void moveTilt(int angle) throws InternalErrorException {
        Log.d("ROB-INTERFACE", String.format("Move tilt: %d ", angle));
        this.rob.moveTilt(MAX_PANTILT_VELOCITY, angle);
    }



    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {

        this.robModule = manager.getModuleInstance(IRobInterfaceModule.class);
        if (this.robModule != null) {
            this.rob = this.robModule.getRobInterface();
        }

    }

    @Override
    public void shutdown() throws InternalErrorException {

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
