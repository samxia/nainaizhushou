package com.example.xyg.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

/**
 * Created by xyg on 2017-01-18.
 */

public class PowerChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(Intent.ACTION_BATTERY_CHANGED.equals(action))
        {
            int intLevel= intent.getIntExtra("level",0);
            int intScale=intent.getIntExtra("scale",100);

            // 是否在充电
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            boolean isFull=(status==BatteryManager.BATTERY_STATUS_FULL);

            if(!isCharging) {
                if (intLevel < 50) {
                    Toast.makeText(context, "该充电了" + "---" + intLevel, Toast.LENGTH_SHORT).show();
                    //SoundService.play(context,SoundService.SoundFileName_GaiChongDianLe,3);
                } else {
                    //Toast.makeText(context, intLevel + "---" + intScale, Toast.LENGTH_SHORT).show();
                }
            }
            else  if(isFull || intLevel==100)
            {
                Toast.makeText(context, "电充好了" + "---" + intLevel, Toast.LENGTH_SHORT).show();
                context.startService(new Intent(context,PowerService.class));
            }
    }
}}
