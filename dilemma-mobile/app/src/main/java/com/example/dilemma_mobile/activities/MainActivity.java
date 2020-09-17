package com.example.dilemma_mobile.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dilemma_mobile.R;
import com.example.dilemma_mobile.consumeApi.CustomerServiceAPI;
import com.example.dilemma_mobile.model.Store;
import com.example.dilemma_mobile.service.MyBeaconService;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private Button btnIndoorNavigationActivity;
    private WebSocketClient webSocketClient;
    private Context context = this;
    private Button btnJourney;
    private BeaconManager beaconManager;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 2;
    protected static final String TAG = "RangingActivity";

    BeaconConsumer beaconConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createWebSocketClient();
        // permission pour capter les beacons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            }
        } else {
            //startService(new Intent(getApplicationContext(), LocationService.class));
        }
        btnIndoorNavigationActivity = findViewById(R.id.btnIndoorNavigationActivity);
        //startService(new Intent(getApplicationContext(), IndoorNavigationService.class));
        btnJourney= findViewById(R.id.parcours);
        btnJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IndoorMapActivity.class);
                startActivity(intent);
            }
        });


        btnIndoorNavigationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IndoorNavigationActivity.class);
                startActivity(intent);
            }
        });

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconConsumer= new MyBeaconService(beaconManager,context);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(beaconConsumer);


    }


    public void getJourney(CustomerServiceAPI customerServiceAPI) {
        int id = 0;
        Call<List<Store>> call = customerServiceAPI.getJourney(id);
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                // retrieve journey from the api

            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                // retrieve journey from sqlite
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    System.out.println("Permission granted");
                } else {

                    System.out.println("Permission denied");
                    Toast.makeText(this, "You didn't allow the application to use your location, we can't locate you", Toast.LENGTH_LONG);
                }
                return;
            }
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    //1 - Starting a new connection to the localhost “ws://10.0.2.2:8080/websocket”.
    //2 - Sending a message to the server once a connection is opened.
    //3 - Displaying the messages sent from the server on the app.
    //4 - Setting timeouts and automatic reconnection.
//    private void createWebSocketClient() {
//        URI uri;
//        try {
//            // Connect to local host
//            uri = new URI("ws://172.31.249.11:8035/websocket");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        webSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//                Log.i("WebSocket", "Session is starting");
//                webSocketClient.send("Hello World!");
//                webSocketClient.send("0 Bienvenue");
//            }
//
//            @Override
//            public void onTextReceived(String s) {
//                Log.i("WebSocket", "Message received");
//                Notification notification = new Notification();
//                final String message = s;
//                Moshi moshi = new Moshi.Builder().build();
//                JsonAdapter<Notification> jsonAdapter = moshi.adapter(Notification.class);
//                try {
//                    notification = jsonAdapter.fromJson(s);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.notification_icon)
//                        .setContentTitle(notification.getName())
//                        .setContentText(notification.getBody())
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//            }
//
//            @Override
//            public void onBinaryReceived(byte[] data) {
//            }
//
//            @Override
//            public void onPingReceived(byte[] data) {
//            }
//
//            @Override
//            public void onPongReceived(byte[] data) {
//            }
//
//            @Override
//            public void onException(Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public void onCloseReceived() {
//                Log.i("WebSocket", "Closed ");
//                System.out.println("onCloseReceived");
//            }
//        };
//        webSocketClient.setConnectTimeout(10000);
//        webSocketClient.setReadTimeout(60000);
//        webSocketClient.enableAutomaticReconnection(5000);
//        webSocketClient.connect();
//    }
//
//    public void sendMessage(String message) {
//        webSocketClient.send(message);
//    }
}
