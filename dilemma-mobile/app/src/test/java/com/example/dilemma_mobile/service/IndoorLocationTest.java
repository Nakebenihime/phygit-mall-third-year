package com.example.dilemma_mobile.service;

import android.util.Log;

import com.example.dilemma_mobile.model.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
@PowerMockIgnore("javax.net.ssl.*")
public class IndoorLocationTest {

    private LocationService locationService;

    @Before
    public void setup() {
        locationService = new LocationService();
        PowerMockito.mockStatic(Log.class);

    }

    @Test
    public void verifyLocationHasPositiveCoordinates() {
        Location location = locationService.getLoc();
        assertTrue(location.getX() >= 0 && location.getY() >= 0);
    }

    @Test
    public void calculateDistanceTest() {
        double distance = locationService.calculateDistanceFromRSSI(-57, 2412);
        assertEquals(Math.round(distance), Math.round(7.000397));
    }

    @Test
    public void locationIsInReferencePoints() {
        Location location = locationService.getLoc();
        double[][] references = locationService.getReferences();
        boolean exists = false;
        for(int i = 0 ; i < references.length ; i++) {
            if((references[i][0] == location.getX()) && (references[i][1] == location.getY())) {
                exists = true;
            }
        }
        assertTrue(exists);
    }

}
