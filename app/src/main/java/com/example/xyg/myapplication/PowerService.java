package com.example.xyg.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;


/**
 * Created by xyg on 2017-01-18.
 */

public class PowerService extends Service {
public static boolean isPowerConnected=true;


    public void onCreate(){

    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(!isPowerConnected)
        {
            return super.onStartCommand(intent, flags, startId);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                check();
                Log.d("PowerService", "executed at " + new Date().
                        toString());
            }
        }).start();



        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
      //  int anHour = 10* 1000; // 10秒
       // int anHour = 60* 1000; // 60秒
        int anHour = 300* 1000; // 10秒

        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, PowerServiceAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
    private void check()
    {
        Log.d("tag",String.valueOf(isPowerConnected));
        Looper.prepare();
        if(isPowerConnected)
        {

            Toast.makeText(getApplicationContext(), "service-电充好了,请拔充电器",Toast.LENGTH_SHORT).show();
            SoundService.play(this,SoundService.SoundFileName_DianChongHaoLe,2);
        }
        else{

            Toast.makeText(getApplicationContext(), "service-充电器已拔",Toast.LENGTH_SHORT).show();
            this.stopSelf();
        }
        Looper.loop();

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
