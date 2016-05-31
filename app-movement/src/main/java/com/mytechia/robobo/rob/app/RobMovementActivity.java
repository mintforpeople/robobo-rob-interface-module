/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Gervasio Varela <gervasio.varela@mytechia.com>
 *   Copyright 2016 Julio Gómez <julio.gomez@mytechia.com>
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

package com.mytechia.robobo.rob.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;
import com.mytechia.robobo.rob.BatteryStatus;
import com.mytechia.robobo.rob.BluetoothRobInterfaceModule;
import com.mytechia.robobo.rob.FallStatus;
import com.mytechia.robobo.rob.GapStatus;
import com.mytechia.robobo.rob.IRSensorStatus;
import com.mytechia.robobo.rob.IRob;
import com.mytechia.robobo.rob.IRobInterfaceModule;
import com.mytechia.robobo.rob.IRobStatusListener;
import com.mytechia.robobo.rob.LEDsModeEnum;
import com.mytechia.robobo.rob.MotorStatus;
import com.mytechia.robobo.rob.WallConnectionStatus;
import com.mytechia.robobo.rob.movement.IRobMovementModule;
import com.mytechia.robobo.rob.util.RoboboDeviceSelectionDialog;

import java.util.Collection;
import java.util.Date;

/** Custom Robobo display activity that provides a remote control of the ROB
 *
 * @author Gervasio Varela
 */
public class RobMovementActivity extends Activity {


    private RoboboServiceHelper roboboHelper;
    private RoboboManager roboboManager;
    private IRobMovementModule robMovement;
    private IRobInterfaceModule robModule;
    private IRob rob;


    private ImageButton btnForwards;
    private ImageButton btnBackwards;
    private ImageButton btnTurnLeft;
    private ImageButton btnTurnRight;
    private Button btnStop;

    private TextView lblTime;
    private SeekBar skBarTime;
    private TextView lblAngVel;
    private SeekBar skBarAngVel;
    private TextView lblAngle;
    private SeekBar skBarAngle;

    private TextView lblBarPan;
    private SeekBar skBarPan;
    private TextView lblBarTilt;
    private SeekBar skBarTilt;

    private TextView txtGaps;
    private TextView txtFalls;
    private TextView txtIRs;
    private TextView txtPan, txtTilt, txtLeft, txtRight;

    private TextView lblPeriod;
    private SeekBar skBarPeriod;


    private Button btnReset;
    private ToggleButton tglMode;
    private ToggleButton tglLeds;

    private TextView txtBattery;

    private TextView txtLastStatus;


    private BatteryStatus batteryStatus;
    private WallConnectionStatus wallConnectionStatus;

    private boolean useTime = true;

