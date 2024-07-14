package com.xemplarsoft.nnt.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.xemplarsoft.nnt.World;
import com.xemplarsoft.nnt.neuro.Network;
import com.xemplarsoft.nnt.neuro.OutputNode;

import java.util.Arrays;

import static com.xemplarsoft.nnt.screens.SimScreen.LIFETIME_DURATION;
import static com.xemplarsoft.nnt.screens.SimScreen.GRID_SIZE;
import static com.xemplarsoft.nnt.World.*;

public class Unit extends Entity {
    public static final float HUNGER_DELTA_PERCENT = 0.2F;
    public static final float COMFORT_DELTA_PERCENT = 0.1F;
    public static final float AGE_DELTA = 0.01F;
    public static final float AGE_DEATH = 20F;

    public static final float AGE_REPRODUCE = 5F;
    public static final float MAX_HUNGER = 3F;

    public static final float ENERGY_REPRODUCE = 0.5F;
    public static final float REPRODUCE_CHANCE = 0.5F;
    public static final float REPRODUCE_COOLDOWN = 1F;
    public static final float STARVING_ENERGY = 0.1F;
    public static final float STARVING_DAMAGE = 0.01F;
    public static final float HUNGER_DEDUCT = 1F / (LIFETIME_DURATION - (LIFETIME_DURATION * HUNGER_DELTA_PERCENT));
    public static final float COMFORT_ADDITION = 1F / (LIFETIME_DURATION - (LIFETIME_DURATION * COMFORT_DELTA_PERCENT));

    private boolean dead = false;
    public float energy = 1.5F, comfort = 0F, health = 1F, age = 0F, reproduce_counter = 0F;

    public int lastX, lastY;
    public boolean noface = false, eaten = false, replicate = false;

    public float sensitivity = 0.5F;

    public Network network;

    private Unit(int x, int y){
        this(x, y, Network.generateGenome());
    }

    private Unit(int x, int y, byte[] dna){
        this.x = x;
        this.y = y;
        this.network = new Network(dna);
        this.color = new Color(Arrays.hashCode(dna) | 0xFF);

        this.lastX = 0;
        this.lastY = 0;
    }

    public void kill(){
        health = 0F;
    }

    private OutputNode currOut;
    public void update(World w){
        //Reset All Net Calc values
        if(energy <= STARVING_ENERGY){
            health -= STARVING_DAMAGE;
        }
        if(age > AGE_DEATH){
            health = 0F;
        }
        age += AGE_DELTA;
        if(energy < 0) return;

        energy -= HUNGER_DEDUCT / 2;

        if(age >= AGE_REPRODUCE && energy > ENERGY_REPRODUCE  && reproduce_counter >= REPRODUCE_COOLDOWN && eaten){
            if(MathUtils.random() >= 1 - (reproduce_counter - REPRODUCE_CHANCE)) {
                if(w.replicate(this)){
                    System.out.println("CHILD BORN FROM: " + x + ", " + y);
                    energy -= ENERGY_REPRODUCE;
                    reproduce_counter = 0;
                }
            }
        }
        reproduce_counter += 0.05F;

        network.update(this, w);

        float chance = MathUtils.random() * (sensitivity + 2F), total = 0, counter = 0;

        if(chance > 1F){
            return;
        }

        boolean moved = false;
        int moveX = 0, moveY = 0;

        for(int i = 0; i < network.outputs.size; i++){
            total += Math.abs(network.outputs.get(i).value);
        }
        for(int i = 0; i < network.outputs.size; i++){
            currOut = network.outputs.get(i);
            counter += Math.abs(currOut.value) / total;
            if(counter >= chance){
                moved = currOut.moved;
                moveX = currOut.moveX;
                moveY = currOut.moveY;

                break;
            }
        }

        if(!moved) return;

        if(moved && (!HUNGER_HALT_MOVE || energy >= HUNGER_MINIMUM_MOVE)) {
            int newPos = (x + moveX) + (y + moveY) * GRID_SIZE;

            if((newPos >= w.field.length) || (newPos < 0)) return;
            if(w.field[newPos] != null) return;

            if (((x > 0 && moveX < 0) || (x < (GRID_SIZE - 1) && moveX > 0) || (moveX == 0 && moveY != 0)) &&
                    ((y > 0 && moveY < 0) || (y < (GRID_SIZE - 1) && moveY > 0) || (moveY == 0 && moveX != 0))) {

                energy -= HUNGER_DEDUCT / 2;

                w.field[x + y * GRID_SIZE] = null;
                x += moveX;
                y += moveY;
                w.field[x + y * GRID_SIZE] = this;

                lastX = moveX;
                lastY = moveY;
            }
        }

        //hunger -= network.nodes.size * 0.0002F;
    }

    public boolean isDead(){
        return health <= 0F;
    }

    public byte[] getDNA(){
        return network.DNA;
    }

    public static Unit spawn(int x, int y){
        return new Unit(x, y);
    }

    public static Unit spawn(int x, int y, byte[] dna){
        return new Unit(x, y, dna);
    }
}
