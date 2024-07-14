package com.xemplarsoft.nnt.entity;

import com.badlogic.gdx.graphics.Color;

public class Food extends Entity {
    protected int qtyLeft = 0, qtyMax = 0;
    protected String name;

    public Food(int x, int y, String name, Color c){
        qtyLeft = -1;
        this.name = name;
        this.color = c;
        this.x = x;
        this.y = y;
    }

    public Food(int x, int y, String name, Color c, int qty){
        qtyLeft = qty;
        qtyMax = qty;
        this.name = name;
        this.color = c;
        this.x = x;
        this.y = y;
    }

    public boolean obtainFood(){
        if(qtyLeft == -1) return true;

        if(qtyLeft > 0){
            qtyLeft--;
            return true;
        }
        return false;
    }

    public boolean empty(){
        return qtyLeft == 0;
    }

    public float foodFullness(){
        return 0.5F;
    }
}
