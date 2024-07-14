package com.xemplarsoft.gameutils.screens.ui;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.xemplarsoft.gameutils.screens.ui.bg.Background;

public abstract class AbstractComponent {
    protected Background bg;
    public boolean visible = true;
    public float x, y, width, height;
    public long ID = -1;
    public String data = "";

    public AbstractComponent(){}
    public AbstractComponent setBounds(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        return this;
    }
    public void setBounds(Rectangle rect){
        this.setBounds(rect.x, rect.y, rect.width, rect.height);
    }
    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }
    public Rectangle getBounds(Rectangle rect){
        return rect.set(x, y, width, height);
    }
    public void setBg(Background bg){
        this.bg = bg;
    }
    public void setPos(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setPos(Vector2 vec){
        this.setPos(vec.x, vec.y);
    }
    public Vector2 getPos(){
        return new Vector2(x, y);
    }
    public Vector2 getPos(Vector2 vec){
        return vec.set(x, y);
    }
    public boolean inside(Vector2 pos){
        return getBounds().contains(pos);
    }
    public boolean inside(float x, float y){
        return (x >= this.x) && (x <= this.x + width) && (y >= this.y) && (y <= this.y + height);
    }
    public long getID() {
        return ID;
    }
    public void setID(long ID) {
        this.ID = ID;
    }

    public void setVisible(boolean vis){
        this.visible = vis;
    }
    public boolean isVisible(){
        return visible;
    }

    protected abstract void renderComp(SpriteBatch batch);
    public abstract void updateComp(float delta);
    public boolean touchDown(float gridX, float gridY, int pointer, int button){
        return false;
    }
    public boolean touchUp(float gridX, float gridY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(float gridX, float gridY, int pointer) {
        return false;
    }

    public boolean mouseMoved(float gridX, float gridY) {
        return false;
    }

    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
