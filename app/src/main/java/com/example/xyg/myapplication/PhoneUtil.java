package com.example.xyg.myapplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;

public class PhoneUtil {

    public static String TAG = PhoneUtil.class.getSimpleName();

    /**
     * 挂断电话
     * @param context
     */
    public static void endCall(Context context) {
        try {
            Object telephonyObject = getTelephonyObject(context);
            if (null != telephonyObject) {
                Class telephonyClass = telephonyObject.getClass();

                Method endCallMethod = telephonyClass.getMethod("endCall");
                endCallMethod.setAccessible(true);

                endCallMethod.invoke(telephonyObject);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static Object getTelephonyObject(Context context) {
        Object telephonyObject = null;
        try {
            // 初始化iTelephony
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Will be used to invoke hidden methods with reflection
            // Get the current object implementing ITelephony interface
            Class telManager = telephonyManager.getClass();
            Method getITelephony = telManager.getDeclaredMethod("getITelephony");
            getITelephony.setAccessible(true);
            telephonyObject = getITelephony.invoke(telephonyManager);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return telephonyObject;
    }


    /**
     * 通过反射调用的方法，接听电话，该方法只在android 2.3之前的系统上有效。
     * @param context
     */
    private static void answerRingingCallWithReflect(Context context) {
        try {
            Object telephonyObject = getTelephonyObject(context);
            if (null != telephonyObject) {
                Class telephonyClass = telephonyObject.getClass();
                Method endCallMethod = telephonyClass.getMethod("answerRingingCall");
                endCallMethod.setAccessible(true);

                endCallMethod.invoke(telephonyObject);
                // ITelephony iTelephony = (ITelephony) telephonyObject;
                // iTelephony.answerRingingCall();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    /**
     * 伪造一个有线耳机插入，并按接听键的广播，让系统开始接听电话。
     * @param context
     */
    private static void answerRingingCallWithBroadcast(Context context){
        AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //判断是否插上了耳机
        boolean isWiredHeadsetOn = localAudioManager.isWiredHeadsetOn();
        if (!isWiredHeadsetOn) {
            Intent headsetPluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
            headsetPluggedIntent.putExtra("state", 1);
            headsetPluggedIntent.putExtra("microphone", 0);
            headsetPluggedIntent.putExtra("name", "");
            context.sendBroadcast(headsetPluggedIntent);

            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
            context.sendOrderedBroadcast(meidaButtonIntent, null);

            Intent headsetUnpluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
            headsetUnpluggedIntent.putExtra("state", 0);
            headsetUnpluggedIntent.putExtra("microphone", 0);
            headsetUnpluggedIntent.putExtra("name", "");
            context.sendBroadcast(headsetUnpluggedIntent);

        } else {
            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
            context.sendOrderedBroadcast(meidaButtonIntent, null);
        }
    }
private static void answerRingingCallForAndroid4Point1(Context context)
{
    //放开耳机按钮
    Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
    KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
    localIntent3.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent2);
    context.sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");

    //插耳机
    Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
    localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    localIntent1.putExtra("state", 1);
    localIntent1.putExtra("microphone", 1);
    localIntent1.putExtra("name", "Headset");
    context.sendOrderedBroadcast(localIntent1,"android.permission.CALL_PRIVILEGED");
    //按下耳机按钮
    Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
    KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
    localIntent2.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent1);
    context.sendOrderedBroadcast(localIntent2,"android.permission.CALL_PRIVILEGED");
    //放开耳机按钮
    localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
    localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
    localIntent3.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent2);
    context.sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");
    //拔出耳机
    Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
    localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    localIntent4.putExtra("state", 0);
    localIntent4.putExtra("microphone", 1);
    localIntent4.putExtra("name", "Headset");
    context.sendOrderedBroadcast(localIntent4,"android.permission.CALL_PRIVILEGED");
}
    /**
     * 接听电话
     * @param context
     */
    public static void answerRingingCall(Context context) {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {  //2.3或2.3以上系统
            answerRingingCallWithBroadcast(context);

        }else*/
         if(Integer.valueOf(Build.VERSION.SDK_INT)==16)//Android 4.1, xiaomi 1
        {
            answerRingingCallForAndroid4Point1(context);
        }
        else {
            answerRingingCallWithReflect(context);
        }
    }

    /**
     * 打电话
     * @param context
     * @param phoneNumber
     */
    public static void callPhone(Context context, String phoneNumber) {
        if(!TextUtils.isEmpty(phoneNumber)){
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phoneNumber));
                //context.startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拨电话
     * @param context
     * @param phoneNumber
     */
    public static void dialPhone(Context context, String phoneNumber){
        if(!TextUtils.isEmpty(phoneNumber)){
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phoneNumber));
                context.startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}