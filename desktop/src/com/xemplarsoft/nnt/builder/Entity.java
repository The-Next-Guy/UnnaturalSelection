package com.xemplarsoft.nnt.builder;


import java.awt.*;

public class Entity {
    public int x, y;
    public Color c;
    public Type t;

    public Entity(Type t, Color c){
        this.c = c;
        this.t = t;
    }

    public enum Type{
        BARRIER,
        FOOD
    }
}
