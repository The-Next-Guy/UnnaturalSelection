package com.xemplarsoft.nnt.builder;

public class Safezone {
    public int[] points;
    public int x, y, w, h;
    public int type;

    public Safezone(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.type = 0;
    }

    public Safezone(int x, int y, int r){
        this.x = x;
        this.y = y;
        this.w = r;

        this.type = 1;
    }

    public Safezone(int x1, int y1, int... morePoints){
        this.type = 2;
        this.x = x1;
        this.y = y1;
        points = morePoints;
    }
}
