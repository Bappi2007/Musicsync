package com.example.musicsync;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GuestActivity extends AppCompatActivity {
    private TextView tvAvailableSessions, tvSongNameGuest;
    private Button btnJoin, btnPlayGuest, btnPauseGuest;
    private SessionManager sessionManager;
    private MusicPlayerService musicPlayerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_guest);

        tvAvailableSessions = findViewById(R.id.tvAvailableSessions);
        tvSongNameGuest = findViewById(R.id.tvSongNameGuest);
        btnJoin = findViewById(R.id.btnJoin);
        btnPlayGuest = findViewById(R.id.btnPlayGuest);
        btnPauseGuest = findViewById(R.id.btnPauseGuest);

        musicPlayerService = new MusicPlayerService(this);

        sessionManager = new SessionManager(this, false, new SessionManager.SessionListener() {
            @Override
            public void onConnectedDevicesChanged(int count) {
                // Guests don't care
            }
        });

        sessionManager.discoverSessions(sessions -> {
            if (sessions.length > 0) {
                tvAvailableSessions.setText("Session found: " + sessions[0]);
                btnJoin.setEnabled(true);
            } else {
                tvAvailableSessions.setText("No sessions found");
                btnJoin.setEnabled(false);
            }
        });

        btnJoin.setOnClickListener(v -> {
            sessionManager.joinSession(() -> {
                btnPlayGuest.setEnabled(true);
                btnPauseGuest.setEnabled(true);
                tvSongNameGuest.setText("Waiting for song...");
            });
        });

        btnPlayGuest.setOnClickListener(v -> {
            sessionManager.requestPlay(() -> {
                musicPlayerService.play(sessionManager.getCurrentSongUri());
                tvSongNameGuest.setText(sessionManager.getCurrentSongName());
            });
        });
        btnPauseGuest.setOnClickListener(v -> {
            musicPlayerService.pause();
        });

        // SessionManager will also update song info when host changes song
        sessionManager.setOnSongChangedListener((uri, name) -> {
            tvSongNameGuest.setText(name);
            musicPlayerService.play(uri);
        });
    }
}