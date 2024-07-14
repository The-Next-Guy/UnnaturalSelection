package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class LabelInflator extends ComponentInflator{
    public float itemHeight;
    public BitmapFont font;
    public int align;

    public LabelInflator(float itemHeight, BitmapFont font, int align){
        this.itemHeight = itemHeight;
        this.font = font;
        this.align = align;
    }

    public AbstractComponent inflate(Object data, int index) {
        Label ret = new Label(data.toString());
        ret.height = itemHeight;
        ret.font = font;
        ret.alignment = align;
        ret.setPadding(0, 0.25F, 0, 0.25F);
        return ret;
    }
}
