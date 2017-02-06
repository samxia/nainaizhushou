package com.example.xyg.myapplication;

import java.lang.reflect.Method;
import com.android.internal.telephony.ITelephony;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

public class AutoAnswerIntentService extends IntentService implements SensorEventListener {
	private SensorManager mSensorManager;
	private  Sensor mAccelerometer;
	private float mGravity = SensorManager.STANDARD_GRAVITY-0.8f;

	private TelephonyManager tm;
	private Context context;

	private boolean boolean_gravity_left_or_right=false;

	private AudioManager audioManager;

	public AutoAnswerIntentService() {
		super("AutoAnswerIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		 context = getBaseContext();

		/*try{
		 Thread.sleep(5000);   //answer the ringing call after 5 seconds
		 } catch (InterruptedException e) {
		
		 }*/
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING) {
			this.stopSelf();
			return;
		}

		/*try {
			answerPhoneAidl(context);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("AutoAnswer", "Error trying to answer using telephony service.  Falling back to headset.");
			answerPhoneHeadsethook(context);
		}
		
		return;
		*/
	}

	private void answerPhoneHeadsethook(Context context) {

		Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");

		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
	}

	@SuppressWarnings("unchecked")
	private void answerPhoneAidl(Context context) throws Exception {

		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		Class c = Class.forName(tm.getClass().getName());
		Method m = c.getDeclaredMethod("getITelephony");
		m.setAccessible(true);
		ITelephony telephonyService;
		telephonyService = (ITelephony) m.invoke(tm);

		telephonyService.answerRingingCall();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	}


	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		// Log.d("linc", "value size: " + sensorEvent.values.length);
		float xValue = sensorEvent.values[0];// Acceleration minus Gx on the x-axis
		float yValue = sensorEvent.values[1];//Acceleration minus Gy on the y-axis
		float zValue = sensorEvent.values[2];//Acceleration minus Gz on the z-axis
		//mTvInfo.setText("x轴： "+xValue+"  y轴： "+yValue+"  z轴： "+zValue);
		if(xValue > mGravity || xValue < -mGravity)
		{

			//	mTvInfo.append("\n重力指向设备左边或右边");
				if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING) {
					return;
				}
			//	try {
				//	answerPhoneAidl(context);
				//} catch (Exception e) {
				//	e.printStackTrace();
				//	Log.d("AutoAnswer", "Error trying to answer using telephony service.  Falling back to headset.");
					answerPhoneHeadsethook(context);
			        setSpeekModle(true);
			//	}
				 this.stopSelf();
		}


	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	public void setSpeekModle(boolean open){
		//audioManager.setMode(AudioManager.ROUTE_SPEAKER);
		int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
		audioManager.setMode(AudioManager.MODE_IN_CALL);

		if(!audioManager.isSpeakerphoneOn()&&true==open) {
			audioManager.setSpeakerphoneOn(true);
			audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
					audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
					AudioManager.STREAM_VOICE_CALL);
		}else if(audioManager.isSpeakerphoneOn()&&false==open){
			audioManager.setSpeakerphoneOn(false);
			audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,currVolume,
					AudioManager.STREAM_VOICE_CALL);
		}
	}

}
