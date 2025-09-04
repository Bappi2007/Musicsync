package com.example.musicsync;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        Button btnHost = findViewById(R.id.btnHost);
        Button btnGuest = findViewById(R.id.btnGuest);

        btnHost.setOnClickListener(v -> {
            startActivity(new Intent(this, HostActivity.class));
        });
        btnGuest.setOnClickListener(v -> {
            startActivity(new Intent(this, GuestActivity.class));
        });
    }
}