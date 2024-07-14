package com.xemplarsoft.gameutils.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Flags Used:
 * [0] repeat flag 0 = loop, 1 = once
 * [1] finished flag 0 = nope, 1 = finished
 */
public class SpriteA extends Sprite{
    public Sprite[] frames;
    public int[] order;
    public float[] delays;
    public int frame, maxFrames;
    public float counter;

    public SpriteA(float delay, TextureAtlas atlas, String texture, int start, int stop){
        this.delays = new float[stop - start];
        this.order = new int[stop - start];
        this.frames = new Sprite[stop - start];
        this.maxFrames = order.length;
        for(int i = 0; i < maxFrames; i++){
            delays[i] = delay;
            order[i] = i;
            frames[i] = new Sprite(atlas.findRegion(texture, i + start));
        }
    }
    public SpriteA(float delay, Sprite... frames){
        this.delays = new float[frames.length];
        this.order = new int[frames.length];
        for(int i = 0; i < frames.length; i++){
            this.delays[i] = delay;
            this.order[i] = i;
        }
        this.frames = frames;
        this.maxFrames = order.length;
    }

    public SpriteA(float delay, int[] order, Sprite... sprites){
        this.delays = new float[sprites.length];
        this.order = order;

        Sprite[] frames = new Sprite[sprites.length];
        for(int i = 0; i < sprites.length; i++){
            this.delays[i] = delay;
            frames[i] = sprites[i];
        }
        this.frames = frames;
        this.maxFrames = this.order.length;
    }

    public SpriteA(float[] delays, int[] order, Sprite... sprites){
        this.delays = new float[sprites.length];
        this.order = order;

        Sprite[] frames = new Sprite[sprites.length];
        for(int i = 0; i < sprites.length; i++){
            this.delays[i] = delays[i];
            frames[i] = sprites[i];
        }
        this.frames = frames;
        this.maxFrames = this.order.length;
    }

    public SpriteA(float delay, TextureRegion... regions){
        this.delays = new float[regions.length];
        this.order = new int[regions.length];

        Sprite[] frames = new Sprite[regions.length];
        for(int i = 0; i < regions.length; i++){
            delays[i] = delay;
            order[i] = i;
            frames[i] = new Sprite(regions[i]);
        }
        this.frames = frames;
        this.maxFrames = order.length;
    }

    public SpriteA(float[] delays, TextureRegion... regions){
        this.delays = new float[regions.length];
        this.order = new int[regions.length];

        Sprite[] frames = new Sprite[regions.length];
        for(int i = 0; i < regions.length; i++){
            this.delays[i] = delays[i];
            order[i] = i;
            frames[i] = new Sprite(regions[i]);
        }
        this.frames = frames;
        this.maxFrames = order.length;
    }

    public SpriteA(float delay, int[] order, TextureRegion... regions){
        this.delays = new float[order.length];
        this.order = order;

        Sprite[] frames = new Sprite[regions.length];
        for(int i = 0; i < regions.length; i++){
            frames[i] = new Sprite(regions[i]);
        }
        for(int i = 0; i < order.length; i++){
            this.delays[i] = delay;
        }
        this.frames = frames;
        this.maxFrames = this.order.length;
    }

    public SpriteA(float[] delays, int[] order, TextureRegion... regions){
        this.delays = new float[order.length];
        this.order = order;

        Sprite[] frames = new Sprite[regions.length];
        for(int i = 0; i < regions.length; i++){
            frames[i] = new Sprite(regions[i]);
        }
        for(int i = 0; i < order.length; i++){
            this.delays[i] = delays[i];
        }
        this.frames = frames;
        this.maxFrames = this.order.length;
    }

    public Sprite clone(){
        Sprite[] cloneFrame = new Sprite[frames.length];
        for(int i = 0; i < frames.length; i++){
            cloneFrame[i] = frames[i].clone();
        }
        return new SpriteA(delays.clone(), order.clone(), cloneFrame);
    }

    public void setData(int data) {
        super.setData(data);
        this.frame = data % maxFrames;
    }

    public void reset(){
        this.frame = 0;
        this.flag[1] = false;
        this.counter = 0;
    }

    public void update(float delta){
        if(getFlag(2)){
            setFlag(2, false);
            reset();
            return;
        }
        this.counter += delta;
        if(this.counter >= delays[frame] && !(frame == maxFrames - 1 && flag[0])){
            this.counter -= delays[frame];
            frame++;
            if(frame >= maxFrames && !flag[0]){
                frame = 0;
                flag[1] = true;
            }
        }
        if(frame == maxFrames - 1 && flag[0]){
            flag[1] = true;
        }
    }

    public TextureRegion getRegion() {
        return frames[order[frame]].getRegion();
    }
}
