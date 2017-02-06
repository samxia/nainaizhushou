package com.example.xyg.myapplication;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class PowerChangedRegisterService extends Service {
    private PowerChangedReceiver powerChangedReceiver = new PowerChangedReceiver();
    public PowerChangedRegisterService() {
    }

    @Override
    public void onCreate() {
        registerReceiver(powerChangedReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(powerChangedReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
