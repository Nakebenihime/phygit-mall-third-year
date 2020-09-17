package com.example.dilemma_mobile.embedded;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;

import com.example.dilemma_mobile.model.Store;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Aberkane on 09/05/2020.
 */
@RunWith(RobolectricTestRunner.class)
public class DatabaseAccessTest {
    DatabaseAccess db;

    @Before
    public void setUp() throws Exception {
        db= new DatabaseAccess(ApplicationProvider.getApplicationContext());
        db.openDatabse();
    }

    @After
    public void tearDown() throws Exception {
        db.closeDatabase();
    }

    @Test
    public void getStoreByUUID_uuid_dont_exist() {
        Store store =db.getStoreByUUID("a");
        assertNull(store);
    }
    @Test
    public void insertStore_return_true() {
        Store store =new Store(UUID.randomUUID().toString(),"TEST","TESTTYPE",2,2,UUID.randomUUID().toString());
        boolean reuslt = db.insertStore(store);
        assertTrue(reuslt);
        db.deleteStore(store.getId());
    }

    @Test
    public void testDelete_return_one_result() {
        Store store =new Store(UUID.randomUUID().toString(),"TEST","TESTTYPE",2,2,UUID.randomUUID().toString());
        boolean reuslt = db.insertStore(store);
        int result =db.deleteStore(store.getId());
        assertEquals(1,result);
    }

    @Test
    public void testDelete_return_zero_result() {

        int result =db.deleteStore("notexist");
        assertEquals(0,result);
    }


}