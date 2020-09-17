package com.example.dilemma_mobile.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.rtt.RangingRequest;
import android.net.wifi.rtt.RangingResult;
import android.net.wifi.rtt.RangingResultCallback;
import android.net.wifi.rtt.WifiRttManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dilemma_mobile.consumeApi.IndoorLocationServiceAPI;
import com.example.dilemma_mobile.consumeApi.ServiceGeneratorGeolocation;
import com.example.dilemma_mobile.embedded.MyDatabaseHelper;
import com.example.dilemma_mobile.model.AccessPoint;
import com.example.dilemma_mobile.model.Location;
import com.example.dilemma_mobile.model.MyExecutor;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    private WifiManager wifiManager;
    private WifiRttManager wifiRttManager;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;
    private List<ScanResult> results;
    private MyExecutor executor;
    private AccessPoint ap1 = new AccessPoint("6c:f3:7f:ef:6f:30",27.05,3.05,1);
    private AccessPoint ap2 = new AccessPoint("6c:f3:7f:ef:70:40",27.05,8.95,1);
    private AccessPoint ap3 = new AccessPoint("6c:f3:7f:eb:b0:90",14.1,29.5,1);
    private AccessPoint ap4 = new AccessPoint("6c:f3:7f:eb:bd:70",15.72,20.15,1);
    private AccessPoint ap5 = new AccessPoint("6c:f3:7f:eb:da:30",7.38,10.15,1);
    private double distanceFromAP1;
    private double distanceFromAP2;
    private double distanceFromAP3;
    private double distanceFromAP4;
    private double distanceFromAP5;
    private ArrayList<Integer> listRssiAP1 = new ArrayList<>();
    private ArrayList<Integer> listFreqAP1 = new ArrayList<>();
    private ArrayList<Integer> listRssiAP2 = new ArrayList<>();
    private ArrayList<Integer> listFreqAP2 = new ArrayList<>();
    private ArrayList<Integer> listRssiAP3 = new ArrayList<>();
    private ArrayList<Integer> listFreqAP3 = new ArrayList<>();
    private ArrayList<Integer> listRssiAP4 = new ArrayList<>();
    private ArrayList<Integer> listFreqAP4 = new ArrayList<>();
    private ArrayList<Integer> listRssiAP5 = new ArrayList<>();
    private ArrayList<Integer> listFreqAP5 = new ArrayList<>();
    private List<AccessPoint> foundAccessPoints = new ArrayList<>();
    private Location location = new Location(12.9,0.8,0);
    private final IBinder binder = new MyBinder();
    final IndoorLocationServiceAPI service = ServiceGeneratorGeolocation.createService(IndoorLocationServiceAPI.class);
    private double[][] references = new double[][] {
                                                    { 1.3,0.8},{8.2,0.8},{17.8,0.8},{12.9,0.8},{21.8,0.8},
                                                    {26.0,0.8},{35.2,8.3},{26.0,14.9},{23.0,12.7},{23.0,18.2},
                                                    {18.2,19.7},{18.2,28.6},{17.8,33.9},{12.8,23.2},{8.5,33.9},
                                                    {8.3,24.7},{12.8,18.2},{8.5,19.1},{8.5,15.7},{12.8,14.2},
                                                    {11.6,8.9},{1.3,19.3},{5.7,16.4},{35.7,33.9},{12.8,8.3},{5.95,9.6}
                                                    };

    public class MyBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocationService", "onStartCommand: Geolocation service started");
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        myDatabaseHelper = new MyDatabaseHelper(this);
        db = myDatabaseHelper.getWritableDatabase();

        wifiManager.startScan();

        locate();

        return START_STICKY;
    }

    public void locate() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                registerReceiver(broadcastReceiver, intentFilter);
                for(int i = 0 ; i < 10 ; i++) {
                    wifiManager.disconnect();
                    wifiManager.reconnect();
                    wifiManager.startScan();
                    results = wifiManager.getScanResults();
                    storeScanResults(results);
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("MissingPermission") //To suppress the warning about the check of ACCESS FINE LOCATION because already done in onCreate
    @TargetApi(28)
    public void calculateWithRtt() {
        Log.d("CalculateWithRtt", "Calculating position with Rtt");
        wifiRttManager = (WifiRttManager) getApplicationContext().getSystemService(Context.WIFI_RTT_RANGING_SERVICE);
        if(wifiRttManager.isAvailable()) {
            RangingRequest.Builder builder = new RangingRequest.Builder();
            for (ScanResult scanResult : results) {
                if(scanResult.BSSID.equals(ap1.getMac())) {
                    System.out.println("MAC ADDRESS : " + scanResult.BSSID);
                    builder.addAccessPoint(scanResult);
                }
            }
            RangingRequest request = builder.build();
            RangingResultCallback callback =  new RangingResultCallback() {
                @Override
                public void onRangingFailure(int code) {
                    System.out.println("RangingFailure");
                }

                @Override
                public void onRangingResults(@NonNull List<RangingResult> results) {
                    for(RangingResult rr : results) {
                        System.out.println("SSID : " + rr.getMacAddress());
                    }
                }
            };
            executor = new MyExecutor();
            wifiRttManager.startRanging(request, executor, callback);

            //Do the trilateration algorithm
        }
    }

    public void calculateWithRSSI() {
        Log.d("CalculateWithRSSI", "Calculate position with RSSI");
        for(ScanResult scanResult : results)  {
            wifiManager.startScan();
            if(scanResult.BSSID.equals(ap1.getMac())) {
                Log.d("MAC", scanResult.BSSID);
                double avgDistance  = 0;
                double  temp;
                for(int i = 0 ; i < listRssiAP1.size() ; i++) {
                    temp = calculateDistanceFromRSSI(listRssiAP1.get(i), listFreqAP1.get(i));
                    System.out.println("TEMP = " + temp);
                    avgDistance += temp;
                }
                avgDistance = avgDistance / listRssiAP1.size();
                distanceFromAP1 = avgDistance * 0.9;
                ap1.setDistanceFromUser(distanceFromAP1);
                Log.d("AP1", "Distance : " + avgDistance);
            }
            if(scanResult.BSSID.equals(ap2.getMac())) {
                Log.d("MAC", scanResult.BSSID);
                double avgDistance  = 0;
                double  temp;
                for(int i = 0 ; i < listRssiAP2.size() ; i++) {
                    temp = calculateDistanceFromRSSI(listRssiAP2.get(i), listFreqAP2.get(i));
                    System.out.println("TEMP = " + temp);
                    avgDistance += temp;
                }
                avgDistance = avgDistance / listRssiAP2.size();
                distanceFromAP2 = avgDistance * 0.9;
                ap2.setDistanceFromUser(distanceFromAP2);
                Log.d("AP2", "Distance : " + avgDistance);
            }
            if(scanResult.BSSID.equals(ap3.getMac())) {
                Log.d("MAC", scanResult.BSSID);
                double avgDistance  = 0;
                double  temp;
                for(int i = 0 ; i < listRssiAP3.size() ; i++) {
                    temp = calculateDistanceFromRSSI(listRssiAP3.get(i), listFreqAP3.get(i));
                    System.out.println("TEMP = " + temp);
                    avgDistance += temp;
                }
                avgDistance = avgDistance / listRssiAP3.size();
                distanceFromAP3 = avgDistance * 0.9;
                ap3.setDistanceFromUser(distanceFromAP3);
                Log.d("AP3", "Distance : " + avgDistance);
            }
            if(scanResult.BSSID.equals(ap4.getMac())) {
                Log.d("MAC", scanResult.BSSID);
                double avgDistance  = 0;
                double  temp;
                for(int i = 0 ; i < listRssiAP4.size() ; i++) {
                    temp = calculateDistanceFromRSSI(listRssiAP4.get(i), listFreqAP4.get(i));
                    System.out.println("TEMP = " + temp);
                    avgDistance += temp;
                }
                avgDistance = avgDistance / listRssiAP4.size();
                distanceFromAP4 = avgDistance * 0.9;
                ap4.setDistanceFromUser(distanceFromAP4);
                Log.d("AP4", "Distance : " + avgDistance);
            }
            if(scanResult.BSSID.equals(ap5.getMac())) {
                Log.d("MAC", scanResult.BSSID);
                double avgDistance  = 0;
                double  temp;
                for(int i = 0 ; i < listRssiAP5.size() ; i++) {
                    temp = calculateDistanceFromRSSI(listRssiAP5.get(i), listFreqAP5.get(i));
                    System.out.println("TEMP = " + temp);
                    avgDistance += temp;
                }
                avgDistance = avgDistance / listRssiAP5.size();
                distanceFromAP5 = avgDistance * 0.9;
                ap5.setDistanceFromUser(distanceFromAP5);
                Log.d("AP4", "Distance : " + avgDistance);
            }
        }
        listRssiAP1.clear();
        listRssiAP2.clear();
        listRssiAP3.clear();
        listRssiAP4.clear();
        listRssiAP5.clear();
        listFreqAP1.clear();
        listFreqAP2.clear();
        listFreqAP3.clear();
        listFreqAP4.clear();
        listFreqAP5.clear();
        location = getLocation();
        wifiManager.startScan();
        Log.d("Location", "Location of the guy : (" + location.getX() + "," + location.getY() + "," + location.getFloor() + ")");
        Call<Location> call = service.addLocation(location);
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Log.d("onResponse", "POST : geolocation added to distant database.");
                myDatabaseHelper.addGeolocation(location,db);
                postAllDataFromSQLite();
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                //Store in SQLITE then (maybe in locate() function)
                Log.d("onFailure", "POST : geolocation added to SQLite");
                myDatabaseHelper.addGeolocation(location,db);
            }
        });
    }

    public void postAllDataFromSQLite() {
        List<Location> listAll = myDatabaseHelper.getAll(db);
        if(listAll.size() > 0) {
            for(Location geo : listAll) {
                Call<Location> internCall = service.addLocation(geo);
                internCall.enqueue(new Callback<Location>() {
                    @Override
                    public void onResponse(Call<Location> call, Response<Location> response) {
                        Log.d("onResponseIntern", "POST : geolocation from SQLite added to distant database.");
                    }

                    @Override
                    public void onFailure(Call<Location> call, Throwable t) {
                        Log.d("onFailureIntern", "POST : geolocation from SQLite not added to distant database.");
                    }
                });
            }
            myDatabaseHelper.deleteAll(db);
        }
    }

    public double calculateDistanceFromRSSI(double RSSI, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(RSSI)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public void storeScanResults(List<ScanResult> listResults) {
        for(ScanResult scanResult : listResults) {
            if (scanResult.BSSID.equals(ap1.getMac())) {
                listRssiAP1.add(scanResult.level);
                listFreqAP1.add(scanResult.frequency);
                foundAccessPoints.add(ap1);
            }
            if (scanResult.BSSID.equals(ap2.getMac())) {
                listRssiAP2.add(scanResult.level);
                listFreqAP2.add(scanResult.frequency);
                foundAccessPoints.add(ap2);
            }
            if (scanResult.BSSID.equals(ap3.getMac())) {
                listRssiAP3.add(scanResult.level);
                listFreqAP3.add(scanResult.frequency);
                foundAccessPoints.add(ap3);
            }
            if (scanResult.BSSID.equals(ap4.getMac())) {
                listRssiAP4.add(scanResult.level);
                listFreqAP4.add(scanResult.frequency);
                foundAccessPoints.add(ap4);
            }
            if (scanResult.BSSID.equals(ap5.getMac())) {
                listRssiAP5.add(scanResult.level);
                listFreqAP5.add(scanResult.frequency);
                foundAccessPoints.add(ap5);
            }
        }
    }

    public Location getLocation() {
        //double[][] positions = new double[][] { {ap1.getX(),ap1.getY()}, /*{ap2.getX(),ap2.getY()}, */{ap3.getX(),ap3.getY()}, {ap4.getX(),ap4.getY()}/*, {ap5.getX(),ap5.getY()}*/};
        //double[] distances = new double[] {distanceFromAP1, /*distanceFromAP2, */distanceFromAP3, distanceFromAP4/*, distanceFromAP5*/};
        int size = foundAccessPoints.size();
        double[][] positions = new double[size][2];
        double[] distances = new double[size];

        for(int i = 0 ; i < size ; i++) {
            positions[i][0] = foundAccessPoints.get(i).getX();
            positions[i][1] = foundAccessPoints.get(i).getY();
            distances[i] = foundAccessPoints.get(i).getDistanceFromUser();
        }


        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        double[] centroid = optimum.getPoint().toArray();

        Location geoLoc = new Location(centroid[0], centroid[1], 0);
        Log.d("POSITION", "LOCATION : (" + geoLoc.getX() + "," + geoLoc.getY() + "," + geoLoc.getFloor() + ")");

        double nearestReferencePointDistance = 200;
        int nearestPointIndex = 0;

        for(int i = 0 ; i < references.length ; i++) {
            double distance = Math.sqrt(Math.pow(geoLoc.getX() - references[i][0], 2) + Math.pow(geoLoc.getY() - references[i][1], 2));
            if(distance < nearestReferencePointDistance) {
                nearestReferencePointDistance = distance;
                nearestPointIndex = i;
            }
        }
        Log.d("GetLocation", "Nearest reference point (index = " + nearestPointIndex + ") : (" + references[nearestPointIndex][0] + "," + references[nearestPointIndex][1] + ")");
        geoLoc.setX(references[nearestPointIndex][0]);
        geoLoc.setY(references[nearestPointIndex][1]);
        return geoLoc;
    }

    public Location getLoc() {
        return location;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("OnReceive", "onReceive: Dans la methode");
            results = wifiManager.getScanResults();
            if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_RTT)) {
                calculateWithRtt();
            }
            else {
                calculateWithRSSI();
            }
            unregisterReceiver(this);
        }
    };

    public double[][] getReferences() {
        return references;
    }
}
