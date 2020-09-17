package com.example.dilemma_mobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dilemma_mobile.R;
import com.example.dilemma_mobile.model.Location;
import com.example.dilemma_mobile.service.IndoorNavigationService;

public class IndoorNavigationActivity extends AppCompatActivity {
    IndoorNavigationService indoorNavigationService;
    private Button btnSkip;
    private TextView textViewDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_navigation);
        btnSkip = findViewById(R.id.btnSkip);
        textViewDestination= findViewById(R.id.textdestination);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipNextDestination();
                textViewDestination.setText(indoorNavigationService.getTarget().getName());

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to IndoorNavigationService
        Intent intent = new Intent(this, IndoorNavigationService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // IndoorNavigationService instance
            IndoorNavigationService.LocalBinder binder = (IndoorNavigationService.LocalBinder) service;
            indoorNavigationService = binder.getService();
            textViewDestination.setText(indoorNavigationService.getTarget().getName());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };
    public void skipNextDestination(){
        indoorNavigationService.skip();
        Location currentLocation = indoorNavigationService.getCurrentLocation();
        indoorNavigationService.getUserDirection(currentLocation);
    }
}
