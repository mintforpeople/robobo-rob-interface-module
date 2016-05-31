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

package com.mytechia.robobo.rob.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mytechia.robobo.rob.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/** A pop-up dialog that asks the user to select a particular Robobo bluetooth device
 *
 * @author Gervasio Varela
 */
public class RoboboDeviceSelectionDialog extends DialogFragment {


    private ArrayList<Listener> listeners = new ArrayList<>(1);


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //get the names of the devices paired
        final String[] roboboNames = getBtPairedDevicesNames();

        //show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.bluetooth_dialog_title))
                .setItems(roboboNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //notify the selection to the listeners
                        notifyRoboboName(roboboNames[which]);

                    }
                });
        return builder.create();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        notifySelectionCacelled();
    }

    /** Returns an array with the names of all the bluetooth devices paired with this smartphone
     *
     * @return an array with the names of all the bluetooth devices paired with this smartphone
     */
    public String[] getBtPairedDevicesNames() {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        String[] devicesNames = new String[pairedDevices.size()];
        int i=0;
        for(BluetoothDevice btDev : pairedDevices) {
            devicesNames[i] = btDev.getName();
            i++;
        }

        return devicesNames;

    }


    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }


    public void notifyRoboboName(String roboboName) {
        Iterator<Listener> listenerIter = this.listeners.iterator();
        while(listenerIter.hasNext()) {
            listenerIter.next().roboboSelected(roboboName);
        }
    }

    public void notifySelectionCacelled() {
        Iterator<Listener> listenerIter = this.listeners.iterator();
        while(listenerIter.hasNext()) {
            listenerIter.next().selectionCancelled();
        }
    }


    /** Receives notifications of the selection of a Robobo bluetooth device
     * from the selection dialog.
     *
     */
    public interface Listener {


        /** Notifies the name of the Robobo device selected by the user
         *
         * @param roboboName the name of the Robobo device selected by the user
         */
        public void roboboSelected(String roboboName);


        public void selectionCancelled();

    }

}
