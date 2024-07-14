package com.xemplarsoft.nnt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.xemplarsoft.nnt.entity.Barrier;
import com.xemplarsoft.nnt.entity.Entity;
import com.xemplarsoft.nnt.entity.Food;
import com.xemplarsoft.nnt.entity.Unit;
import com.xemplarsoft.nnt.neuro.Network;

import static com.xemplarsoft.nnt.screens.SimScreen.*;

public class World {
    public static final boolean HUNGER_HALT_MOVE = true;
    public static final float HUNGER_MINIMUM_MOVE = 0.0F;

    public static final int POPULATION = 100, INIT_FOOD = 1000;

    public Array<Shape2D> shelterRegions = new Array<>();
    //new Circle(GRID_SIZE / 2F, GRID_SIZE / 2F, GRID_SIZE / 5F);

    public Unit selectedUnit;
    public Entity[] field;
    public Array<Entity> entities;
    public Array<Unit> units, removeDead;
    public Array<Food> foods;

    public int birthCount = 0;
    public float TICK_COUNTER = 0F;
    public boolean barriersNextGen, foodNextGen = true;

    public World(){
        field = new Entity[GRID_SIZE * GRID_SIZE];

        entities = new Array<>();
        foods = new Array<>();
        units = new Array<>();
        removeDead = new Array<>();
        resetWorld();
    }

    public void reset(){
        entities.clear();
        foods.clear();

        if(barriersNextGen) addBarriers();
        if(foodNextGen) addFoods();
    }

    public void resetWorld(){
        selectedUnit = null;
        units.clear();
        reset();

        int ranX, ranY;
        Unit u;

        for(int i = 0; i < POPULATION; i++){
            ranX = getRandomXLoc();
            ranY = getRandomYLoc();

            if(getEntityAt(ranX, ranY) != null) {
                i--;
            } else {
                u = Unit.spawn(ranX, ranY);
                units.add(u);
                entities.add(u);
            }
        }

        initField();
    }
    public void evolve(byte[] baseDNA){
        reset();
        units.clear();

        for(int i = 0; i < 4; i++){
            units.add(Unit.spawn(0, 0, baseDNA));
        }

        byte[] half1, half2;
        int surv1, surv2, ranX, ranY;
        boolean occupied = false;
        Unit child;

        while(units.size < POPULATION){
            surv1 = MathUtils.random(units.size - 1);
            surv2 = MathUtils.random(units.size - 1);

            if(surv1 == surv2) continue; //Cannot mate with self :p

            half1 = Network.splitDNA(units.get(surv1).getDNA(), true);
            half2 = Network.splitDNA(units.get(surv2).getDNA(), false);

            ranX = 0;
            ranY = 0;
            occupied = true;
            while (occupied){
                ranX = getRandomXLoc();
                ranY = getRandomYLoc();
                occupied = getEntityAt(ranX, ranY) != null;
            }

            child = Unit.spawn(ranX, ranY, Network.combineDNA(half1, half2, 0.01F));
            units.add(child);
            entities.add(child);
        }
    }

    private void initField(){
        for(int i = 0; i < field.length; i++){
            field[i] = null;
        }
        Entity e;
        int index;
        for(int i = 0; i < entities.size; i++){
            e = entities.get(i);
            index = e.x + e.y * GRID_SIZE;

            if(index >= field.length || index < 0){
                entities.removeIndex(i);
                i--;
                continue;
            }

            field[e.x + e.y * GRID_SIZE] = e;
        }
    }

    public boolean update(float delta){
        TICK_COUNTER += delta;
        if(TICK_COUNTER < TICK_SPEED){
            return false;
        }
        TICK_COUNTER = Math.max(TICK_COUNTER - TICK_SPEED, 0F);

        removeDead.clear();
        for(Unit u : units){
            u.update(this); //No delta because everything is stepwise
            if(unitSheltered(u)) u.comfort = Math.min(u.comfort + Unit.COMFORT_ADDITION, 1F);
            if(u.isDead()){
                removeDead.add(u);
            }
        }
        for(int i = 0; i < foods.size; i++){
            Food f = foods.get(i);
            if(f.empty()) {
                field[f.x + f.y * GRID_SIZE] = null;
                entities.removeValue(f, true);
                foods.removeIndex(i);
                i--;
            }
        }
        if (foods.size < INIT_FOOD){
            regenerateFood();
        }

        for(Unit dead : removeDead){
            field[dead.x + dead.y * GRID_SIZE] = null;
            entities.removeValue(dead, true);
            units.removeValue(dead, true);
        }

        return true;
    }

    public void render(ShapeRenderer shape){
        for(Entity e : entities){
            shape.setColor(e.color);
            shape.rect((float) e.x / GRID_SIZE * WORLD_HEIGHT, (float) e.y / GRID_SIZE * WORLD_HEIGHT, WORLD_HEIGHT / GRID_SIZE, WORLD_HEIGHT / GRID_SIZE);
            //shape.circle((float) (e.x + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (float) (e.y + 0.5F) / GRID_SIZE * WORLD_HEIGHT, WORLD_HEIGHT / GRID_SIZE / 2, 8);
        }
    }

    public int getRandomXLoc(){
        return MathUtils.random(GRID_SIZE - 1);
    }

    public int getRandomYLoc(){
        return MathUtils.random(GRID_SIZE - 1);
    }

    public int getRandomXLocFood(){
        return (int)(MathUtils.cos(MathUtils.PI2 * MathUtils.random()) * GRID_SIZE);
    }

    public int getRandomYLocFood(){
        return (int)(MathUtils.sin(MathUtils.PI2 * MathUtils.random()) * GRID_SIZE);
    }

