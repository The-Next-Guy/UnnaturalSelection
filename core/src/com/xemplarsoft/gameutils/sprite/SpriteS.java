package com.xemplarsoft.gameutils.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.StringBuilder;

public class SpriteS extends Sprite{
    protected ArrayMap<String, Sprite> states;
    protected Sprite current;
    protected String state = "", defState;

    public SpriteS(){
        states = new ArrayMap<>();
    }

    public SpriteS(String def){
        states = new ArrayMap<>();
        this.state = this.defState = def;
    }

    public SpriteS(ArrayMap<String, Sprite> states, String def){
        this.states = states;
        this.defState = def;
        setState(def);
    }

    public SpriteS addState(String state, Sprite s){
        if(s == this) return null;

        states.put(state.trim(), s);
        return this;
    }

    public SpriteS setDefaultState(String def){
        this.defState = def;
        this.setState(def);
        return this;
    }

    public SpriteS setState(String state){
        this.current = states.get(state);
        this.state = state;

        return this;
    }

    public String getState(){
        return state;
    }

    public void setData(int data){
        this.data = data;
        for(int i = 0; i < states.size; i++){
            states.getValueAt(i).setData(data);
        }
    }

    @Override
    public void render(SpriteBatch batch, float x, float y, float w, float h, float degrees) {
        if(this.current == null) return;
        this.current.render(batch, x, y, w, h, degrees);
    }

    public void update(float delta) {
        if(this.current == null){
            System.out.println("States:");
            for(String s : this.states.keys()) {
                System.out.print(s + " ");
            }
            System.out.println();
        }

        this.current.update(delta);
    }

    public TextureRegion getRegion() {
        return this.current.getRegion();
    }

    public SpriteS setFlag(int index, boolean value) {
        if(current == null) super.setFlag(index, value); else current.setFlag(index, value);
        return this;
    }

    public boolean getFlag(int index) {
        return current == null ? super.getFlag(index) : current.getFlag(index);
    }

    public SpriteS clone(){
        ArrayMap<String, Sprite> cloneSprites = new ArrayMap<>();
        String[] states = this.states.keys;
        for(int i = 0; i < states.length; i++){
            cloneSprites.put(states[i], this.states.get(states[i]).clone());
        }
        return new SpriteS(cloneSprites, defState);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String s : states.keys()){
            sb.append(s).append(",");
        }
        return sb.toString();
    }

    public static SpriteS createBooleanSprite(Sprite f, Sprite t){
        SpriteS ret = new SpriteS(Boolean.toString(false));
        ret.addState(Boolean.toString(true), t);
        ret.addState(Boolean.toString(false), f);
        ret.setState(Boolean.toString(false));

        return ret;
    }

    public static SpriteS createBooleanSprite(TextureRegion f, TextureRegion t){
        return createBooleanSprite(new Sprite(f), new Sprite(t));
    }
}
