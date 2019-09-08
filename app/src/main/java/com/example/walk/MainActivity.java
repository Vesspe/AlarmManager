package com.example.walk;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;

    private static final String ACTION_NOTIFY = "com.example.android.walk.ACTION_NOTIFY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //        Intent contentIntent = new Intent(this, MainActivity.class);
//
//        final PendingIntent contentPendingIntent = PendingIntent.getActivity
//                (this,NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //check if alarm is already set and toggle accordingly

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton alarmToggleButton = (ToggleButton) findViewById(R.id.alarmToggle);
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Notification Broadcast Intent
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        boolean alarmUp = (PendingIntent.getBroadcast
                (this, NOTIFICATION_ID,notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggleButton.setChecked(alarmUp);

        //Pending intent for alarm manager
        final PendingIntent notifyPendingIntent = PendingIntent.getActivity
                (this,NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String toastMessage;
                if(isChecked){

                    //undo comment to make trigger 15 min (now should be instant) 
                    long triggerTime = SystemClock.elapsedRealtime();
                           // + AlarmManager.INTERVAL_FIFTEEN_MINUTES;

                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

                        //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                triggerTime, repeatInterval, notifyPendingIntent);

                        //Set the toast message for the "on" case
                        toastMessage = getString(R.string.alarm_on_toast);
                        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

                    } else {
                        //Cancel the alarm and notification if the alarm is turned off
                        alarmManager.cancel(notifyPendingIntent);
                        mNotificationManager.cancelAll();

                        //Set the toast message for the "off" case
                        toastMessage = getString(R.string.alarm_off_toast);
                    }

                    //Show a toast to say the alarm is turned on or off
//                    Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT)
//                            .show();
                }
            });


        }
    }