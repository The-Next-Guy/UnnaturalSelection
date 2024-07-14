package com.xemplarsoft.gameutils.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.xemplarsoft.gameutils.screens.ui.bg.Background;

public class ListView<T> extends AbstractComponent {
    protected Array<AbstractComponent> itemViews = new Array<>();
    protected Array<T> items = new Array<>();

    protected int scrollPos;
    protected float itemHeight;
    public Action action;
    protected Background itemBg;
    protected ComponentInflator inflator;

    public ListView(LabelInflator inflator){
        this.inflator = inflator;
    }

    public void addItem(T item){
        items.add(item);
        updateViews();
    }

    public void addItems(T[] items){
        this.items.addAll(items);
        updateViews();
    }

    public void removeIndex(int index){
        this.items.removeIndex(index);
        updateViews();
    }

    public void scroll(int amt){
        scrollPos += amt;
        if(scrollPos < 0) scrollPos = 0;
    }

    public void setInflator(ComponentInflator inflator){
        this.inflator = inflator;
    }

    public void setItemBg(Background bg){
        this.itemBg = bg;
    }

    public void updateViews(){
        itemViews.clear();
        float yOffset = 0;
        for(int i = scrollPos; i < items.size; i++){
            AbstractComponent comp = inflator.inflate(items.get(i), i);
            if(comp == null) continue;

            comp.setBounds(this.x, this.y + height - comp.height - yOffset, comp.width == 0 ? this.width : comp.width, comp.height == 0 ? 1F : comp.height);
            comp.setID(i);
            comp.setBg(itemBg);

            yOffset += comp.height + 0.125F;
            if(yOffset > height) return;
            itemViews.add(comp);
        }
    }

    public void setPos(float x, float y) {
        super.setPos(x, y);
        updateViews();
    }

    public void setAction(Action a){
        this.action = a;
        updateViews();
    }

    public boolean touchUp(float gridX, float gridY, int pointer, int button) {
        if(!visible || !inside(gridX, gridY)) return false;

        for(int i = scrollPos; i < itemViews.size; i++){
            if(itemViews.get(i).inside(gridX, gridY)){
                if(action != null) action.action(this, "up:" + i + ":" + items.get(i).toString());
                return true;
            }
        }

        return super.touchUp(gridX, gridY, pointer, button);
    }

    public void renderComp(SpriteBatch batch) {
        if(!visible) return;

        if(bg != null) bg.render(batch, x, y, width, height);
        for(int i = 0; i < itemViews.size; i++){
            itemViews.get(i).renderComp(batch);
        }
    }

    public void updateComp(float delta) {}
}
