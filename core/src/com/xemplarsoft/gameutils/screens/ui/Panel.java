package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Panel extends AbstractContainer implements Clickable{
    public Action action;

    public void renderComp(SpriteBatch batch) {
        if(!visible) return;

        if(bg != null) bg.render(batch, x, y, width, height);
        super.renderComp(batch);
    }

    public void setVisible(boolean vis) {
        super.setVisible(vis);
    }

    public void setPos(float x, float y) {
        float dx = x - this.x;
        float dy = y - this.y;

        super.setPos(x, y);
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            comp.setPos(comp.getPos().add(dx, dy));
        }
    }

    public boolean touchDown(float gridX, float gridY, int pointer, int button) {
        return visible && super.touchDown(gridX, gridY, pointer, button);
    }

    public boolean touchUp(float gridX, float gridY, int pointer, int button) {
        if(!visible) return false;
        if(super.touchUp(gridX, gridY, pointer, button)) return true;

        if(inside(gridX, gridY)){
            if(action != null) action.action(this, "up");
            return true;
        }
        return false;
    }

    public boolean touchDragged(float gridX, float gridY, int pointer) {
        return visible && super.touchDragged(gridX, gridY, pointer);
    }
}
