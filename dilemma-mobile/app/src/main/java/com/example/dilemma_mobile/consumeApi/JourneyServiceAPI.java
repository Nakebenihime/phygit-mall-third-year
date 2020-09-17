package com.example.dilemma_mobile.consumeApi;

import com.example.dilemma_mobile.model.Node;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JourneyServiceAPI {
    @GET("/api/v1/journeys/")
    Call<List<Node>> getNodes();
}