    private ProgressDialog waitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rob_movement);

        //wait to dialog shown during the startup of the framework and the bluetooth connection
        this.waitDialog = ProgressDialog.show(this,
                getString(R.string.dialogWaitConnectionTitle),
                getString(R.string.dialogWaitConnectionMsg), true);


        //buttons for movement control
        this.btnForwards = (ImageButton) findViewById(R.id.btnForward);
        this.btnBackwards = (ImageButton) findViewById(R.id.btnBarckward);
        this.btnTurnLeft = (ImageButton) findViewById(R.id.btnTurnLeft);
        this.btnTurnRight = (ImageButton) findViewById(R.id.btnTurnRight);
        this.btnStop = (Button) findViewById(R.id.btnStop);

        //bars for movement control
        this.lblTime = (TextView) findViewById(R.id.lblTime);
        this.skBarTime = (SeekBar) findViewById(R.id.skBarTime);
        this.lblAngVel = (TextView) findViewById(R.id.lblAngVel);
        this.skBarAngVel = (SeekBar) findViewById(R.id.skBarAngVel);
        this.lblAngle = (TextView) findViewById(R.id.lblAngle);
        this.skBarAngle = (SeekBar) findViewById(R.id.skBarAngle);

        //bars for pan & tilt
        this.lblBarPan = (TextView) findViewById(R.id.lblPanBar);
        this.skBarPan = (SeekBar) findViewById(R.id.skBarPan);
        this.lblBarTilt = (TextView) findViewById(R.id.lblTiltBar);
        this.skBarTilt = (SeekBar) findViewById(R.id.skBarTilt);


        //views for sensors
        this.txtGaps = (TextView) findViewById(R.id.txtGaps);
        this.txtFalls = (TextView) findViewById(R.id.txtFalls);
        this.txtIRs = (TextView) findViewById(R.id.txtIRs);
        this.txtPan = (TextView) findViewById(R.id.txtPan);
        this.txtTilt = (TextView) findViewById(R.id.txtTilt);
        this.txtLeft = (TextView) findViewById(R.id.txtLeft);
        this.txtRight = (TextView) findViewById(R.id.txtRight);
        this.txtBattery = (TextView) findViewById(R.id.txtBattery);

        //bar for the status period
        this.lblPeriod = (TextView) findViewById(R.id.lblStatusPeriod);
        this.skBarPeriod = (SeekBar) findViewById(R.id.skBarStatusPeriod);

        //button bar with some utility buttons
        this.btnReset = (Button) findViewById(R.id.btnResetPanTilt);
        this.tglMode = (ToggleButton) findViewById(R.id.tglMode);
        this.tglLeds = (ToggleButton) findViewById(R.id.tglLeds);

        //last time where an status update was received
        this.txtLastStatus = (TextView) findViewById(R.id.txtLastStatus);

        //show the device selection dialog and wait until the user selects a device
        showRoboboDeviceSelectionDialog();

    }


    /** Shows a Robobo device selection dialog, suscribes to device selection
     * events to "wait" until the user selects a device, and then starts
     * the Robobo Framework using the RoboboHelper inside an AsyncTask to
     * not freeze the UI code.
     */
    private void showRoboboDeviceSelectionDialog() {

        RoboboDeviceSelectionDialog dialog = new RoboboDeviceSelectionDialog();
        dialog.addListener(new RoboboDeviceSelectionDialog.Listener() {
            @Override
            public void roboboSelected(String roboboName) {

                final String roboboBluetoothName = roboboName;

                //start the framework in background
                AsyncTask<Void, Void, Void> launchRoboboService =
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                launchAndConnectRoboboService(roboboBluetoothName);
                                return null;
                            }
                        };
                launchRoboboService.execute();

            }

            @Override
            public void selectionCancelled() {
                showErrorDialog("No device selected.");
            }
        });
        dialog.show(getFragmentManager(),"BLUETOOTH-DIALOG");

    }


    private void launchAndConnectRoboboService(String roboboBluetoothName) {

        //we use the RoboboServiceHelper class to manage the startup and binding
        //of the Robobo Manager service and Robobo modules
        roboboHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
            @Override
            public void onRoboboManagerStarted(RoboboManager robobo) {

                //the robobo service and manager have been started up
                roboboManager = robobo;

                //dismiss the wait dialog
                waitDialog.dismiss();

                //start the "custom" robobo application
                startRoboboApplication();

            }

            @Override
            public void onError(String errorMsg) {

                final String error = errorMsg;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //dismiss the wait dialog
                        waitDialog.dismiss();

                        //show an error dialog
                        showErrorDialog(error);

                    }
                });

            }

        });

        //start & bind the Robobo service
        Bundle options = new Bundle();
        options.putString(BluetoothRobInterfaceModule.ROBOBO_BT_NAME_OPTION, roboboBluetoothName);
        roboboHelper.bindRoboboService(options);

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        //we unbind and (maybe) stop the Robobo service on exit
        if (roboboHelper != null) {
            roboboHelper.unbindRoboboService();
        }

    }


    /** Starts our "custom" Robobo application
     */
    private void startRoboboApplication() {

        try {

            //easy-to-use platform movement module
            this.robMovement = this.roboboManager.getModuleInstance(IRobMovementModule.class);

            //low-level platform control module
            this.robModule = this.roboboManager.getModuleInstance(IRobInterfaceModule.class);
            this.rob = this.robModule.getRobInterface();

        } catch (ModuleNotFoundException e) {
            showErrorDialog(e.getMessage());
        }

        //configure the listeners for the different view objects of the GUI
        setMovementBarsListeners();
        setMovementButtonsListeners();
        setPanTiltBarsListeners();
        setStatusPeriodBarListener();
        setButtonBarListeners();


        //configure a listener to receive status data from the Robobo platform
        setRobStatusListener();

    }


    /** Configures a listener to receive periodic status information from the Robobo platform.
     */
    private void setRobStatusListener() {

        this.rob.addRobStatusListener(new IRobStatusListener() {
            @Override
            public void statusMotorsMT(MotorStatus motorStatus, MotorStatus motorStatus1) {
                Log.d("MOVEMENT", "Motor MT "+motorStatus+" - "+motorStatus1);
                setMotor(txtLeft, motorStatus);
                setMotor(txtRight, motorStatus1);
                updateLastStatus();
            }

            @Override
            public void statusMotorPan(MotorStatus motorStatus) {
                Log.d("MOVEMENT", "Motor Pan");
                setMotor(txtPan, motorStatus);
                skBarPan.setProgress(motorStatus.getVariationAngle());
                updateLastStatus();
            }

            @Override
            public void statusMotorTilt(MotorStatus motorStatus) {
                Log.d("MOVEMENT", "Motor Tilt");
                setMotor(txtTilt, motorStatus);
                skBarTilt.setProgress(motorStatus.getVariationAngle());
                updateLastStatus();
            }

            @Override
            public void statusGaps(Collection<GapStatus> gapStatus) {
                Log.d("MOVEMENT", "Gaps");
                final Collection<GapStatus> gaps = gapStatus;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setGaps(gaps);
                    }
                });
                updateLastStatus();
            }

            @Override
            public void statusFalls(Collection<FallStatus> fallStatus) {
                Log.d("MOVEMENT", "Falls");
                final Collection<FallStatus> falls = fallStatus;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFalls(falls);
                    }
                });
                updateLastStatus();
            }

            @Override
            public void statusBattery(BatteryStatus batteryStatus) {
                Log.d("MOVEMENT", "Battery");
                RobMovementActivity.this.batteryStatus = batteryStatus;
                updateBattery();
                updateLastStatus();
            }

            @Override
            public void statusWallConnectionStatus(WallConnectionStatus wallConnectionStatus) {
                RobMovementActivity.this.wallConnectionStatus = wallConnectionStatus;
                updateBattery();
                updateLastStatus();
            }

            @Override
            public void statusIRSensorStatus(final Collection<IRSensorStatus> irSensorStatus) {
                Log.d("MOVEMENT", "IRs");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setIRs(irSensorStatus);
                    }
                });
                updateLastStatus();
            }
        });

    }


    private void setMovementButtonsListeners() {

        //forward button
        this.btnForwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime) {
                    robMovement.moveForwardsTime(getAngVel(), getTime());
                }
                else {
                    robMovement.moveForwardsAngle(getAngVel(), getAngle());
                }
            }
        });

        //backward button
        this.btnBackwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime) {
                    robMovement.moveBackwardsTime(getAngVel(), getTime());
                }
                else {
                    robMovement.moveBackwardsAngle(getAngVel(), getAngle());
                }
            }
        });

        //turn left button
        this.btnTurnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime) {
                    robMovement.turnLeftTime(getAngVel(), getTime());
                }
                else {
                    robMovement.turnLeftAngle(getAngVel(), getAngle());
                }
            }
        });

        //turn right button
        this.btnTurnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime) {
                    robMovement.turnRightTime(getAngVel(), getTime());
                }
                else {
                    robMovement.turnRightAngle(getAngVel(), getAngle());
                }
            }
        });

        //stop all movement button
        this.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robMovement.stop();
            }
        });

    }


    private void setButtonBarListeners() {

        //pan/tilt reset
        this.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rob.resetPanTiltOffset();
            }
        });

        //secure/unsecure movement mode
        this.tglMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rob.setLEDsMode(LEDsModeEnum.INFRARED_AND_DETECT_FALL);
                }
                else {
                    rob.setLEDsMode(LEDsModeEnum.NONE);
                }
            }
        });

        //ir led toggle mode
        this.tglMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rob.setOperationMode((byte)0);
                }
                else {
                    rob.setOperationMode((byte)1);
                }
            }
        });

    }



    private void setStatusPeriodBarListener() {

        //STATUS PERIOD BAR
        this.skBarPeriod.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //update label value
                setLblValue(lblPeriod, R.string.lblStatusPeriod, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //CHANGE STATUS PERIOD
                rob.setRobStatusPeriod(seekBar.getProgress());
            }
        });

    }


    private void setMovementBarsListeners() {

        //MOVEMENT SEEK BAR MAGANEMENT
        this.skBarAngVel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //the angular velocity bar has been changed, update the value in the label
                setLblValue(lblAngVel, R.string.lblAngVel, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        this.skBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //the time bar has been changed, set the mode to USE TIME and update the value in the label
                useTime();
                setLblValue(lblTime, R.string.lblTime, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        this.skBarAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //the angle bar has been changed, set the mode to USE ANGLE and update the value in the label
                useAngle();
                setLblValue(lblAngle, R.string.lblAngle, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }


    private void setPanTiltBarsListeners() {

        //PAN & TILT SEEK BAR MANAGEMENT
        this.skBarPan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //update label value
                setLblValue(lblBarPan, R.string.lblPanBar, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //send a move PAN command
                robMovement.movePan(seekBar.getProgress());
            }
        });

        this.skBarTilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //update label value
                setLblValue(lblBarTilt, R.string.lblTiltBar, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //send a move TILT command
                robMovement.moveTilt(seekBar.getProgress());
            }
        });

    }


    private void setLblValue(TextView lbl, int lblId, int value) {
        String txt = getText(lblId)+" ("+value+")";
        lbl.setText(txt);
    }


    /** Configures the app and UI to use time for the movement commands
     */
    private void useTime() {
        this.useTime = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                skBarAngle.setProgress(0);
            }
        });
    }

    /** Configures the app and UI to use the angle for the movement commands
     */
    private void useAngle() {
        this.useTime = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                skBarTime.setProgress(0);
            }
        });
    }


    /** Updates the battery information in the GUI
     */
    private void updateBattery() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtBattery.setText(String.valueOf(batteryStatus.getBattery()) + " ("+getWallConnectionStatus()+")");
            }
        });

    }


    private String getWallConnectionStatus() {
        if (this.wallConnectionStatus.getWallConnetion() == (byte)1) {
            return getString(R.string.txtBatteryCharging);
        }
        else {
            return getString(R.string.txtBatteryDischarging);
        }
    }


    /** Updates the motors information in the GUI
     *
     * @param txtMotor
     * @param motors
     */
    private void setMotor(final TextView txtMotor, MotorStatus motors) {

        final StringBuilder sb = new StringBuilder("");

        sb.append(motors.getAngularVelocity());
        sb.append(" ");
        sb.append(motors.getVariationAngle());
        sb.append(" ");
        sb.append(motors.getVoltage());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtMotor.setText(sb.toString());
            }
        });

    }


    /** Updates the Gap detection information in the GUI
     *
     * @param gaps
     */
    private void setGaps(Collection<GapStatus> gaps) {

        StringBuilder sb = new StringBuilder("");

        for(GapStatus g : gaps) {
            if (g.isGap()) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }
            sb.append(" ");
        }

        this.txtGaps.setText(sb.toString());

    }


    /** Updates the Fall detection information in the GUI
     *
     * @param falls
     */
    private void setFalls(Collection<FallStatus> falls) {

        StringBuilder sb = new StringBuilder("");

        for(FallStatus f : falls) {
            if (f.isFall()) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }
            sb.append(" ");
        }

        this.txtFalls.setText(sb.toString());

    }

    /** Updates the IR sensors status in the GUI
     *
     * @param irSensorStatus
     */
    private void setIRs(Collection<IRSensorStatus> irSensorStatus) {

        StringBuilder sb = new StringBuilder("");

        for(IRSensorStatus ir : irSensorStatus) {
            sb.append(ir.getDistance());
            sb.append(" ");
        }

        this.txtIRs.setText(sb.toString());

    }


    /** Gets the time of movement from the time bar
     * @return the time of movement (ms)
     */
    private int getTime() {
        return this.skBarTime.getProgress();
    }

    /** Gets the angular velocity of movement from the angular velocity bar
     * @return the angle of movement (ms)
     */
    private short getAngVel() {
        return (short) this.skBarAngVel.getProgress();
    }

    /** Gets the angle of movement from the angle bar
     * @return the angle of movement (ms)
     */
    private int getAngle() {
        return this.skBarAngle.getProgress();
    }


    /** Updates the time of the last platform status message received.
     */
    private void updateLastStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLastStatus.setText((new Date()).toString());
            }
        });
    }


    /** Shows an error dialog with the message 'msg'
     *
     * @param msg the message to be shown in the error dialog
     */
    protected void showErrorDialog(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(com.mytechia.robobo.framework.R.string.title_error_dialog).
                setMessage(msg);
        builder.setPositiveButton(com.mytechia.robobo.framework.R.string.ok_msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
        .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
