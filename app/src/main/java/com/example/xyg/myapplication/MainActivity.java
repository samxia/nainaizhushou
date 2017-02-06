package com.example.xyg.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    //  private float mGravity = SensorManager.STANDARD_GRAVITY-0.8f;

    public boolean boolean_CALL_STATE_RINGING = false;
    TextView show;
    Vibrator vibrator01;
    TextView mTvInfo;

    public MainActivity() {

    }

    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();

    }

    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvInfo =(TextView) findViewById(R.id.mTvInfo);
        show = (TextView) findViewById(R.id.show_textView);

        vibrator01=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        Button button_on = (Button) findViewById(R.id.on_button);
        button_on.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                         show.setText(String.valueOf(Build.VERSION.SDK_INT) );
                        vibrator01.vibrate(new long[]{100,10,100,500},-1);
                       Toast.makeText(MainActivity.this, getString(R.string.str_ok),Toast.LENGTH_SHORT).show();

                      //  Log.d("main",String.valueOf(PowerService.isPowerConnected));
                      /*  Intent intent=new Intent(MainActivity.this,SoundService.class);
                        intent.putExtra("soundFileName",SoundService.SoundFileName_ChongDianQiBaChuLaiLe);
                        startService(intent);
                        */
                        SoundService.play(MainActivity.this,SoundService.SoundFileName_DianChongHaoLe);
                    }

        });


        Intent intent=new Intent(MainActivity.this,PowerChangedRegisterService.class);
        startService(intent);

      //  mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
      //  mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }


     /*@Override
   public void onSensorChanged(SensorEvent sensorEvent) {
       // Log.d("linc", "value size: " + sensorEvent.values.length);
        float xValue = sensorEvent.values[0];// Acceleration minus Gx on the x-axis
        float yValue = sensorEvent.values[1];//Acceleration minus Gy on the y-axis
        float zValue = sensorEvent.values[2];//Acceleration minus Gz on the z-axis
        mTvInfo.setText("x轴： "+xValue+"  y轴： "+yValue+"  z轴： "+zValue);
        if(xValue > mGravity || xValue < -mGravity)
        {

            mTvInfo.append("\n重力指向设备左边或右边");
            boolean_gravity_left_or_right=true;

        }
        else{
            boolean_gravity_left_or_right=false;
        }

       /* if(xValue > mGravity) {
            mTvInfo.append("\n重力指向设备左边");
        } else if(xValue < -mGravity) {
            mTvInfo.append("\n重力指向设备右边");
        }
        else if(yValue > mGravity) {
            mTvInfo.append("\n重力指向设备下边");
        } else if(yValue < -mGravity) {
            mTvInfo.append("\n重力指向设备上边");
        } else if(zValue > mGravity) {
            mTvInfo.append("\n屏幕朝上");
        } else if(zValue < -mGravity) {
            mTvInfo.append("\n屏幕朝下");
        }
        */

/*    }*/

  /*  @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
*/

}
