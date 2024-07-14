package com.xemplarsoft.nnt.neuro;

import com.badlogic.gdx.math.MathUtils;
import com.xemplarsoft.nnt.entity.Unit;
import com.xemplarsoft.nnt.World;

import static com.xemplarsoft.nnt.entity.Unit.MAX_HUNGER;
import static com.xemplarsoft.nnt.screens.SimScreen.GRID_SIZE;

public abstract class InputNode extends Node{
    public InputNode(final int id, float bias){
        super(id, bias);
    }

    public abstract void viewEnvironment(Unit self, World environment);

    public final float getOutput() {
        return value + bias;
    }

    public static class XPosSensor extends InputNode{
        public XPosSensor(float bias) { super(0, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = (float) self.x / GRID_SIZE;
        }
        public String getName() { return "PxS"; }
    }
    public static class YPosSensor extends InputNode{
        public YPosSensor(float bias) { super(1, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = (float) self.y / GRID_SIZE;
        }
        public String getName() { return "PyS"; }
    }
    public static class NorthPop extends InputNode{
        public NorthPop(float bias) { super(2, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getUnitCount(self.x, self.y + 1, 1, 10) / 10F;
        }
        public String getName() { return "Pn"; }
    }
    public static class SouthPop extends InputNode{
        public SouthPop(float bias) { super(3, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getUnitCount(self.x, self.y - 11, 1, 10) / 10F;
        }
        public String getName() { return "Ps"; }
    }
    public static class EastPop extends InputNode{
        public EastPop(float bias) { super(4, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getUnitCount(self.x + 1, self.y, 10, 1) / 10F;
        }
        public String getName() { return "Pe"; }
    }
    public static class WestPop extends InputNode{
        public WestPop(float bias) { super(5, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getUnitCount(self.x - 11, self.y, 10, 1) / 10F;
        }
        public String getName() { return "Pw"; }
    }
    public static class RandomTick extends InputNode{
        public RandomTick(float bias) { super(6, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = MathUtils.random();
        }
        public String getName() { return "Rnd"; }
    }
    public static class ComfortSensor extends InputNode{
        public ComfortSensor(float bias) { super(7, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = self.comfort;
        }
        public String getName() { return "Cmf"; }
    }

    public static final int SENSE_RADIUS = 3;
    public static final int SENSE_WIDTH = (2 * SENSE_RADIUS) - 1;
    public static final int FULL_CELLS = (int)Math.pow(SENSE_WIDTH, 2);

    public static class PopDensity extends InputNode{
        public PopDensity(float bias) { super(8, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getUnitCount(self.x - (SENSE_RADIUS - 1), self.y - (SENSE_RADIUS - 1), SENSE_WIDTH, SENSE_WIDTH) / (float)FULL_CELLS;
        }
        public String getName() { return "Pop"; }
    }
    public static class LastMoveX extends InputNode{
        public LastMoveX(float bias) { super(9, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = Math.max(-1, Math.min(1, self.lastX));
        }
        public String getName() { return "LMx"; }
    }
    public static class LastMoveY extends InputNode{
        public LastMoveY(float bias) { super(10, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = Math.max(-1, Math.min(1, self.lastY));
        }
        public String getName() { return "LMy"; }
    }
    public static class BlockForward extends InputNode{
        public BlockForward(float bias) { super(11, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getEntityAt(self.x + self.lastX, self.y + self.lastY) != null ? 1 : 0;
        }
        public String getName() { return "BLf"; }
    }
    public static class BlockReverse extends InputNode{
        public BlockReverse(float bias) { super(12, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getEntityAt(self.x - self.lastX, self.y - self.lastY) != null ? 1 : 0;
        }
        public String getName() { return "BLr"; }
    }
    public static class BlockSide extends InputNode{
        public BlockSide(float bias) { super(13, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = environment.getEntityAt(self.x - self.lastY, self.y + self.lastX) != null ? 1 : environment.getEntityAt(self.x + self.lastY, self.y - self.lastX) == null ? 1 : 0;
        }
        public String getName() { return "BLs"; }
    }
    public static class BorderDist extends InputNode{
        public BorderDist(float bias) { super(14, bias); }
        public void viewEnvironment(Unit self, World environment) {
            float xDist = Math.abs(self.x - (GRID_SIZE / 2)) / (GRID_SIZE / 2F);
            float yDist = Math.abs(self.y - (GRID_SIZE / 2)) / (GRID_SIZE / 2F);
            this.value = (xDist + yDist) / 2F;
        }
        public String getName() { return "BRa"; }
    }
    public static class BorderHZDist extends InputNode{
        public BorderHZDist(float bias) { super(15, bias); }
        public void viewEnvironment(Unit self, World environment) {
            this.value = Math.abs(self.x - (GRID_SIZE / 2)) / (GRID_SIZE / 2F);
        }
        public String getName() { return "BRx"; }
    }
    public static class BorderVTDist extends InputNode{
        public BorderVTDist(float bias) { super(16, bias); }
        public void viewEnvironment(Unit self, World environment) {
            this.value = Math.abs(self.y - (GRID_SIZE / 2)) / (GRID_SIZE / 2F);
        }
        public String getName() { return "BRy"; }
    }
    public static class Oscillator extends InputNode{
        private float time = 0F;
        public Oscillator(float bias) { super(17, bias); }
        public void viewEnvironment(Unit self, World environment) {
            time += 0.06F;
            this.value = (float) (Math.sin(time * Math.PI * 2) + 1F) / 2F;
        }
        public String getName() { return "Osc"; }
    }
    public static class HungerSensor extends InputNode{
        public HungerSensor(float bias) { super(18, bias); }
        public void viewEnvironment(Unit self, World environment) {
            value = self.energy / MAX_HUNGER;
        }
        public String getName() { return "Hng"; }
    }

