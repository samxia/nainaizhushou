package com.example.xyg.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

/**
 * Created by xyg on 2017-01-18.
 */

public class PowerReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(Intent.ACTION_POWER_CONNECTED.equals(action)){
            PowerService.isPowerConnected=true;
            Toast.makeText(context, "充电器插好了" , Toast.LENGTH_SHORT).show();
            SoundService.play(context,SoundService.SoundFileName_ChongDianQiChaHaoLe,2);
        }else if(Intent.ACTION_POWER_DISCONNECTED.equals(action))
        {
            PowerService.isPowerConnected=false;
            Toast.makeText(context, "充电器拔出来了" , Toast.LENGTH_SHORT).show();
            SoundService.play(context,SoundService.SoundFileName_ChongDianQiBaChuLaiLe,2);
        }
        else if(Intent.ACTION_BATTERY_OKAY.equals(action))
        {
            context.startService(new Intent(context,PowerService.class));
           // Toast.makeText(context, "okay-电充好了" , Toast.LENGTH_SHORT).show();
        }

    }
}
