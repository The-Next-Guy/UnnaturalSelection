package com.xemplarsoft.gameutils.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite4DF extends Sprite{
    protected Sprite u;
    protected Sprite d;
    protected Sprite s;
    boolean left;

    public Sprite4DF(Sprite u, Sprite d, Sprite s, boolean left){
        this.u = u;
        this.d = d;
        this.s = s;
        this.left = left;
    }

    public Sprite4DF(TextureRegion u, TextureRegion d, TextureRegion s, boolean left){
        this.u = new Sprite(u);
        this.d = new Sprite(d);
        this.s = new Sprite(s);
        this.left = left;
    }

    public Sprite clone(){
        return new Sprite4DF(u.clone(), d.clone(), s.clone(), left);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        u.update(delta);
        d.update(delta);
        s.update(delta);
    }

    public void setData(int data) {
        super.setData(data);
        if(data == 2) flipX = true;
        if(data == 0) flipX = false;
    }

    public TextureRegion getRegion() {
        int region = this.data % 4;
        switch (region){
            case 1: return this.u.getRegion();
            case 3: return this.d.getRegion();
            case 0:
            case 2: return this.s.getRegion();
        }

        return super.getRegion();
    }

    public Sprite4DF setFlag(int index, boolean value) {
        u.setFlag(index, value);
        d.setFlag(index, value);
        s.setFlag(index, value);

        return (Sprite4DF) super.setFlag(index, value);
    }

    public boolean getFlag(int index) {
        int region = this.data % 4;

        switch (region){
            case 1: return this.u.getFlag(index);
            case 3: return this.d.getFlag(index);
            case 0:
            case 2: return this.s.getFlag(index);
        }


        return super.getFlag(index);
    }
}
