package com.example.xyg.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by xyg on 2017-01-18.
 */

public class PowerServiceAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PowerService.class);
        context.startService(i);
    }
}
