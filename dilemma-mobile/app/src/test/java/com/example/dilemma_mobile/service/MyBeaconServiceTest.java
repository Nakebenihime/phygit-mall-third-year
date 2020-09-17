package com.example.dilemma_mobile.service;

import android.util.Log;

import org.altbeacon.beacon.AltBeaconParser;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
@PowerMockIgnore("javax.net.ssl.*")
public class MyBeaconServiceTest {
    MyBeaconService myBeaconService;
    @Before
    public void setup() {

        myBeaconService= new MyBeaconService(null,null);
        myBeaconService.setDistanceEntree(2);
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void checkDistancetoStore_Actual_distance_lower_than2_should_retrun_true() {
        double distanceActuelle= 0.80;
        assertEquals(myBeaconService.checkDistancetoStore(distanceActuelle),true);
    }

    @Test
    public void checkDistancetoStore_Actual_distance_lower_than2_should_retrun_false(){
        double distanceActuelle= 3.08;
        assertEquals(myBeaconService.checkDistancetoStore(distanceActuelle),false);
    }

    @Test
    public void checkDistancetoStore_Actual_distance_equal_to_2_should_retrun_true(){
        double distanceActuelle= 2;
        assertEquals(myBeaconService.checkDistancetoStore(distanceActuelle),false);
    }
    @Test
    public void getBeaconid_should_return_true(){
        Beacon beacon = new Beacon.Builder()
                .setId1("76d18b11-41f4-4d34-9a8c-08679d4759e3")
                .build();
        System.out.println(beacon.getId1().toString());
        String id = beacon.getId1().toString();
        assertNotNull("id not empty ",id);
    }

    @Test
    public void getBeaconId_good_format_should_return_true(){
        Beacon beacon = new Beacon.Builder()
                .setId1("76d18b11-41f4-4d34-9a8c-08679d4759e3")
                .build();
        int id = beacon.getId1().toString().length();
        assertEquals(36,id);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void getBeaconId_should_return_null(){
        Beacon beacon = new Beacon.Builder()
                .build();
        System.out.println(beacon.getId1().toString());
        String id = beacon.getId1().toString();
        assertNull("id empty ",id);
    }

}