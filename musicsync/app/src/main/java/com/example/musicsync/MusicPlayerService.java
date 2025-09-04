package com.example.musicsync;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class MusicPlayerService {
    private Context context;
    private MediaPlayer mediaPlayer;

    public MusicPlayerService(Context context) {
        this.context = context;
    }

    public void play(Uri songUri) {
        stop();
        mediaPlayer = MediaPlayer.create(context, songUri);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void skip() {
        stop();
        // TODO: Implement skip logic if you add playlists
    }
}