package com.xemplarsoft.gameutils.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteL extends Sprite{
    public Sprite[] sprites;

    public SpriteL(Sprite... sprites){
        this.sprites = sprites;
    }

    public SpriteL(TextureRegion... regions){
        sprites = new Sprite[regions.length];
        for(int i = 0; i < regions.length; i++){
            sprites[i] = new Sprite(regions[i]);
        }
    }

    public Sprite clone(){
        return new SpriteL(sprites);
    }

    public void update(float delta) {
        super.update(delta);
        for(Sprite s : sprites) s.update(delta);
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h, float degrees) {
        for(int i = 0; i < sprites.length; i++) {
            this.region = sprites[i].getRegion();
            super.render(batch, x, y, w, h, degrees);
        }
    }
}
