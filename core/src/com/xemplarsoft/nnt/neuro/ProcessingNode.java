package com.xemplarsoft.nnt.neuro;

public class ProcessingNode extends Node{
    public ProcessingNode(int id, float bias) {
        super(id, bias);
        value = 0F;
    }

    public void process(){
        value = getOutput();
    }

    public float getOutput() {
        float ins = 0F;
        for(int i = 0; i < inputs.size; i++){
            Node k = inputs.getKeyAt(i);
            if(k == this) {
                ins += value * inputs.get(k, 0F);
            } else {
                ins += k.value * inputs.get(k, 0F);
            }
        }
        //System.out.println("Added Up: " + ins + ", inputs: " + inputs.size);
        return (float) Math.tanh(ins);
    }

    public String getName() {
        return "P" + id;
    }
}