    public static final int FOOD_SENSE_RADIUS = 4;
    public static class FoodDist extends InputNode{
        public FoodDist(float bias) { super(19, bias); }
        public void viewEnvironment(Unit self, World environment) {
            int off = FOOD_SENSE_RADIUS - 1;
            float dst = Float.MAX_VALUE, calcDst;

            for(int xp = -off; xp < off; xp++) {
                for (int yp = -off; yp < off; yp++) {
                    if (environment.getFoodAt(self.x + xp, self.y + yp) != null){
                        calcDst = (float)Math.sqrt(xp*xp + yp*yp);
                        if(calcDst < dst) {
                            dst = calcDst;
                        }
                    }
                }
            }
            if(dst > FOOD_SENSE_RADIUS){
                value = 0;
            } else {
                value = 1F - (dst / FOOD_SENSE_RADIUS);
            }
        }
        public String getName() { return "FDa"; }
    }
    public static class FoodDistX extends InputNode{
        public FoodDistX(float bias) { super(20, bias); }
        public void viewEnvironment(Unit self, World environment) {
            int off = FOOD_SENSE_RADIUS - 1;
            float dst = Float.MAX_VALUE, calcDst;

            for(int xp = -off; xp < off; xp++) {
                for (int yp = -off; yp < off; yp++) {
                    if (environment.getFoodAt(self.x + xp, self.y + yp) != null){
                        calcDst = (float)xp;
                        if(calcDst < dst) {
                            dst = calcDst;
                        }
                    }
                }
            }
            if(dst > FOOD_SENSE_RADIUS){
                value = 0;
            } else {
                value = 1F - (dst / FOOD_SENSE_RADIUS);
            }
        }
        public String getName() { return "FDx"; }
    }
    public static class FoodDistY extends InputNode{
        public FoodDistY(float bias) { super(21, bias); }
        public void viewEnvironment(Unit self, World environment) {
            int off = FOOD_SENSE_RADIUS - 1;
            float dst = Float.MAX_VALUE, calcDst;

            for(int xp = -off; xp < off; xp++) {
                for (int yp = -off; yp < off; yp++) {
                    if (environment.getFoodAt(self.x + xp, self.y + yp) != null){
                        calcDst = yp;
                        if(calcDst < dst) {
                            dst = calcDst;
                        }
                    }
                }
            }
            if(dst > FOOD_SENSE_RADIUS){
                value = 0;
            } else {
                value = 1F - (dst / FOOD_SENSE_RADIUS);
            }
        }
        public String getName() { return "FDy"; }
    }

    public static class TouchNorth extends InputNode{
        public TouchNorth(float bias) { super(22, bias); }
        public void viewEnvironment(Unit self, World environment) {
            if(self.y + 1 == GRID_SIZE) value = 1F;
            else value = environment.getEntityAt(self.x, self.y + 1) == null ? 0 : 1;
        }
        public String getName() { return "Tn"; }
    }
    public static class TouchSouth extends InputNode{
        public TouchSouth(float bias) { super(23, bias); }
        public void viewEnvironment(Unit self, World environment) {
            if(self.y == 0) value = 1F;
            else value = environment.getEntityAt(self.x, self.y - 1) == null ? 0 : 1;
        }
        public String getName() { return "Ts"; }
    }
    public static class TouchEast extends InputNode{
        public TouchEast(float bias) { super(24, bias); }
        public void viewEnvironment(Unit self, World environment) {
            if(self.x + 1 == GRID_SIZE) value = 1F;
            else value = environment.getEntityAt(self.x + 1, self.y) == null ? 0 : 1;
        }
        public String getName() { return "Te"; }
    }
    public static class TouchWest extends InputNode{
        public TouchWest(float bias) { super(25, bias); }
        public void viewEnvironment(Unit self, World environment) {
            if(self.x == 0) value = 1F;
            else value = environment.getEntityAt(self.x - 1, self.y) == null ? 0 : 1;
        }
        public String getName() { return "Tw"; }
    }

}
