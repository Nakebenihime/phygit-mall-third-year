package com.example.dilemma_mobile.model;

public class Node {
    public String id;
    public String name;
    public float x;
    public float y;

    public Node (String id, String name, float x, float y){
        this.id=id;
        this.name=name;
        this.x=x;
        this.y=y;

    }
    public String getName(){
        return  this.name;
    }
}
