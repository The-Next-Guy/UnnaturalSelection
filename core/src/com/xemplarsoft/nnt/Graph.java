package com.xemplarsoft.nnt;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Graph {
    protected float xOff, yOff, w, h, spacing;
    protected Array<Integer> data;
    protected Vector2 lastPoint, thisPoint;
    public int maxSamples, yMax;

    public Graph(float x, float y, float w, float h){
        data = new Array<>();
        this.xOff = x;
        this.yOff = y;
        this.w = w;
        this.h = h;
        this.spacing = w / 24;

        this.thisPoint = new Vector2();
        this.lastPoint = new Vector2();
    }

    public void setMaxSamples(int maxSamples){
        this.maxSamples = maxSamples;
    }

    public void setSize(float width, float height){
        this.w = width;
        this.h = height;
    }

    public void setYMax(int yMax){
        this.yMax = yMax;
    }

    public Graph(float w, float h){
        this(0, 0, w, h);
    }

    public void setPos(float x, float y){
        this.xOff = x;
        this.yOff = y;
    }

    public Array<Integer> getData(){
        return data;
    }

    public void addData(int i){
        data.add(i);
    }

    public void drawGraphFill(ShapeRenderer shape){
        shape.setColor(0.5F, 0.5F, 0.5F, 1F);
        shape.rect(xOff, yOff, w, h);
    }

    public void drawGraphLines(ShapeRenderer shape){
        shape.setColor(0, 0, 0, 1F);
        shape.line(xOff + spacing, yOff + spacing, xOff + w - spacing, yOff + spacing);
        shape.line(xOff + spacing, yOff + spacing, xOff + spacing, yOff + h - spacing);

        shape.setColor(0, 1F, 0, 1F);
        int sampleCount = data.size;
        if(sampleCount > 1){
            int start = Math.max(0, sampleCount - 1 - maxSamples);

            lastPoint.set(0, data.get(start));
            float x1, x2, y1, y2;
            for(int i = 1; i < Math.min(sampleCount, maxSamples + 1); i++){
                thisPoint.set(i, data.get(i + start));
                x1 = xOff + spacing + ((w - spacing * 2) * thisPoint.x / Math.min(maxSamples, sampleCount - 1));
                x2 = xOff + spacing + ((w - spacing * 2) * lastPoint.x / Math.min(maxSamples, sampleCount - 1));
                y1 = yOff + spacing + ((h - spacing * 2) * thisPoint.y / yMax);
                y2 = yOff + spacing + ((h - spacing * 2) * lastPoint.y / yMax);
                shape.line(x1, y1, x2, y2);
                lastPoint.set(thisPoint.x, thisPoint.y);

                //shape.circle(x1, y1, 0.05F, 8);
                //shape.circle(x2, y2, 0.05F, 8);
            }
        }
    }
}
