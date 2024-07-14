package com.xemplarsoft.gameutils.screens.ui.anim;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.xemplarsoft.gameutils.screens.ui.AbstractComponent;

public class TranslateAnimation extends Animation{
    protected Vector2 start, end;
    protected boolean invert = false;

    public TranslateAnimation(Vector2 end, float duration){
        this.duration = duration;
        this.end = end;
    }
    public void setComponent(AbstractComponent comp) {
        super.setComponent(comp);
        this.start = comp.getPos();
    }

    protected void updateAni(float delta) {
        if(invert)
            this.component.setPos(end.cpy().interpolate(start, counter / duration, Interpolation.pow3Out));
        else
            this.component.setPos(start.cpy().interpolate(end, counter / duration, Interpolation.pow3Out));
    }

    protected void onFinish() {
        component.setPos(invert ? start : end);
        super.onFinish();
    }

    public void reset() {
        super.reset();
        component.setPos(invert ? end : start);
    }

    public boolean isInverse() {
        return invert;
    }

    public void inverse(){
        this.invert = !invert;
    }
}
