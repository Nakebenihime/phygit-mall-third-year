package com.example.dilemma_mobile.model;


public class Frequentation {

    private String id;
    private String id_client;
    private String storeName;
    private String in_out;
    private String date;


    public Frequentation(String id, String id_client, String storeName, String in_out, String date) {
        this.id = id;
        this.id_client = id_client;
        this.storeName = storeName;
        this.in_out = in_out;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getIn_out() {
        return in_out;
    }

    public void setIn_out(String in_out) {
        this.in_out = in_out;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
