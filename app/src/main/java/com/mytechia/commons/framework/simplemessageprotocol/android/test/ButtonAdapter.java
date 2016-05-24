package com.mytechia.commons.framework.simplemessageprotocol.android.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.mytechia.robobo.rob.DefaultRob;
import com.mytechia.robobo.rob.LEDsModeEnum;
import com.mytechia.robobo.rob.MoveMTMode;
import com.mytechia.robobo.rob.comm.SmpRobComm;
import com.mytechia.robobo.util.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by julio on 20/07/15.
 */
public class ButtonAdapter extends BaseAdapter {

    private Context context;

    public final List<String> roboCommandsList = new ArrayList<String>();

    private final Map<String, View.OnClickListener> mapRoboCommands = new LinkedHashMap<>();

    private DefaultRob defaultRob;

    private SmpRobComm smpRobComm;


    public ButtonAdapter(Context context) {

        this.context = context;

        initRoboCommandsList();
    }


    @Override
    public int getCount() {
        return roboCommandsList.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setDefaultRob(DefaultRob defaultRob, SmpRobComm smpRobComm) {

        this.defaultRob = defaultRob;

        this.smpRobComm= smpRobComm;

        if (defaultRob != null) {
            initActions();
        }

        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView = null;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.rob_command_list, null);

            Button btnAction = (Button) gridView.findViewById(R.id.btnSendRoboCommand);

            String nameCommand = roboCommandsList.get(position);

            btnAction.setText(nameCommand);

        } else {
            gridView = (View) convertView;
        }

        Button btnAction = (Button) gridView.findViewById(R.id.btnSendRoboCommand);

        if (defaultRob != null) {
            btnAction.setOnClickListener(mapRoboCommands.get(btnAction.getText()));
        } else {
            btnAction.setOnClickListener(null);
        }

        return gridView;
    }

    private void initRoboCommandsList() {
        roboCommandsList.add("setLEDColorMessage");
        roboCommandsList.add("robSetLEDsModeMessage");
        roboCommandsList.add("moveMTAngles");
        roboCommandsList.add("moveMTTimes");
        roboCommandsList.add("movePanAngles");
        roboCommandsList.add("moveTiltAngles");
        roboCommandsList.add("resetPanTiltOffset");
        roboCommandsList.add("setRobStatusPeriod");
        roboCommandsList.add("configureInfrared");
        roboCommandsList.add("maxValueMotors");
        roboCommandsList.add("setOperationMode");

    }

    private void initActions() {

        mapRoboCommands.put(roboCommandsList.get(0), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int led = 2;
                int red = 4095;
                int green = 4095;
                int blue = 0;

                defaultRob.setLEDColor(led, new Color(red, green, blue));
            }
        });

        mapRoboCommands.put(roboCommandsList.get(1), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LEDsModeEnum mode = LEDsModeEnum.INFRARED_AND_DETECT_FALL;

                defaultRob.setLEDsMode(mode);

            }
        });

        mapRoboCommands.put(roboCommandsList.get(2), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                short angVel1 = 100;
                int angle1 = 180;
                short angVel2 = 200;
                int angle2 = 10_000;

                defaultRob.moveMT(MoveMTMode.FORWARD_FORWARD, angVel1, angle1, angVel2, angle2);

            }
        });

        mapRoboCommands.put(roboCommandsList.get(3), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                short angVel1 = 100;
                short angVel2 = 100;
                long time=10000;

                defaultRob.moveMT(MoveMTMode.FORWARD_FORWARD, angVel1,  angVel2, time);

            }
        });





        mapRoboCommands.put(roboCommandsList.get(4), new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                short angVel = 120;
                short angle = 76;

                defaultRob.movePan(angVel, angle);

            }
        });


        mapRoboCommands.put(roboCommandsList.get(5), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                short angVel = 120;
                short angle = 76;

                defaultRob.moveTilt(angVel, angle);

            }
        });




        mapRoboCommands.put(roboCommandsList.get(6), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                defaultRob.resetPanTiltOffset();
            }
        });


        mapRoboCommands.put(roboCommandsList.get(7), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                defaultRob.setRobStatusPeriod(2000);
            }
        });


        mapRoboCommands.put(roboCommandsList.get(8), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                smpRobComm.infraredConfiguration((byte)3, (byte)3, (byte)192, (byte)8);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                smpRobComm.infraredConfiguration((byte)3, (byte)4, (byte)0, (byte)135);
            }
        });

        mapRoboCommands.put(roboCommandsList.get(9), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                defaultRob.maxValueMotors(12, 12, 13, 13, 15, 15, 16, 16);
            }
        });

        mapRoboCommands.put(roboCommandsList.get(10), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                defaultRob.setOperationMode((byte) 1);
            }
        });


    }
}
