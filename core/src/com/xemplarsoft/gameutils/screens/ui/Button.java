package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xemplarsoft.gameutils.screens.ui.bg.BackgroundPress;
import com.xemplarsoft.gameutils.screens.ui.bg.BackgroundSwitch;
import com.xemplarsoft.gameutils.sprite.Sprite;

public class Button extends Label implements Clickable{
    protected int button = -1;
    public Action action;
    public String upAction, downAction;
    public Sprite icon;
    public float iconPadding = 0;

    public Button(){}
    public Button(String text){
        this.text = text;
    }

    public void setIcon(TextureRegion region){
        this.icon = new Sprite(region);
    }

    public void setIcon(Sprite s){
        this.icon = s;
    }

    public void setUpAction(String upAction) {
        this.upAction = upAction;
    }

    public void setDownAction(String downAction) {
        this.downAction = downAction;
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        boolean ret = false;
        if(inside(x, y)) {
            if(bg != null && bg instanceof Clickable){
                ((Clickable) bg).touchDown(x, y, pointer, button);
            }
            this.button = pointer == -1 ? button : pointer;
            ret = true;
        }
        return ret;
    }

    public boolean touchUp(float x, float y, int pointer, int button) {
        boolean ret = false;
        if(inside(x, y)){
            if(bg != null && bg instanceof Clickable){
                ((Clickable) bg).touchUp(x, y, pointer, button);
            }
            if(action != null) action.action(this, upAction != null ? upAction : "up");
            ret = true;
        }
        this.button = -1;
        return ret;
    }

    public void updateComp(float delta) {
        super.updateComp(delta);
        if(bg != null) bg.update(delta);
    }

    public void renderComp(SpriteBatch batch) {
        super.renderComp(batch);
        if(icon != null) icon.render(batch, x + iconPadding, y + iconPadding, width - (iconPadding * 2), height - (iconPadding * 2), 0);
    }
}
