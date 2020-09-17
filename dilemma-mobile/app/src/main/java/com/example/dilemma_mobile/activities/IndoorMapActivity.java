package com.example.dilemma_mobile.activities;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dilemma_mobile.R;
import com.example.dilemma_mobile.consumeApi.JourneyServiceAPI;
import com.example.dilemma_mobile.consumeApi.ServiceGeneratorJourney;
import com.example.dilemma_mobile.model.Node;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Display an indoor map of a building with toggles to switch between floor levels
 */
public class IndoorMapActivity extends AppCompatActivity  {

    WebView webView;
    String script = "map.querySelector(\".image_map\").style.background = 'red';";
    final JourneyServiceAPI service = ServiceGeneratorJourney.createService(JourneyServiceAPI.class);
    List<Node> nodes;
    List<String> nodesTest;
    String nodesId;
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        webView = findViewById(R.id.mapView);
        nodes = new ArrayList<>();
        nodesTest = new ArrayList<>();
        final String nodesC[];
        final String nodesT[] ={};
        Call<List<Node>> call = service.getNodes();
        call.enqueue(new Callback<List<Node>>() {
            @Override
            public void onResponse(Call<List<Node>> call, Response<List<Node>> response) {
                nodes = response.body();
            }

            @Override
            public void onFailure(Call<List<Node>> call, Throwable t) {

            }
        });

        nodesId = "";
        nodes.add(new Node("1","Alley_15",0,0));
        nodes.add(new Node("2","Alley_56",1,1));
        nodes.add(new Node("3","Alley_55",2,2));
        nodes.add(new Node("4","Alley_51",3,3));
        for ( int k=0; k<nodes.size();k++){
           // System.out.println(nodesId + "               forr");
           nodesId=nodesId+nodes.get(k).name+",";
            nodesTest.add("'"+nodes.get(k).name+"'");
        }

        final String[] AlleysArray = nodesTest.toArray(new String[nodesTest.size()]);
        nodesId = nodesId.substring(0,nodesId.length()-1);
        nodesId = nodesId + "";
        System.out.println(nodesId + "              ////////////////////");
        nodesId = nodesId+"]";
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String weburl){
                System.out.println("updateFromAndroid("+ nodesTest+")");
                webView.loadUrl("javascript:updateFromAndroid("+ nodesTest+")");
                //System.out.println("updateFromAndroid(["+nodesId+")");
               // webView.loadUrl("javascript:updateFromAndroid("+ AlleysArray +")");
            }
        });

    }

}