    private final Vector2 randomPoint = new Vector2();
    public Vector2 getRandomPointInCircle(){
        float rads = MathUtils.PI2 * MathUtils.random();
        randomPoint.set(MathUtils.cos(rads), MathUtils.sin(rads));
        return randomPoint;
    }
    public Vector2 getFoodPointInCircle(){
        float radius = MathUtils.random();
        return getRandomPointInCircle().scl(radius).add(1, 1).scl(0.5F);
    }

    public void regenerateFood(){
        if(MathUtils.random() > 0.25) return;

        getFoodPointInCircle();
        setFood((int)(randomPoint.x * GRID_SIZE), (int)(randomPoint.y * GRID_SIZE), 1);
    }

    public void addFoods(){
        for(int i = 0; i < INIT_FOOD; i++){
            getFoodPointInCircle();
            setFood((int)(randomPoint.x * GRID_SIZE), (int)(randomPoint.y * GRID_SIZE), 1);
        }
    }

    public void addBarriers(){
        int spacing = -2;

        fillBarrier(GRID_SIZE / 2, (GRID_SIZE / 3) + spacing, 2, (GRID_SIZE / 3) - (spacing * 2));

        fillBarrier(GRID_SIZE / 4, spacing, 2, (GRID_SIZE / 3) - (spacing * 2));
        fillBarrier(GRID_SIZE / 4, (GRID_SIZE * 2/3) + spacing, 2, (GRID_SIZE / 3) - (spacing * 2));

        fillBarrier(GRID_SIZE * 3/4, spacing, 2, (GRID_SIZE / 3) - (spacing * 2));
        fillBarrier(GRID_SIZE * 3/4, (GRID_SIZE * 2/3) + spacing, 2, (GRID_SIZE / 3) - (spacing * 2));

        initField();
    }

    public void fillBarrier(int x, int y, int w, int h){
        for(int xp = 0; xp < w; xp++){
            for(int yp = 0; yp < h; yp++){
                entities.add(new Barrier(x + xp, y + yp));
            }
        }
    }

    public void setFood(int x, int y, int qtyLeft){
        Food create = new Food(x, y, "", new Color(0, .8F, .2F, 1F), qtyLeft);
        entities.add(create);
        foods.add(create);
    }

    public void fillFood(int x, int y, int w, int h, int qtyLeft){
        Food create;
        for(int xp = 0; xp < w; xp++){
            for(int yp = 0; yp < h; yp++){
                create = new Food(x + xp, y + yp, "", new Color(0, .8F, .2F, 1F), qtyLeft);
                entities.add(create);
                foods.add(create);
            }
        }
    }

    public boolean replicate(Unit parent){
        int nX = parent.x + parent.lastX;
        int nY = parent.y + parent.lastY;
        if(nX + nY * GRID_SIZE >= field.length || nX + nY * GRID_SIZE < 0) return false;

        if(field[nX + nY * GRID_SIZE] != null) return false;

        Unit child = Unit.spawn(parent.x + parent.lastX, parent.y + parent.lastY, Network.mutateDNA(parent.getDNA(), 0.05F));
        entities.add(child);
        units.add(child);
        field[nX + nY * GRID_SIZE] = child;
        return true;
    }

    public void setShelterRegions(int num){
        shelterRegions.clear();

        switch (num){
            default:
            case 0:
                shelterRegions.add(new Rectangle(GRID_SIZE / 2, 0, GRID_SIZE / 2, GRID_SIZE));
                break;
            case 1:
                shelterRegions.add(new Rectangle(0, 0, GRID_SIZE / 8, GRID_SIZE));
                shelterRegions.add(new Rectangle(GRID_SIZE * 7/8, 0, GRID_SIZE / 8, GRID_SIZE));
                break;
            case 2:
                shelterRegions.add(new Rectangle(0, 0, GRID_SIZE, GRID_SIZE / 8));
                shelterRegions.add(new Rectangle(0, GRID_SIZE * 7/8, GRID_SIZE, GRID_SIZE / 8));
                break;
            case 3:
                shelterRegions.add(new Circle(GRID_SIZE / 2F, GRID_SIZE / 2F, GRID_SIZE / 5F));
                break;
            case 4:
                shelterRegions.add(new Circle(0, 0, GRID_SIZE / 6F));
                shelterRegions.add(new Circle(GRID_SIZE, 0, GRID_SIZE / 6F));
                shelterRegions.add(new Circle(GRID_SIZE, GRID_SIZE, GRID_SIZE / 6F));
                shelterRegions.add(new Circle(0, GRID_SIZE, GRID_SIZE / 6F));
        }
    }

    public boolean unitSheltered(Unit u){
        for (Shape2D surv : shelterRegions) {
            if (surv.contains(u.x, u.y)) return true;
        }
        return false;
    }

    public Entity getEntityAt(int x, int y){
        if(x + y * GRID_SIZE >= GRID_SIZE * GRID_SIZE) return null;
        if(x + y * GRID_SIZE < 0) return null;

        return field[x + y * GRID_SIZE];
    }

    public Unit getUnitAt(int x, int y){
        Entity e = getEntityAt(x, y);
        if(e instanceof Unit) return (Unit) e;
        return null;
    }

    public int getUnitCount(int x, int y, int width, int height){
        int ret = 0;
        for(int xp = 0; xp < width; xp++) {
            for (int yp = 0; yp < height; yp++) {
                if (getUnitAt(x + xp, y + yp) != null) ret++;
            }
        }
        return ret;
    }

    public Food getFoodAt(int x, int y){
        Food u;
        for(int i = 0; i < foods.size; i++){
            u = foods.get(i);
            if(u.x == x && u.y == y) return u;
        }

        return null;
    }

    public void setBarriersNextGen(boolean b) {
        this.barriersNextGen = b;
    }
}
