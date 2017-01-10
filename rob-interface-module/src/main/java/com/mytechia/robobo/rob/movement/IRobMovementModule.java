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

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.IModule;

/**
 * Provides a simplified interface to move the Robobo-ROB.
 *
 * @author Gervasio Varela
 */
public interface IRobMovementModule extends IModule {


    public void moveForwardsTime(int velocity, long time) throws InternalErrorException;
    public void moveForwardsAngle(int velocity, int angle) throws InternalErrorException;

    public void moveBackwardsTime(int velocity, long time) throws InternalErrorException;
    public void moveBackwardsAngle(int velocity, int angle) throws InternalErrorException;

    public void stop() throws InternalErrorException;

    public void turnLeftTime(int velocity, long time) throws InternalErrorException;
    public void turnLeftAngle(int velocity, int angle) throws InternalErrorException;

    public void turnRightTime(int velocity, long time) throws InternalErrorException;
    public void turnRightAngle(int velocity, int angle) throws InternalErrorException;

    public void turnLeftBackwardsTime(int velocity, long time) throws InternalErrorException;
    public void turnRightBackwardsTime(int velocity, long time) throws InternalErrorException;


    public void movePan(int velocity, int angle) throws InternalErrorException;

    public void moveTilt(int velocity, int angle) throws InternalErrorException;


}
