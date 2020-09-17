package com.example.dilemma_mobile.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.dilemma_mobile.consumeApi.FrequentationServiceAPI;
import com.example.dilemma_mobile.consumeApi.ServiceGeneratorFrequentation;
import com.example.dilemma_mobile.embedded.DatabaseAccess;
import com.example.dilemma_mobile.model.Frequentation;
import com.example.dilemma_mobile.model.Store;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class MyBeaconService implements BeaconConsumer {

    private BeaconManager beaconManager;
    private double distanceEntree = 2;
    private String state = "Sortie";
    private FrequentationServiceAPI service;
    private Context context;


    public MyBeaconService(BeaconManager beaconManager,Context context) {
        this.beaconManager = beaconManager;
        this.service = ServiceGeneratorFrequentation.createService(FrequentationServiceAPI.class);
        this.context=context;
    }

    public void setDistanceEntree(double distanceEntree) {
        this.distanceEntree = distanceEntree;
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                    if (checkDistancetoStore(beacons.iterator().next().getDistance()) == true && state != "Entree") {
                        System.out.println("client entree");
                        String idClient = "1";
                        state = "Entree";
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                        databaseAccess.openDatabse();
                        Store store = databaseAccess.getStoreByUUID(beacons.iterator().next().getId1().toString());
                        if (store != null) {
                            System.out.println(store.getName());
                            Frequentation frequentation = new Frequentation(null, idClient,store.getName(), state, "");
                            Call<Frequentation> call = service.save(frequentation);
                            call.enqueue(new Callback<Frequentation>() {
                                @Override
                                public void onResponse(Call<Frequentation> call, Response<Frequentation> response) {
                                    System.out.println("send OK ********** Entree ");
                                    System.out.println(response.body().toString());
                                }

                                @Override
                                public void onFailure(Call<Frequentation> call, Throwable t) {
                                    System.out.println("send failed **********");
                                    System.out.println(t.getCause());
                                }
                            });
                            NotificationService notification = new NotificationService();
                            notification.createNotification(store.getName(),"Bienvenue chez",context,context.getSystemService(Context.NOTIFICATION_SERVICE));
                        }
                    } else if (checkDistancetoStore(beacons.iterator().next().getDistance()) == false) {
                        if (state != "Sortie") {
                            System.out.println("client sortie");
                            String idClient = "1";
                            state = "Sortie";
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                            databaseAccess.openDatabse();
                            Store store = databaseAccess.getStoreByUUID(beacons.iterator().next().getId1().toString());
                            if (store != null) {
                                Frequentation frequentation = new Frequentation(null, idClient, store.getName(), state, "");
                                Call<Frequentation> call = service.save(frequentation);
                                call.enqueue(new Callback<Frequentation>() {
                                    @Override
                                    public void onResponse(Call<Frequentation> call, Response<Frequentation> response) {
                                        System.out.println("send OK outisde **********");
                                    }

                                    @Override
                                    public void onFailure(Call<Frequentation> call, Throwable t) {
                                        System.out.println("send failed **********");
                                    }
                                });
                            }
                            NotificationService notification = new NotificationService();
                            notification.createNotification(store.getName(),"Bonne Journée",context,context.getSystemService(Context.NOTIFICATION_SERVICE));

                        }
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    /**
     * Check if distance between beacon and store is greatter than distance entree
     * if distance is lower so client is in the store (return true)  and if it is greatter the client is not inside the store (return false)
     *
     * @param distance
     * @return
     */
    public boolean checkDistancetoStore(double distance) {
        boolean response = false;
        if (distance < distanceEntree) {
            response = true;
        }
        return response;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }
}
