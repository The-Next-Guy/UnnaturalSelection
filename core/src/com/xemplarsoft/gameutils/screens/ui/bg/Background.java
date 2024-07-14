package com.xemplarsoft.gameutils.screens.ui.bg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    public static Background wood_panel;
    public static Background wood_frame;

    public static void initBackgrounds(){

    }

    protected TextureRegion r0;

    public Background(TextureRegion r0){
        this.r0 = r0;
    }

    public Background(TextureAtlas atlas, String name, int index){
        this.r0 = atlas.findRegion(name, index);
    }

    public void update(float delta){

    }
    public void render(SpriteBatch batch, float x, float y, float w, float h){
        batch.draw(r0, x, y, w / 2, h / 2, w, h, 1F, 1F, 0F);
    }
}
