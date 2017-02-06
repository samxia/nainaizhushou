package com.example.xyg.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;


public class SoundIntentService extends IntentService {
    private MediaPlayer myMediaPlayer;
    private  AssetFileDescriptor fileDescriptor;

    public SoundIntentService() {
        super("SoundIntentService");
    }
    @Override
    public void onCreate() {
        super.onCreate();

        setToMaxVolum();


    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent,startId);

    }
    @Override
    protected void onHandleIntent(Intent intent) {
        playSound("sounds/power_xyg/power_gaiChongDianLe.wma");
        this.stopSelf();
    }

private void setToMaxVolum()
{
   /* 把音乐音量强制设置为最大音量
                            */
    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
    int maxVolume = mAudioManager
            .getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音
    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小
}
    private void playSound(String fileName)
    {
        myMediaPlayer = new MediaPlayer();
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
           // fileDescriptor = this.getAssets().openFd("sounds/power_xyg/power_gaiChongDianLe.wma");
            fileDescriptor = this.getAssets().openFd(fileName);
            myMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            myMediaPlayer.prepare();
            myMediaPlayer.start();
            fileDescriptor.close();
        } catch (IOException e) {
            Log.d("SoundIntentService",e.getMessage());
        }
    }
}
