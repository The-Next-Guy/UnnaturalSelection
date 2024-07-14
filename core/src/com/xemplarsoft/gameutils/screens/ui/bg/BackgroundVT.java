package com.xemplarsoft.gameutils.screens.ui.bg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundVT extends Background{
    protected TextureRegion r1, r2;
    protected float vtTrim;

    public BackgroundVT(TextureRegion r0, TextureRegion r1, TextureRegion r2){
        super(r0);
        this.r1 = r1;
        this.r2 = r2;
    }

    public BackgroundVT(TextureAtlas atlas, String name, int start){
        super(atlas.findRegion(name, start));
        this.r1 = atlas.findRegion(name, start + 1);
        this.r2 = atlas.findRegion(name, start + 2);
    }

    public BackgroundVT setVTTrim(float trim){
        vtTrim = trim;
        return this;
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        float ratio = (h + vtTrim * 2) / w;
        if(ratio > 1.99F && ratio < 2.01F){
            batch.draw(r0, x, y - vtTrim, w, h/2);
            batch.draw(r2, x, y + h/2 + vtTrim, w, h/2);
        }
        if(ratio > 2.01F){
            float mid_height = h + (vtTrim * 2) - (w * 2) ;
            batch.draw(r0, x, y + vtTrim - (w/2), w, w);
            batch.draw(r1, x, y + vtTrim - mid_height - (w/2), w, mid_height);
            batch.draw(r2, x, y - h + w - vtTrim - (w/2), w, w);
        }
    }
}
