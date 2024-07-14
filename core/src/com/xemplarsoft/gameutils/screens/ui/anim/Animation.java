package com.xemplarsoft.gameutils.screens.ui.anim;

import com.xemplarsoft.gameutils.screens.ui.AbstractComponent;

public abstract class Animation {
    protected AnimationListener listener;
    protected long aniID;
    protected float counter, duration;
    protected boolean started, finished;
    protected AbstractComponent component;

    public final void setListener(AnimationListener listener){
        this.listener = listener;
    }

    public final void update(float delta){
        if(listener != null) listener.onUpdate(aniID);
        if(started && !finished) {
            counter += delta;
            finished = counter >= duration;
            if(finished && listener != null) {
                onFinish();
                listener.onFinish(aniID);
            }
        }
        updateAni(delta);
    }

    protected void updateAni(float delta){}
    protected void onStart() {}
    protected void onFinish() {}

    public void setComponent(AbstractComponent comp){
        this.component = comp;
    }

    public void setDuration(float duration){
        this.duration = duration;
    }

    public void start(long id){
        this.aniID = id;
        this.started = true;
        this.finished = false;
        onStart();
        if(listener != null) listener.onStart(id);
    }

    public void stop(){
        started = false;
    }

    public void reset(){
        this.started = false;
        this.finished = false;
        this.counter = 0;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }
}
