package com.example.dilemma_mobile.consumeApi;

import com.example.dilemma_mobile.model.Location;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IndoorLocationServiceAPI {

    @GET("/api/v1/locations/")
    Call<List<Location>> getLocations();

    @GET("/api/v1/locations/last/")
    Call<Location> getCurrentLocation();

    @POST("/api/v1/locations/")
    Call<Location> addLocation(@Body Location location);

    @DELETE("/api/v1/locations/{id}")
    Call<Location> deleteLocation(@Path("id") String id);

}

