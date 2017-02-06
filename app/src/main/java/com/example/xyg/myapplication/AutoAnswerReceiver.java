package com.example.xyg.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;



public class AutoAnswerReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {

		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (am.getMode() == AudioManager.MODE_IN_CALL) {
				return;
			}
			context.startService(new Intent(context, AutoAnswerIntentService.class));
		}		
	}
}
