package com.xemplarsoft.nnt.neuro;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public abstract class Node {
    public ArrayMap<Node, Float> inputs;
    public float value = 0F, bias = 0F;
    public final int id;

    public Node(final int id, float bias){
        this.inputs = new ArrayMap<>();
        this.bias = bias;
        this.id = id;
    }

    public void addInput(Node n, float weight){
        inputs.put(n, weight);
    }

    public abstract float getOutput();
    public abstract String getName();
}
