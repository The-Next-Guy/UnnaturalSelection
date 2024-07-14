package com.xemplarsoft.gameutils.screens.ui.bg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background9P extends Background{
    protected TextureRegion r1, r2, r3, r4, r5, r6, r7, r8;
    protected float hzTrim, vtTrim;

    public Background9P(TextureAtlas atlas, String name, int start){
        super(atlas.findRegion(name, start));
        r1 = atlas.findRegion(name, start + 1);
        r2 = atlas.findRegion(name, start + 2);
        r3 = atlas.findRegion(name, start + 3);
        r4 = atlas.findRegion(name, start + 4);
        r5 = atlas.findRegion(name, start + 5);
        r6 = atlas.findRegion(name, start + 6);
        r7 = atlas.findRegion(name, start + 7);
        r8 = atlas.findRegion(name, start + 8);
    }

    public Background9P setTrim(float trim){
        return this.setTrim(trim, trim);
    }

    public Background9P setTrim(float hz, float vt){
        this.hzTrim = hz;
        this.vtTrim = vt;

        return this;
    }

    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        batch.draw(r0, x - hzTrim, y + h - 1F + vtTrim, 0, 0, 1F, 1F, 1F, 1F, 0);
        batch.draw(r1, x - hzTrim + 1F, y + h - 1F + vtTrim, 0, 0, w - 2F + (hzTrim * 2), 1F, 1F, 1F, 0);
        batch.draw(r2, x + hzTrim + w - 1F, y + h - 1F + vtTrim, 0, 0, 1F, 1F, 1F, 1F, 0);

        batch.draw(r3, x - hzTrim, y + 1F - vtTrim, 0, 0, 1F, h - 2F + (vtTrim * 2), 1F, 1F, 0);

        batch.draw(r4, x - hzTrim + 1F, y + 1F - vtTrim, 0, 0, w - 2F + (hzTrim * 2), h - 2F + (vtTrim * 2), 1F, 1F, 0);

        batch.draw(r5, x + hzTrim + w - 1F, y + 1F - vtTrim, 0, 0, 1F, h - 2F + (vtTrim * 2), 1F, 1F, 0);

        batch.draw(r6, x - hzTrim, y - vtTrim, 0, 0, 1F, 1F, 1F, 1F, 0);
        batch.draw(r7, x - hzTrim + 1F, y - vtTrim, 0, 0, w - 2F + (hzTrim * 2), 1F, 1F, 1F, 0);
        batch.draw(r8, x + hzTrim + w - 1F, y - vtTrim, 0, 0, 1F, 1F, 1F, 1F, 0);
    }
}
