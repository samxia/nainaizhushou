package com.example.xyg.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class SoundService extends Service {
    private MediaPlayer myMediaPlayer;
    private AssetFileDescriptor fileDescriptor;
    private AssetManager assetManager;
    private static int playTimes;
    public static String lastSoundFileName;

    public final static String SoundFileName_ChongDianQiBaChuLaiLe="power_chongDianQiBaChuLaiLe.wma";
    public final static String SoundFileName_ChongDianQiChaHaoLe="power_chongDianQiChaHaoLe.wma";
    public final static String SoundFileName_DianChongHaoLe="power_dianChongHaoLe.wma";
    public final static String SoundFileName_DianHaiMeiChongHao="power_dianHaiMeiChongHao.wma";
    public final static String SoundFileName_GaiChongDianLe="power_gaiChongDianLe.wma";

    public SoundService() {
    }
    public void onCreate(){
        assetManager=this.getAssets();
        myMediaPlayer = new MediaPlayer();
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(playTimes<=1) {
                  return;
                }
                mp.start();
                playTimes--;
            }
        });
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        setToMaxVolum();

        String soundFileName=intent.getStringExtra("soundFileName");

        playSound(makeFullPathForFileName(soundFileName));
       // playSound("sounds/power_xyg/power_gaiChongDianLe.wma");

        return super.onStartCommand(intent,flags,startId);
    }
    private  String makeFullPathForFileName(String soundFileName)
    {
        lastSoundFileName=soundFileName;
        return "sounds/power_xyg/"+soundFileName;
    }
    private void playSound(String fileName)
    {
        myMediaPlayer.reset();
        try {
            // fileDescriptor = this.getAssets().openFd("sounds/power_xyg/power_gaiChongDianLe.wma");
            fileDescriptor = assetManager.openFd(fileName);
            myMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            myMediaPlayer.prepare();
            myMediaPlayer.start();

            fileDescriptor.close();
        } catch (IOException e) {
            Log.d("SoundService",e.getMessage());
        }
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
     static public void play(Context context,String soundfileName,int times)
    {
        playTimes=times;
        Intent intent=new Intent(context,SoundService.class);
        intent.putExtra("soundFileName",soundfileName);
        context.startService(intent);
    }
    static public void play(Context context,String soundfileName)
    {
        SoundService.play(context,soundfileName,1);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
