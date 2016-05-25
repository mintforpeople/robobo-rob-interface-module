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

/**
 * The default implementation of the IRovMovementInterfaceModule.
 *
 * @author Gervasio Varela
 */
public class DefaultRobMovementModule implements IRobMovementModule {


    private static final String MODULE_INFO = "Robobo-ROB movement module.";
    private static final String MODULE_VERSION= "0.1.0";

    private IRobInterfaceModule robModule;
    private IRob rob;


    @Override
    public void moveForwards(short velocity, long time) {
        Log.d("ROB-INTERFACE", String.format("Move forwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)velocity, (byte)velocity, time);
    }

    @Override
    public void moveForwards(short velocity, int angle) {
        Log.d("ROB-INTERFACE", String.format("Move forwards: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)velocity, angle, (byte)velocity, angle);
    }

    @Override
    public void moveBackwards(short velocity, long time) {
        Log.d("ROB-INTERFACE", String.format("Move backwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.REVERSE_REVERSE, (byte)velocity, (byte)velocity, time);
    }

    @Override
    public void moveBackwards(short velocity, int angle) {
        Log.d("ROB-INTERFACE", String.format("Move backwards: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.REVERSE_REVERSE, (byte)velocity, angle, (byte)velocity, angle);
    }

    @Override
    public void stop() {
        Log.d("ROB-INTERFACE", "Stop.");
        this.rob.moveMT(MoveMTMode.STOP_STOP, (byte)0, (byte)0, 0);
    }

    @Override
    public void turnLeft(short velocity, long time) {
        Log.d("ROB-INTERFACE", String.format("Turn left: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)velocity, (byte)0, time);
    }

    @Override
    public void turnLeft(short velocity, int angle) {
        Log.d("ROB-INTERFACE", String.format("Turn left: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, velocity, angle, (short) 0, 0);
    }

    @Override
    public void turnLeftBackwards(short velocity, long time) {
        Log.d("ROB-INTERFACE", String.format("Turn left backwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)0,(byte) velocity, time);
    }

    @Override
    public void turnRight(short velocity, long time) {
        Log.d("ROB-INTERFACE", String.format("Turn right: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)velocity, (byte)0, time);
    }

    @Override
    public void turnRight(short velocity, int angle) {
        Log.d("ROB-INTERFACE", String.format("Turn right: %d - %d", velocity, angle));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (short) 0, 0, velocity, angle);
    }

    @Override
    public void turnRightBackwards(short velocity, long time) {
        Log.d("ROB-INTERFACE", String.format("Turn right backwards: %d - %d", velocity, time));
        this.rob.moveMT(MoveMTMode.FORWARD_FORWARD, (byte)0, (byte)velocity, time);
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
