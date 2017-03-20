package com.farbox.androidbyeleven.Controller.Sound;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Utils.Global;

/**
 * describe:控制音效
 * time: 2017/3/19 9:53
 * email: tom.work@foxmail.com
 */
public class Sound {
    private SoundPool soundPool = null;
    private int soundID;

    public Sound() {
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder spb = new SoundPool.Builder();
            AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder();
            audioAttributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
            AudioAttributes aa = audioAttributesBuilder.build();
            this.soundPool = spb.setMaxStreams(3).setAudioAttributes(aa).build();
        } else {
            this.soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
        }

        this.soundID = this.soundPool.load(Global.applicationContext, R.raw.move, 1);
    }

    public void play() {
        this.soundPool.play(this.soundID, 1, 1, 0, 0, 1);
    }


}
