package com.mytechia.robobo.rob.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mytechia.robobo.framework.FrameworkManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.rob.BatteryStatus;
import com.mytechia.robobo.rob.FallStatus;
import com.mytechia.robobo.rob.GapStatus;
import com.mytechia.robobo.rob.IRob;
import com.mytechia.robobo.rob.IRobInterfaceModule;
import com.mytechia.robobo.rob.IRobStatusListener;
import com.mytechia.robobo.rob.MotorStatus;
import com.mytechia.robobo.rob.MoveMTMode;
import com.mytechia.robobo.rob.movement.IRobMovementModule;

import java.util.Collection;
import java.util.Date;

public class RobMovementActivity extends Activity {

    private FrameworkManager roboboManager;
    private IRobMovementModule robMovement;
    private IRobInterfaceModule robModule;
    private IRob rob;

    private Button btnForwards;
    private Button btnBackwards;
    private Button btnTurnLeft;
    private Button btnTurnRight;
    private Button btnStop;

    private TextView lblTime;
    private SeekBar skBarTime;
    private TextView lblAngVel;
    private SeekBar skBarAngVel;
    private TextView lblAngle;
    private SeekBar skBarAngle;

    private RadioButton radioTime;
    private RadioButton radioAngle;

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

    private TextView txtLastStatus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rob_movement);


        this.btnForwards = (Button) findViewById(R.id.btnForward);
        this.btnBackwards = (Button) findViewById(R.id.btnBarckward);
        this.btnTurnLeft = (Button) findViewById(R.id.btnTurnLeft);
        this.btnTurnRight = (Button) findViewById(R.id.btnTurnRight);
        this.btnStop = (Button) findViewById(R.id.btnStop);

        this.lblTime = (TextView) findViewById(R.id.lblTime);
        this.skBarTime = (SeekBar) findViewById(R.id.skBarTime);
        this.lblAngVel = (TextView) findViewById(R.id.lblAngVel);
        this.skBarAngVel = (SeekBar) findViewById(R.id.skBarAngVel);
        this.lblAngle = (TextView) findViewById(R.id.lblAngle);
        this.skBarAngle = (SeekBar) findViewById(R.id.skBarAngle);

        this.radioTime = (RadioButton) findViewById(R.id.radioTime);
        this.radioAngle = (RadioButton) findViewById(R.id.radioAngle);

        this.lblBarPan = (TextView) findViewById(R.id.lblPanBar);
        this.skBarPan = (SeekBar) findViewById(R.id.skBarPan);
        this.lblBarTilt = (TextView) findViewById(R.id.lblTiltBar);
        this.skBarTilt = (SeekBar) findViewById(R.id.skBarTilt);


        this.txtGaps = (TextView) findViewById(R.id.txtGaps);
        this.txtFalls = (TextView) findViewById(R.id.txtFalls);
        this.txtIRs = (TextView) findViewById(R.id.txtIRs);

        this.txtPan = (TextView) findViewById(R.id.txtPan);
        this.txtTilt = (TextView) findViewById(R.id.txtTilt);
        this.txtLeft = (TextView) findViewById(R.id.txtLeft);
        this.txtRight = (TextView) findViewById(R.id.txtRight);

        this.lblPeriod = (TextView) findViewById(R.id.lblStatusPeriod);
        this.skBarPeriod = (SeekBar) findViewById(R.id.skBarStatusPeriod);

        this.txtLastStatus = (TextView) findViewById(R.id.txtLastStatus);



        this.roboboManager = FrameworkManager.getInstance();
        try {

            this.robMovement = this.roboboManager.getModuleInstance(IRobMovementModule.class);
            this.robModule = this.roboboManager.getModuleInstance(IRobInterfaceModule.class);
            this.rob = this.robModule.getRobInterface();

        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }


        //MOVEMENT BUTTONS MANAGEMENT
        this.btnForwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime()) {
                    robMovement.moveForwards(getAngVel(), getTime());
                }
                else {
                    robMovement.moveBackwards(getAngVel(), getAngle());
                }
            }
        });

        this.btnBackwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime()) {
                    robMovement.moveBackwards(getAngVel(), getTime());
                }
                else {
                    robMovement.moveBackwards(getAngVel(), getAngle());
                }
            }
        });

        this.btnTurnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime()) {
                    robMovement.turnLeft(getAngVel(), getTime());
                }
                else {
                    robMovement.turnLeft(getAngVel(), getAngle());
                }
            }
        });

        this.btnTurnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTime()) {
                    robMovement.turnRight(getAngVel(), getTime());
                }
                else {
                    robMovement.turnRight(getAngVel(), getAngle());
                }
            }
        });

        this.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robMovement.stop();
            }
        });


        //MOVEMENT SEEK BAR MAGANEMENT
        this.skBarAngVel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setLblValue(lblAngVel, R.string.lblAngVel, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.skBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setLblValue(lblTime, R.string.lblTime, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.skBarAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setLblValue(lblAngle, R.string.lblAngle, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //PAN & TILT SEEK BAR MANAGEMENT
        this.skBarPan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setLblValue(lblBarPan, R.string.lblPanBar, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //PAN COMMAND
                rob.movePan(getAngVel(), seekBar.getProgress());
            }
        });

        this.skBarTilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setLblValue(lblBarTilt, R.string.lblTiltBar, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TILT COMMAND
                rob.moveTilt(getAngVel(), seekBar.getProgress());
            }
        });


        //STATUS PERIOD BAR
        this.skBarPeriod.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setLblValue(lblPeriod, R.string.lblStatusPeriod, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //CHANGE STATUS PERIOD
                rob.setRobStatusPeriod(seekBar.getProgress());
            }
        });



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
                updateLastStatus();
            }

            @Override
            public void statusMotorTilt(MotorStatus motorStatus) {
                Log.d("MOVEMENT", "Motor Tilt");
                setMotor(txtTilt, motorStatus);
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
                updateLastStatus();
            }
        });


    }


    private void setLblValue(TextView lbl, int lblId, int value) {
        String txt = getText(lblId)+" ("+value+")";
        lbl.setText(txt);
    }


    private boolean useTime() {

        return radioTime.isChecked();

    }


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

        this.txtGaps.setText(sb.toString());

    }


    private int getTime() {
        return this.skBarTime.getProgress();
    }

    private short getAngVel() {
        return (short) this.skBarAngVel.getProgress();
    }

    private int getAngle() {
        return this.skBarAngle.getProgress();
    }


    private void updateLastStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLastStatus.setText((new Date()).toString());
            }
        });
    }


}
