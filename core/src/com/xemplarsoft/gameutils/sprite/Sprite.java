package com.xemplarsoft.gameutils.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite {
    protected boolean[] flag = new boolean[8];
    protected boolean flipX = false, flipY = false;
    protected TextureRegion region;
    protected int data;

    public Sprite(TextureRegion region){
        this.region = region;
    }

    protected Sprite(){}

    public void update(float delta){}
    public void setData(int data){
        this.data = data;
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h, float degrees){
        if(getRegion() == null) return;
        batch.draw(getRegion(), x, y, w / 2, h / 2, w, h, flipX ? -1 : 1, 1, degrees);
    }

    public Sprite clone(){
        return new Sprite(this.region);
    }

    public TextureRegion getRegion(){
        return this.region;
    }

    public Sprite setFlag(int index, boolean value){
        flag[index] = value;
        return this;
    }

    public boolean getFlag(int index){
        return flag[index];
    }
}
