package com.example.musicsync;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.database.Cursor;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HostActivity extends AppCompatActivity {
    private static final int PICK_SONG_REQUEST = 1;
    private Uri selectedSongUri;
    private TextView tvSongName, tvConnectedDevices;
    private Button btnPickSong, btnPlay, btnPause, btnSkip;

    private SessionManager sessionManager;
    private MusicPlayerService musicPlayerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_host);

        tvSongName = findViewById(R.id.tvSongName);
        tvConnectedDevices = findViewById(R.id.tvConnectedDevices);
        btnPickSong = findViewById(R.id.btnPickSong);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnSkip = findViewById(R.id.btnSkip);

        sessionManager = new SessionManager(this, true, new SessionManager.SessionListener() {
            @Override
            public void onConnectedDevicesChanged(int count) {
                tvConnectedDevices.setText("Connected Devices: " + count);
            }
        });
        musicPlayerService = new MusicPlayerService(this);

        btnPickSong.setOnClickListener(v -> pickSongFromDevice());
        btnPlay.setOnClickListener(v -> playSong());
        btnPause.setOnClickListener(v -> pauseSong());
        btnSkip.setOnClickListener(v -> skipSong());

        sessionManager.startHostingSession();
    }

    private void pickSongFromDevice() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_SONG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SONG_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedSongUri = data.getData();
            String songName = getFileName(selectedSongUri);
            tvSongName.setText(songName);

            sessionManager.broadcastSongSelection(selectedSongUri, songName);
        }
    }

    private String getFileName(Uri uri) {
        String result = "Unknown";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            result = cursor.getString(nameIndex);
            cursor.close();
        }
        return result;
    }

    private void playSong() {
        if (selectedSongUri != null) {
            musicPlayerService.play(selectedSongUri);
            sessionManager.broadcastPlayCommand();
        }
    }

    private void pauseSong() {
        musicPlayerService.pause();
        sessionManager.broadcastPauseCommand();
    }

    private void skipSong() {
        musicPlayerService.skip();
        sessionManager.broadcastSkipCommand();
    }
}