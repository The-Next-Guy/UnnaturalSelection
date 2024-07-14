package com.xemplarsoft.gameutils.screens.ui.bg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundHZ extends Background{
    protected TextureRegion r1, r2;
    protected float hzTrim;

    public BackgroundHZ(TextureRegion r0, TextureRegion r1, TextureRegion r2){
        super(r0);
        this.r1 = r1;
        this.r2 = r2;
    }

    public BackgroundHZ(TextureAtlas atlas, String name, int start){
        super(atlas.findRegion(name, start));
        this.r1 = atlas.findRegion(name, start + 1);
        this.r2 = atlas.findRegion(name, start + 2);
    }

    public BackgroundHZ setHZTrim(float trim){
        hzTrim = trim;
        return this;
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        float ratio = (w + hzTrim * 2) / h;
        if(ratio > 1.99F && ratio < 2.01F){
            batch.draw(r0, x - hzTrim, y, 0, 0, w/2, h, 1F, 1F, 0);
            batch.draw(r2, x + w/2 + hzTrim, y, 0, 0, w/2, h, 1F, 1F, 0);
        }
        if(ratio >= 2.01F){
            batch.draw(r0, x - hzTrim, y, 0, 0, h, h, 1F, 1F, 0);
            batch.draw(r1, x + h - hzTrim, y, 0, 0, w - (h * 2) + (hzTrim * 2), h, 1F, 1F, 0);
            batch.draw(r2, x + w - h + hzTrim, y, 0, 0, h, h, 1F, 1F, 0);
        }
    }
}
