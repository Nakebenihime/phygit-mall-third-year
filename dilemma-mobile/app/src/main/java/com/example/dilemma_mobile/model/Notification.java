package com.example.dilemma_mobile.model;

public class Notification {

    private String title;
    private String body;
    private Boolean hasBeenSent;
    private int TTL;

    public Notification() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getHasBeenSent() {
        return hasBeenSent;
    }

    public void setHasBeenSent(Boolean hasBeenSent) {
        this.hasBeenSent = hasBeenSent;
    }

    public int getTTL() {
        return TTL;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }
}
