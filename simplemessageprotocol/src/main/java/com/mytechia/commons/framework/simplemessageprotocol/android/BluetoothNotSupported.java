/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Julio Gomez <julio.gomez@mytechia.com>
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

package com.mytechia.commons.framework.simplemessageprotocol.android;

/**
 * Created by julio on 1/04/16.
 */
public class BluetoothNotSupported extends RuntimeException{

    public BluetoothNotSupported(Throwable throwable) {
        super(throwable);
    }

    public BluetoothNotSupported(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BluetoothNotSupported(String detailMessage) {
        super(detailMessage);
    }
}
