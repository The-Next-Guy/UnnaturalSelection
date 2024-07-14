package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xemplarsoft.gameutils.sprite.Sprite;

public class SpriteView extends AbstractComponent{
    protected Sprite s;

    public SpriteView(){}
    public SpriteView(Sprite s) {
        this.s = s;
    }

    public SpriteView(TextureRegion r) {
        this.s = new Sprite(r);
    }

    public void setSprite(Sprite s){
        this.s = s;
    }

    protected void renderComp(SpriteBatch batch) {
        if(bg != null){
            bg.render(batch, x, y, width, height);
        }
        if(s != null) s.render(batch, x, y, width, height, 0);
    }

    @Override
    public void updateComp(float delta) {

    }
}
