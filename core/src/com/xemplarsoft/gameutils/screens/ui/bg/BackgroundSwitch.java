package com.xemplarsoft.gameutils.screens.ui.bg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xemplarsoft.gameutils.screens.ui.Clickable;

public class BackgroundSwitch extends Background implements Clickable {
    protected Background b0, b1;
    protected TextureRegion r1;
    public boolean state;

    public BackgroundSwitch(Background b0, Background b1) {
        super(b0.r0);
        this.b0 = b0;
        this.b1 = b1;
    }

    public BackgroundSwitch(TextureRegion r0, TextureRegion r1) {
        super(r0);
        this.r1 = r1;
    }

    public BackgroundSwitch(TextureAtlas atlas, String name, int i1, int i2) {
        super(atlas, name, i1);
        this.r1 = atlas.findRegion(name, i2);
    }

    public void update(float delta) {
        if(b0 != null) {
            b0.update(delta);
            b1.update(delta);
        }
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        if(b0 != null){
            if(state) b1.render(batch, x, y, w, h);
            else b0.render(batch, x, y, w, h);
        } else {
            batch.draw(state ? r1 : r0, x, y, w / 2, h / 2, w, h, 1F, 1F, 0F);
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(b0 != null){
            if(state && b1 instanceof Clickable){
                ((Clickable) b1).touchDown(x, y, pointer, button);
            } else if(!state && b0 instanceof Clickable) {
                ((Clickable) b0).touchDown(x, y, pointer, button);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(float x, float y, int pointer, int button) {
        if(b0 != null){
            if(state && b1 instanceof Clickable){
                ((Clickable) b1).touchUp(x, y, pointer, button);
            } else if(!state && b0 instanceof Clickable) {
                ((Clickable) b0).touchUp(x, y, pointer, button);
            }
        }
        state = !state;
        return false;
    }
}
