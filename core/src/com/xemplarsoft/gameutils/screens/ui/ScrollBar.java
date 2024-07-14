package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScrollBar extends AbstractComponent{
    protected int min, max, val;
    protected boolean orientation;
    protected TextureRegion tabRegion;
    protected float tabMovementPadding = 0.1F;

    public ScrollBar(int min, int max){
        this.min = min;
        this.max = max;
    }

    public void setTabRegion(TextureRegion region){
        this.tabRegion = region;
    }

    public ScrollBar setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        orientation = width > height;

        return this;
    }

    public void setValue(int val){
        if(val < min) val = min;
        if(val > max) val = max;

        this.val = val;
    }

    public void addValue(int val){
        val += this.val;
        if(val < min) val = min;
        if(val > max) val = max;

        this.val = val;
    }

    public int getValue(){
        return val;
    }

    public void setMin(int min){
        this.min = min;
    }

    public void setMax(int max){
        this.max = max;
    }

    protected void renderComp(SpriteBatch batch) {
        if(bg != null) bg.render(batch, x, y, width, height);
        if(tabRegion != null){
            if(orientation){ //Horizontal
                batch.draw(tabRegion, x + tabMovementPadding + ((float)(val - min) / (max - min) * (width - (tabMovementPadding / 2))), y, height, height);
            } else { //Vertical
                batch.draw(tabRegion, x, y - tabMovementPadding - ((float)(val - min) / (max - min) * (height - (tabMovementPadding * 2))), width, width);
            }
        }
    }

    public void updateComp(float delta) {
    }
}
