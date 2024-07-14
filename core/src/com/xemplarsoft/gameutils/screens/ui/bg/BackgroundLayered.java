package com.xemplarsoft.gameutils.screens.ui.bg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xemplarsoft.gameutils.screens.ui.Clickable;

public class BackgroundLayered extends Background implements Clickable {
    protected Background[] layers;
    public BackgroundLayered(Background... layers){
        super(layers[0].r0);
        this.layers = layers;
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        for(int i = 0; i < layers.length; i++){
            layers[i].render(batch, x, y, w, h);
        }
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        for(int i = 0; i < layers.length; i++){
            if(layers[i] instanceof Clickable) ((Clickable) layers[i]).touchDown(x, y, pointer, button);
        }
        return false;
    }

    public boolean touchUp(float x, float y, int pointer, int button) {
        for(int i = 0; i < layers.length; i++){
            if(layers[i] instanceof Clickable) ((Clickable) layers[i]).touchUp(x, y, pointer, button);
        }
        return false;
    }
}
