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


    public void moveForwardsTime(short velocity, long time) throws InternalErrorException;
    public void moveForwardsAngle(short velocity, int angle) throws InternalErrorException;

    public void moveBackwardsTime(short velocity, long time) throws InternalErrorException;
    public void moveBackwardsAngle(short velocity, int angle) throws InternalErrorException;

    public void stop() throws InternalErrorException;

    public void turnLeftTime(short velocity, long time) throws InternalErrorException;
    public void turnLeftAngle(short velocity, int angle) throws InternalErrorException;

    public void turnRightTime(short velocity, long time) throws InternalErrorException;
    public void turnRightAngle(short velocity, int angle) throws InternalErrorException;

    public void turnLeftBackwardsTime(short velocity, long time) throws InternalErrorException;
    public void turnRightBackwardsTime(short velocity, long time) throws InternalErrorException;


    public void movePan(short velocity, int angle) throws InternalErrorException;

    public void moveTilt(short velocity, int angle) throws InternalErrorException;


}
