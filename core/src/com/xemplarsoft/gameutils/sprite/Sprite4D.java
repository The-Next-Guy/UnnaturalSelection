package com.xemplarsoft.gameutils.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite4D extends Sprite{
    protected Sprite u;
    protected Sprite d;
    protected Sprite l;
    protected Sprite r;

    public Sprite4D(Sprite u, Sprite d, Sprite l, Sprite r){
        this.u = u;
        this.d = d;
        this.l = l;
        this.r = r;
    }

    public Sprite4D(TextureRegion u, TextureRegion d, TextureRegion l, TextureRegion r){
        this.u = new Sprite(u);
        this.d = new Sprite(d);
        this.l = new Sprite(l);
        this.r = new Sprite(r);
    }

    public Sprite4D setFlag(int index, boolean value) {
        u.setFlag(index, value);
        d.setFlag(index, value);
        l.setFlag(index, value);
        r.setFlag(index, value);

        return (Sprite4D) super.setFlag(index, value);
    }

    public boolean getFlag(int index) {
        int region = this.data % 4;
        switch (region){
            case 1: return this.u.getFlag(index);
            case 3: return this.d.getFlag(index);
            case 0: return this.l.getFlag(index);
            case 2: return this.r.getFlag(index);
        }

        return super.getFlag(index);
    }

    public Sprite clone(){
        return new Sprite4D(u.clone(), d.clone(), l.clone(), r.clone());
    }

    public TextureRegion getRegion() {
        int region = this.data % 4;
        switch (region){
            case 1: return this.u.getRegion();
            case 3: return this.d.getRegion();
            case 0: return this.l.getRegion();
            case 2: return this.r.getRegion();
        }

        return super.getRegion();
    }
}
