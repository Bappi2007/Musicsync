package com.example.musicsync;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

// Stubs for Wi-Fi Direct (to be implemented for real sync)
public class SessionManager {
    private Context context;
    private boolean isHost;
    private SessionListener listener;
    private String[] sessions = {"Host's Session"};
    private Uri currentSongUri;
    private String currentSongName;
    private OnSongChangedListener songChangedListener;

    public interface SessionListener {
        void onConnectedDevicesChanged(int count);
    }

    public interface SessionDiscoveryCallback {
        void onSessionsFound(String[] sessions);
    }

    public interface OnSongChangedListener {
        void onSongChanged(Uri uri, String name);
    }

    public SessionManager(Context context, boolean isHost, SessionListener listener) {
        this.context = context;
        this.isHost = isHost;
        this.listener = listener;
    }

    public void startHostingSession() {
        // TODO: Implement Wi-Fi Direct host session
        Toast.makeText(context, "Session started. Waiting for guests...", Toast.LENGTH_LONG).show();
        listener.onConnectedDevicesChanged(0);
    }

    public void broadcastSongSelection(Uri songUri, String songName) {
        currentSongUri = songUri;
        currentSongName = songName;
        // TODO: Send song info to connected devices; implement streaming
        if (songChangedListener != null) songChangedListener.onSongChanged(songUri, songName);
    }

    public void broadcastPlayCommand() {
        // TODO: Send play signal (with timestamp for sync)
    }

    public void broadcastPauseCommand() {
        // TODO: Send pause signal
    }

    public void broadcastSkipCommand() {
        // TODO: Send skip signal
    }

    public void discoverSessions(SessionDiscoveryCallback callback) {
        // TODO: Use Wi-Fi Direct to discover host sessions
        callback.onSessionsFound(sessions);
    }

    public void joinSession(Runnable onJoined) {
        // TODO: Wi-Fi Direct connection logic
        Toast.makeText(context, "Joined session!", Toast.LENGTH_SHORT).show();
        if (onJoined != null) onJoined.run();
    }

    public void requestPlay(Runnable onPlayed) {
        // TODO: Request play from host; stubbed for demo
        if (onPlayed != null) onPlayed.run();
    }

    public Uri getCurrentSongUri() {
        return currentSongUri;
    }

    public String getCurrentSongName() {
        return currentSongName;
    }

    public void setOnSongChangedListener(OnSongChangedListener listener) {
        this.songChangedListener = listener;
    }
}