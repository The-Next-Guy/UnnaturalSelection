package com.xemplarsoft.gameutils.screens.ui.bg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xemplarsoft.gameutils.screens.ui.Clickable;
import com.xemplarsoft.gameutils.sprite.SpriteA;

public class BackgroundPress extends Background implements Clickable {
    protected SpriteA anim;
    protected boolean run;

    public BackgroundPress(TextureRegion def, TextureRegion... regions){
        super(def);
        this.anim = new SpriteA(0.1F, regions);
        this.anim.setFlag(0, true);
    }

    public BackgroundPress(TextureAtlas atlas, int defIndex, int startIndex, int endIndex, String region){
        super(atlas.findRegion(region, defIndex));
        TextureRegion[] regions = new TextureRegion[endIndex - startIndex + 1];
        for(int i = startIndex; i <= endIndex; i++){
            regions[i - startIndex] = atlas.findRegion(region, i);
        }
        this.anim = new SpriteA(0.09F, regions);
        this.anim.setFlag(0, true);
    }

    public BackgroundPress(TextureAtlas atlas, int defIndex, int startIndex, int endIndex, int[] order, String region){
        super(atlas.findRegion(region, defIndex));
        TextureRegion[] regions = new TextureRegion[endIndex - startIndex + 1];
        for(int i = startIndex; i <= endIndex; i++){
            regions[i - startIndex] = atlas.findRegion(region, i);
        }
        this.anim = new SpriteA(0.09F, order, regions);
        this.anim.setFlag(0, true);
    }

    public void animate(){
        anim.reset();
        run = true;
    }

    public void update(float delta) {
        super.update(delta);
        if(run && !anim.getFlag(1)) {
            anim.update(delta);
        }
        if(anim.getFlag(1)) {
            run = false;
        }
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        if(!run) {
            super.render(batch, x, y, w, h);
        } else {
            anim.render(batch, x, y, w, h, 0);
        }
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        animate();
        return false;
    }

    public boolean touchUp(float x, float y, int pointer, int button) {
        anim.reset();
        run = false;

        return false;
    }
}
