package com.example.dilemma_mobile.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.dilemma_mobile.model.Location;
import com.example.dilemma_mobile.model.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IndoorNavigationService extends Service {
    private List<Store> storeList;
    private Store target;
    private Location currentLocation;
    private double distanceX;
    private double distanceY;
    private final IBinder binder = new LocalBinder();

    public IndoorNavigationService() {

    }
    public class LocalBinder extends Binder {
        public IndoorNavigationService getService() {
            return IndoorNavigationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        storeList= new ArrayList<Store>();
        Store store = new Store("1", "DARTY", "video games, books, ...", 30d, 30d);
        Store store2 = new Store("2","FNAC","video games, books, ...",35d,30d);
        this.storeList.add(store);
        this.storeList.add(store2);
        this.currentLocation = new Location(new Random().nextDouble(),new Random().nextDouble(),0);
        //this.currentLocation = new Location(30d,30d,0);
        this.target = storeList.get(0);// to verify
        this.distanceX = target.getLatitude() - currentLocation.getX();
        this.distanceY = target.getLongitude() - currentLocation.getY();
        guideUser();

        return START_STICKY ;
    }
    public void guideUser() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Location l = new Location(new Random().nextDouble(),new Random().nextDouble(),0);
                //Location l = new Location(30d,31d,0);
                if(verifyUserChangeLocation(l)){
                    String message = getUserDirection(l);
                    NotificationService notificationService = new NotificationService();
                    notificationService.createNotification("JOURNEY",message,getApplicationContext(),getSystemService(Context.NOTIFICATION_SERVICE));
                }

                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);

    }

    /**
     * This method verify if the Location have change
     *
     * @param newLocation
     * @return
     */
    public boolean verifyUserChangeLocation(Location newLocation) {

        if (newLocation.getX() != currentLocation.getX() || newLocation.getY() != currentLocation.getY()) {
            Log.d("verifyUserChange","User location changed");
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method is used to get the user next direction (left,right,straight ahead ,behind)
     *
     * @param newLocation the new location received
     * @return
     */
    public String getUserDirection(Location newLocation) {
        Location oldLocation = currentLocation;
        currentLocation = newLocation;
        double convergeX = target.getLatitude() - newLocation.getX();
        double convergeY = target.getLongitude() - newLocation.getY();
        String message = null;
        if (convergeX == 0.0 && convergeY == 0.0) {
            System.out.println("You arrived to destination");
            message = "You arrived to destination";
            // remove store from the list and set the new destination
        } else if (convergeX < distanceX || convergeY < distanceY) {
            System.out.println("You're going the right way");
            if (currentLocation.getX() != oldLocation.getX()) {
                message = getDirectionX(currentLocation);
            }
            if (currentLocation.getY() != oldLocation.getY()) {
                message = getDirectionY(currentLocation);
            }
        } else if (convergeX > distanceX || convergeY > distanceY) {
            System.out.println("You're going the wrong way");
            Log.i("guide User", "You're going the wrong way");

            message = "You're going the wrong way";
        }
        updateGPSCoordinates(currentLocation);

        return message;
    }

    public void updateGPSCoordinates(Location gpsLastPosition) {
        setDistanceX(target.getLatitude() - gpsLastPosition.getX());
        setDistanceY(target.getLongitude() - gpsLastPosition.getY());
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public Store getTarget() {
        return target;
    }

    public void setTarget(Store target) {
        this.target = target;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double getDistanceX() {
        return distanceX;
    }

    public void setDistanceX(double distanceX) {
        this.distanceX = distanceX;
    }

    public double getDistanceY() {
        return distanceY;
    }

    public void setDistanceY(double distanceY) {
        this.distanceY = distanceY;
    }

    public String getDirectionX(Location newLocation) {
        double X = target.getLatitude() - newLocation.getX();
        if (X < 0) {
            Log.i("direction x", "to the left");

            return "to the left";
        } else {
            Log.i("direction x", "to the right");

            return "to the right";
        }

    }

    public String getDirectionY(Location newLocation) {
        double Y = target.getLongitude() - newLocation.getY();
        if (Y < 0) {
            Log.i("direction y", "go behind");
            return "go behind";
        } else {
            Log.i("direction y", "go straight ahead");

            return "go straight ahead";
        }
    }

    public void skip() {
        this.storeList.remove(0);
        if(!storeList.isEmpty()) {
            this.target = storeList.get(0);
        }
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
}
