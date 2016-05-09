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

package com.mytechia.robobo.rob.movement;

import com.mytechia.robobo.framework.IModule;

/**
 * Provides a simplified interface to move the Robobo-ROB.
 *
 * @author Gervasio Varela
 */
public interface IRobMovementModule extends IModule {


    public void moveForwards(int velocity, long time);

    public void moveBackwards(int velocity, long time);

    public void stop();

    public void turnLeft(int velocity, long time);
    public void turnLeftBackwards(int velocity, long time);

    public void turnRight(int velocity, long time);
    public void turnRightBackwards(int velocity, long time);


}
