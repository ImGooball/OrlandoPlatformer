package com.company.myplatformerfinal;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public void playMusic(Context context, int musicResourceId) {
        stopMusic();  // Stop currently playing music if any
        mediaPlayer = MediaPlayer.create(context, musicResourceId);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);  // Set looping
            mediaPlayer.start();  // Start playback
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();  // Release resources
            mediaPlayer = null;
        }
    }
}