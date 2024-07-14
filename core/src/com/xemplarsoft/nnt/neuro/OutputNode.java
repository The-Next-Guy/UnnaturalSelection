package com.xemplarsoft.nnt.neuro;

import com.badlogic.gdx.math.MathUtils;
import com.xemplarsoft.nnt.entity.Food;
import com.xemplarsoft.nnt.entity.Unit;
import com.xemplarsoft.nnt.World;

import static com.xemplarsoft.nnt.entity.Unit.HUNGER_DEDUCT;
import static com.xemplarsoft.nnt.entity.Unit.MAX_HUNGER;

public abstract class OutputNode extends Node{
    public int moveX, moveY;
    public boolean moved;

    public OutputNode(int id, float bias) {
        super(id, bias);
    }

    public final void performAction(Unit self, World environment){
        //Reset Output Params;
        moveX = 0;
        moveY = 0;
        moved = false;
        value = getOutput();

        perform(value, self, environment);

    }
    protected abstract void perform(float value, Unit self, World environment);

    public static class Sensitivity extends OutputNode{
        public Sensitivity(float bias) {
            super(0, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            //if(value < 0) return;

            self.sensitivity = (value * 0.5F) + 0.5F;
        }

        public String getName() {
            return "Sen";
        }
    }

    public static class MoveX extends OutputNode{
        public MoveX(float bias) {
            super(1, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = Math.round(Math.abs(value)/value);
            moveY = 0;
            moved = true;
        }

        public String getName() {
            return "Mx";
        }
    }

    public static class MoveY extends OutputNode{
        public MoveY(float bias) {
            super(2, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = 0;
            moveY = Math.round(Math.abs(value)/value);
            moved = true;
        }

        public String getName() {
            return "My";
        }
    }

    public static class MoveE extends OutputNode{
        public MoveE(float bias) {
            super(3, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = 1;
            moveY = 0;
            moved = true;
        }

        public String getName() {
            return "Me";
        }
    }

    public static class MoveW extends OutputNode{
        public MoveW(float bias) {
            super(4, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = -1;
            moveY = 0;
            moved = true;
        }

        public String getName() {
            return "Mw";
        }
    }

    public static class MoveN extends OutputNode{
        public MoveN(float bias) {
            super(5, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = 0;
            moveY = 1;
            moved = true;
        }

        public String getName() {
            return "Mn";
        }
    }

    public static class MoveS extends OutputNode{
        public MoveS(float bias) {
            super(6, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = 0;
            moveY = -1;
            moved = true;
        }

        public String getName() {
            return "Ms";
        }
    }

    public static class MoveRandom extends OutputNode{
        public MoveRandom(float bias) {
            super(7, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = MathUtils.random(2) - 1;
            moveY = MathUtils.random(2) - 1;
            moved = true;
        }

        public String getName() {
            return "MRn";
        }
    }

    public static class MoveForward extends OutputNode{
        public MoveForward(float bias) {
            super(8, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = self.lastX;
            moveY = self.lastY;
            moved = true;
        }

        public String getName() {
            return "MFw";
        }
    }

    public static class MoveSideways extends OutputNode{
        public MoveSideways(float bias) {
            super(9, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            moveX = value > 0 ? (-self.lastY) : (self.lastY);
            moveY = value > 0 ? (self.lastX) : (-self.lastX);
            moved = true;
        }

        public String getName() {
            return "MFp";
        }
    }

    public static class EatFood extends OutputNode{
        public EatFood(float bias) {
            super(10, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            if(Math.abs(value) < 0.5F) return;

            Food select = null;
            for(int x = -1; x <= 1; x++){
                for(int y = -1; y <= 1; y++){
                    select = environment.getFoodAt(self.x + x, self.y + y);
                    if(select != null && select.obtainFood() && self.energy + select.foodFullness() <= MAX_HUNGER){
                        self.energy += select.foodFullness();
                        self.eaten = true;

                        return;
                    }
                }
            }
            if(select == null){
                self.energy -= HUNGER_DEDUCT;
            }
        }

        public String getName() {
            return "Eat";
        }
    }

    public static class AttackForward extends OutputNode{
        public AttackForward(float bias) {
            super(11, bias);
        }

        protected void perform(float value, Unit self, World environment) {
            if(Math.abs(value) < 0.1F) return;

            Unit select = environment.getUnitAt(self.x + self.lastX, self.y + self.lastY);

            if(select == null){
                self.energy -= HUNGER_DEDUCT;
            } else {
                select.health -= 0.1F;
            }
        }

        public String getName() {
            return "Att";
        }
    }

    public float getOutput() {
        float ins = 0F;
        Node k;
        for(int i = 0; i < inputs.size; i++){
            k = inputs.getKeyAt(i);
            if(k == this) {
                ins += value * inputs.get(k, 0F);
            } else {
                ins += k.value * inputs.get(k, 0F);
            }
        }
        return (float) Math.tanh(ins);
    }
}
