package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class Label extends AbstractComponent{
    public float padT, padR, padB, padL;
    public int alignment = Align.left | Align.center;
    public boolean wrap = false;
    public CharSequence text = "";
    public BitmapFont font;

    public Label(){
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.02F);
    }

    public Label(String text){
        this();
        this.text = text;
    }

    public void setPadding(float top, float right, float bottom, float left){
        this.padT = top;
        this.padR = right;
        this.padB = bottom;
        this.padL = left;
    }

    public void renderComp(SpriteBatch batch) {
        if(bg != null) bg.render(batch, x, y, width, height);
        font.draw(batch, text, x + padL, y + height - (height * 0.125F), width - (padL + padR), alignment, wrap);
    }
    public void updateComp(float delta) {

    }
}
