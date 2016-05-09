package com.mytechia.robobo.rob.movement;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.FrameworkManager;
import com.mytechia.robobo.rob.IRob;
import com.mytechia.robobo.rob.IRobInterfaceModule;

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
    public void moveForwards(int velocity, long time) {
        this.rob.moveMT(velocity, velocity, time);
    }

    @Override
    public void moveBackwards(int velocity, long time) {
        this.rob.moveMT(-velocity, -velocity, time);
    }

    @Override
    public void stop() {
        this.rob.moveMT(0, 0, 0);
    }

    @Override
    public void turnLeft(int velocity, long time) {
        this.rob.moveMT(velocity, 0, time);
    }

    @Override
    public void turnLeftBackwards(int velocity, long time) {
        this.rob.moveMT(-velocity, 0, time);
    }

    @Override
    public void turnRight(int velocity, long time) {
        this.rob.moveMT(0, velocity, time);
    }

    @Override
    public void turnRightBackwards(int velocity, long time) {
        this.rob.moveMT(0, velocity, time);
    }




    @Override
    public void startup(FrameworkManager manager) throws InternalErrorException {

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
