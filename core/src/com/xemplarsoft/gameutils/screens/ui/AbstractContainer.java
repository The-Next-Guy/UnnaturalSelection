package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ArrayMap;

public class AbstractContainer extends AbstractComponent{
    protected ArrayMap<AbstractComponent, AbstractConstraint> components = new ArrayMap<>();

    public void addComponent(AbstractComponent comp){
        components.put(comp, null);
    }

    public void addComponent(AbstractComponent comp, AbstractConstraint cons){
        components.put(comp, cons);
    }

    public AbstractComponent[] getComponents(){
        AbstractComponent[] comps = new AbstractComponent[components.size];
        for(int i = 0; i < comps.length; i++){
            comps[i] = components.getKeyAt(i);
        }
        return comps;
    }

    public AbstractComponent findCompByID(long ID){
        for(int i = 0; i < components.size; i++){
            if(components.getKeyAt(i).getID() == ID) return components.getKeyAt(i);
        }
        return null;
    }

    public boolean setActionByID(long ID, Action a){
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            if(comp.getID() == ID && comp instanceof Button){
                ((Button) comp).action = a;
                return true;
            }
            if(comp.getID() == ID && comp instanceof Panel){
                ((Panel) comp).action = a;
                return true;
            }
            if(comp.getID() == ID && comp instanceof ListView){
                ((ListView<?>) comp).setAction(a);
                return true;
            }
        }
        return false;
    }

    public boolean removeCompByID(long ID){
        for(int i = 0; i < components.size; i++){
            if(components.getKeyAt(i).getID() == ID) {
                components.removeIndex(i);
                return true;
            }
        }
        return false;
    }

    public AbstractContainer setBounds(float x, float y, float width, float height){
        setPos(x, y);
        this.width = width;
        this.height = height;

        return this;
    }

    public void renderComp(SpriteBatch batch) {
        for(int i = 0; i < components.size; i++){
            components.getKeyAt(i).renderComp(batch);
        }
    }

    public void updateComp(float delta) {
        for(int i = 0; i < components.size; i++){
            components.getKeyAt(i).updateComp(delta);
        }
    }

    public boolean touchDown(float gridX, float gridY, int pointer, int button) {
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.touchDown(gridX, gridY, pointer, button);
        }
        return ret;
    }

    public boolean touchUp(float gridX, float gridY, int pointer, int button) {
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            if(comp.touchUp(gridX, gridY, pointer, button)) return true;
        }
        return ret;
    }

    public boolean touchDragged(float gridX, float gridY, int pointer) {
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.touchDragged(gridX, gridY, pointer);
        }
        return ret;
    }

    public boolean mouseMoved(int gridX, int gridY) {
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.mouseMoved(gridX, gridY);
        }
        return ret;
    }

    public boolean scrolled(float amountX, float amountY) {
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.scrolled(amountX, amountY);
        }
        return ret;
    }
}
