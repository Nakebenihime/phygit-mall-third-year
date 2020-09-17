package com.example.dilemma_mobile.service;

import android.util.Log;

import com.example.dilemma_mobile.model.Location;
import com.example.dilemma_mobile.model.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
@PrepareForTest({Log.class})
@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
public class IndoorNavigationServiceTest {

    private IndoorNavigationService indoorNavigation;
    private List<Store> storeList;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Log.class);
        indoorNavigation = new IndoorNavigationService();
        storeList = new ArrayList<>();
        storeList= new ArrayList<Store>();
        Store store = new Store("1", "DARTY", "video games, books, ...", 30d, 30d);
        Store store2 = new Store("2","FNAC","video games, books, ...",35d,30d);
        storeList.add(store);
        storeList.add(store2);
        Location currentLocation = new Location(20d, 10d,1);
        indoorNavigation.setCurrentLocation(currentLocation);
        indoorNavigation.setTarget(storeList.get(0));// to verify
        indoorNavigation.setDistanceX(indoorNavigation.getTarget().getLatitude() - currentLocation.getX());
        indoorNavigation.setDistanceY(indoorNavigation.getTarget().getLongitude() - currentLocation.getY());

        indoorNavigation.setStoreList(storeList);

    }

    @Test
    public void verifyUserChangeLocation_UserLocationHasChanged() {
        Location newLocation = new Location(21d, 10d,1);
        assertTrue(indoorNavigation.verifyUserChangeLocation(newLocation));
    }

    @Test
    public void verifyUserChangeLocation_UserLocationHasNotChanged() {
        Location newLocation = new Location(20d, 10d,1);
        assertFalse(indoorNavigation.verifyUserChangeLocation(newLocation));
    }

    @Test
    public void verifyGetDirectionX_GoodDirectionToTheRight() {//  x POSITIF
        Location newLocation = new Location(20d, 10d,1);
        assertEquals(indoorNavigation.getDirectionX(newLocation), "to the right");
    }

    @Test
    public void verifyGetDirectionX_GoodDirectionToTheLeft() {// x NEGATIF
        Location newLocation = new Location(40d, 10d,1);
        assertEquals(indoorNavigation.getDirectionX(newLocation), "to the left");
    }

    @Test
    public void verifyGetDirectionY_GoodDirectionGoBehind() {// y NEGATIF
        Location newLocation = new Location(20d, 40d,1);
        assertEquals(indoorNavigation.getDirectionY(newLocation), "go behind");
    }

    @Test
    public void verifyGetDirectionY_GoodDirectionGoSraightAhead() {//  y POSITIF
        Location newLocation = new Location(40d, 10d,1);
        assertEquals(indoorNavigation.getDirectionY(newLocation), "go straight ahead");
    }


//    @Test
//    public void verifyUserDirection_GoodDirection() {
//        Location newLocation = new Location(21d, 11d);
//        assertEquals(indoorNavigation.verifyUserDirection(newLocation), "You're going the right way");
//
//    }
//
//    @Test
//    public void verifyUserDirection_WrongDirection() {
//        Location newLocation = new Location(19d, 9d);
//        assertEquals(indoorNavigation.verifyUserDirection(newLocation), "You're going the wrong way");
//    }

    @Test
    public void verifyUserDirection_Arrived() {
        Location newLocation = new Location(30d, 30d,1);
        assertEquals(indoorNavigation.getUserDirection(newLocation), "You arrived to destination");

    }

    @Test
    public void updateGPSCoordinates() {
        double olddistanceX = indoorNavigation.getDistanceX();
        double olddistanceY = indoorNavigation.getDistanceY();
        Location newLocaion = new Location(19d, 9d,1);

        indoorNavigation.updateGPSCoordinates(newLocaion);
        double newdistanceY = indoorNavigation.getDistanceY();
        double newdistanceX = indoorNavigation.getDistanceX();

        assertNotEquals(olddistanceY, newdistanceY);
        assertNotEquals(olddistanceX, newdistanceX);

    }

    @Test
    public void skipNotNull(){
        assertNotNull(storeList);
    }

    @Test
    public void skipArrayNotEquals(){
        int expected = indoorNavigation.getStoreList().size();
        System.out.println(indoorNavigation.getStoreList().get(0).getName());
        indoorNavigation.skip();
        assertNotEquals(new int[]{expected},new int[]{indoorNavigation.getStoreList().size()});
        System.out.println(indoorNavigation.getStoreList().get(0).getName());
    }
}