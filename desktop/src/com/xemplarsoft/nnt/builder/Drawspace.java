package com.xemplarsoft.nnt.builder;

import com.badlogic.gdx.utils.Array;

import javax.swing.*;
import java.awt.*;

public class Drawspace extends JPanel {
    public Array<Entity> entities;
    public Array<Safezone> safezones;

    public int GRID_SIZE = 128;

    public Drawspace(){
        entities = new Array<>();
        safezones = new Array<>();
    }

    public void addEntity(Entity e, int x, int y){
        e.x = x;
        e.y = y;
        entities.add(e);
    }

    public int drawWidth, drawHeight, drawSize, offX, offY;
    protected void paintComponent(Graphics g) {
        drawWidth = getWidth();
        drawHeight = getHeight();

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, drawWidth, drawHeight);

        if(drawHeight > drawWidth){
            drawSize = drawWidth;
            offX = 0;
            offY = (drawHeight - drawWidth) / 2;
        } else {
            drawSize = drawHeight;
            offX = (drawWidth - drawHeight) / 2;
            offY = 0;
        }

        if(drawSize > GRID_SIZE * 3){
            drawSize = GRID_SIZE * 3;
            offX = (drawWidth - drawSize) / 2;
            offY = (drawHeight - drawSize) / 2;
        }

        g.setColor(Color.WHITE);
        g.fillRect(offX, offY, drawSize, drawSize);

        for(Entity e : entities){
            g.setColor(e.c);
            g.fillRect(offX + e.x / GRID_SIZE * drawSize, offY + e.y / GRID_SIZE * drawSize, drawSize / GRID_SIZE, drawSize / GRID_SIZE);
        }
    }
}
