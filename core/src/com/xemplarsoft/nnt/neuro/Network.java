package com.xemplarsoft.nnt.neuro;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.xemplarsoft.nnt.entity.Unit;
import com.xemplarsoft.nnt.World;

import java.sql.Struct;

public class Network {
    public int[] processOrder;
    public Array<Node> nodes;
    public Array<OutputNode> outputs;
    public byte[] DNA;

    public Network(){
        this(generateGenome());
    }

    //Creation Parameters
    public static final int CHROMO1_GENES = 4;
    public static final int CHROMO2_GENES = 30;

    public static final int CHROMO2_DOMEX = 18;

    public static final int CHROMO1_GENE_LEN = 4;
    public static final int CHROMO2_GENE_LEN = 6;

    public static final int MAX_PROCESSING = 5;
    public static final int PROCESS_OPTION_COUNT = 8;

    public static final int OUTPUT_OPTION_COUNT = 12;
    public static final int INPUT_OPTION_COUNT = 26;

    public Network(byte[] dna){
        DNA = dna;
        outputs = new Array<>();
        nodes = parseDNA(dna);
        processOrder = new int[nodes.size];
        int counter = 0;

        Node n;
        for(int i = 0; i < nodes.size; i++){
            n = nodes.get(i);
            if(n instanceof InputNode){
                processOrder[counter] = i;
                counter++;
            }
        }

        //Cycle through all processing nodes
        for(int i = 0; i < nodes.size; i++){
            n = nodes.get(i);
            if(n instanceof ProcessingNode){
                processOrder[counter] = i;
                counter++;
            }
        }

        //Cycle through all output nodes
        outputs.clear();
        for(int i = 0; i < nodes.size; i++){
            n = nodes.get(i);
            if(n instanceof OutputNode){
                outputs.add((OutputNode) n);
                processOrder[counter] = i;
                counter++;
            }
        }
    }


    public void update(Unit self, World environment){
        //Update all input nodes
        Node n;
        for(int i = 0; i < nodes.size; i++){
            n = nodes.get(processOrder[i]);

            if(n instanceof InputNode){
                ((InputNode) n).viewEnvironment(self, environment);
            }
            if(n instanceof ProcessingNode){
                ((ProcessingNode) n).process();
            }
            if(n instanceof OutputNode){
                ((OutputNode) n).performAction(self, environment);
            }
        }
    }

    public boolean haveID(int id){
        for(int i = 0; i < nodes.size; i++){
            if(nodes.get(i).id == id) return true;
        }
        return false;
    }

    /**
     * The genome of the units will be described in pair values.<br>
     * <p>
     * <h3>NODE SELECTION (Chromosome 1)</h3>
     * 4 bytes are provided. The first two represent an ID. If one byte is a valid
     * ID, the neuron is usable. If both are valid and different, or both are invalid,
     * no neuron is enabled. If both are the same, then the neuron is enabled. The
     * second two bytes determine the bias of the neuron. The first byte is subtracted
     * from the second byte to determine bias.
     * </p><br>
     * <p>
     * <h3>CONNECTIONS (Chromosome 2)</h3>
     * A connection is made up of 8 bytes. The first two are the selectable IDs for
     * the sender, and the second two are for the receiver. Both sets are selected
     * in the same way as node selection. The third set represent the connection
     * strength, and the last set determine gene dominance. Dominance determines which
     * gene gets represented in the unit.
     *
     * </p>
     * @return final genome
     */
    public static final byte[] generateGenome(){
        Array<Byte> genome = new Array<>();

        //Chromo 1
        for(int i = 0; i < CHROMO1_GENES; i++){
            byte id1 = (byte)MathUtils.random(255);
            byte id2 = (byte)MathUtils.random(255);

            byte weightA = (byte)MathUtils.random(255);
            byte weightB = (byte)MathUtils.random(255);

            genome.add(id1);
            genome.add(id2);

            genome.add(weightA);
            genome.add(weightB);
        }

        byte txType, txID, rxType, rxID, weightH, weightL;
        for(int i = 0; i < CHROMO2_GENES; i++){
            txType = (byte)MathUtils.random(255);
            txID = (byte)MathUtils.random(255);

            rxType = (byte)MathUtils.random(255);
            rxID = (byte)MathUtils.random(255);

            weightH = (byte)MathUtils.random(255);
            weightL = (byte)MathUtils.random(255);

            genome.add(txType);
            genome.add(txID);

            genome.add(rxType);
            genome.add(rxID);

            genome.add(weightH);
            genome.add(weightL);
        }

        byte[] ret = new byte[genome.size];
        for(int i = 0; i < genome.size; i++){
            ret[i] = genome.get(i);
        }
        return ret;
    }

    public Node getNodeByID(Class<? extends Node> clazz, int id){
        Node curr;
        for(int i = 0; i < nodes.size; i++){
            curr = nodes.get(i);
            if(curr.getClass() == clazz && curr.id == id) return nodes.get(i);
        }
        return null;
    }
    public void printNetwork(){
        System.out.print("Neural Network: " + nodes.size + " Active Nodes\n  Input Nodes:");
        for(int i = 0; i < nodes.size; i++){
            Node curr = nodes.get(i);
            if(curr instanceof InputNode){
                System.out.print(curr.getName() + " ");
            }
        }
        System.out.print("\n  Processing Nodes: ");
        for(int i = 0; i < nodes.size; i++){
            Node curr = nodes.get(i);
            if(curr instanceof ProcessingNode && !(curr instanceof OutputNode)){
                System.out.print(curr.getName() + " ");
            }
        }
        System.out.print("\n  Output Nodes: ");
        for(int i = 0; i < nodes.size; i++){
            Node curr = nodes.get(i);
            if(curr instanceof OutputNode){
                System.out.print(curr.getName() + " ");
            }
        }

        System.out.println("\n\nConnections: ");
        Array<Node> used = new Array<>();
        Array<Node> checked = new Array<>();

        for(int i = 0; i < nodes.size; i++){
            Node curr = nodes.get(i);

            if(curr instanceof OutputNode){
                used.add(curr);
            }
        }

        int usedIndex = 0;
        while(used.size != checked.size){
            Node curr = used.get(usedIndex);
            int inputCount = curr.inputs.size;

            if(inputCount == 0 && !(curr instanceof InputNode) && !(curr instanceof ProcessingNode)){
                used.removeIndex(usedIndex);
                //Don't increment the index counter, next element will fall down.
                continue;
            }

            System.out.println("  " + curr.getName() + ": ");
            for(int i = 0; i < inputCount; i++){
                Node input = curr.inputs.getKeyAt(i);
                System.out.println("    " + input.getName() + ": " + curr.inputs.getValueAt(i));
                if(!used.contains(input, false)){
                    used.add(input);
                }
            }
            checked.add(curr);
            usedIndex++;
        }
    }

    private static Gene parseGene(int chromo, byte[] geneDNA){
        switch (chromo){
            case 1:
            default:
                return new Chromo1Gene(1, geneDNA);
            case 2:
                return new Chromo2Gene(2, geneDNA);
        }
    }

    private static Array<Node> parseDNA(byte[] dna){
        Array<Node> nodes = new Array<>();

        nodes.add(new InputNode.XPosSensor(1F));
        nodes.add(new InputNode.YPosSensor(1F));
        nodes.add(new InputNode.NorthPop(1F));
        nodes.add(new InputNode.SouthPop(1F));
        nodes.add(new InputNode.EastPop(1F));
        nodes.add(new InputNode.WestPop(1F));
        nodes.add(new InputNode.RandomTick(1F));
        nodes.add(new InputNode.HungerSensor(1F));
        nodes.add(new InputNode.ComfortSensor(1F));
        nodes.add(new InputNode.PopDensity(1F));
        nodes.add(new InputNode.LastMoveX(1F));
        nodes.add(new InputNode.LastMoveY(1F));
        nodes.add(new InputNode.BlockForward(1F));
        nodes.add(new InputNode.BlockReverse(1F));
        nodes.add(new InputNode.BlockSide(1F));
        nodes.add(new InputNode.BorderDist(1F));
        nodes.add(new InputNode.BorderHZDist(1F));
        nodes.add(new InputNode.BorderVTDist(1F));
        nodes.add(new InputNode.Oscillator(1F));
        nodes.add(new InputNode.FoodDist(1F));
        nodes.add(new InputNode.FoodDistY(1F));
        nodes.add(new InputNode.FoodDistX(1F));
        nodes.add(new InputNode.TouchNorth(1F));
        nodes.add(new InputNode.TouchSouth(1F));
        nodes.add(new InputNode.TouchEast(1F));
        nodes.add(new InputNode.TouchWest(1F));

        for(int i = 0; i < MAX_PROCESSING; i++){
            nodes.add(new ProcessingNode(i, 0F));
        }

        nodes.add(new OutputNode.MoveX(1F));
        nodes.add(new OutputNode.MoveY(1F));
        nodes.add(new OutputNode.MoveRandom(1F));
        nodes.add(new OutputNode.MoveForward(1F));
        nodes.add(new OutputNode.MoveSideways(1F));
        nodes.add(new OutputNode.Sensitivity(1F));
        nodes.add(new OutputNode.MoveN(1F));
        nodes.add(new OutputNode.MoveS(1F));
        nodes.add(new OutputNode.MoveE(1F));
        nodes.add(new OutputNode.MoveW(1F));
        nodes.add(new OutputNode.EatFood(1F));
        nodes.add(new OutputNode.AttackForward(1F));

        Chromo2Gene chromo2 = new Chromo2Gene(2);
        byte[] geneDNA = new byte[CHROMO2_GENE_LEN];

        int dnaIndex = (CHROMO1_GENES * CHROMO1_GENE_LEN);
        Node tx, rx, curr;

        for(int i = 0; i < CHROMO2_GENES; i++) {
            tx = null;
            rx = null;

            for(int j = 0; j < CHROMO2_GENE_LEN; j++){
                geneDNA[j] = dna[dnaIndex + j];
            }
            chromo2.setDNA(geneDNA);

            if(chromo2.domN > chromo2.domP) {
                dnaIndex += CHROMO2_GENE_LEN;
                continue;
            }

            for (int j = 0; j < nodes.size; j++) {
                curr = nodes.get(j);
                if ((curr.id == chromo2.txID && chromo2.txType == 0 && curr instanceof InputNode) ||
                    (curr.id == chromo2.txID && chromo2.txType == 1 && curr instanceof ProcessingNode)) {
                    tx = curr;
                }
                if ((curr.id == chromo2.rxID && chromo2.rxType == 0 && curr instanceof ProcessingNode) ||
                    (curr.id == chromo2.rxID && chromo2.rxType == 1 && curr instanceof OutputNode)) {
                    rx = curr;
                }
                if (tx != null && rx != null) break;
            }
            //System.out.println((tx == null ? "!" : "") + txType + ":" + txID + " --> " + (rx == null ? "!" : "") + rxType + ":" + rxID);
            //System.out.println();

            //if(tx instanceof InputNode && rx instanceof OutputNode) continue;

            if (tx != null && rx != null) {
                rx.addInput(tx, chromo2.weightF);
            }

            dnaIndex += CHROMO2_GENE_LEN;
        }

        return purgeNullBranchesV2(nodes);
    }

    public static Array<Node> purgeNullBranchesV2(Array<Node> nodes){
        Array<Node> outUsed = new Array<>();
        Array<Node> ret = new Array<>();

        Node curr, input;
        for(int i = 0; i < nodes.size; i++){
            curr = nodes.get(i);
            if(curr instanceof OutputNode) outUsed.add(curr);
        }

        for(int i = 0; i < outUsed.size; i++){
            curr = outUsed.get(i);
            if(curr.inputs.size == 0) continue;

            ret.add(curr);
            for(int j = 0; j < curr.inputs.size; j++){
                input = curr.inputs.getKeyAt(j);
                if(!ret.contains(input, true)) ret.add(input);
            }
        }
        int i = 0;
        while(i < ret.size){
            curr = ret.get(i);

            for(int j = 0; j < curr.inputs.size; j++){
                input = curr.inputs.getKeyAt(j);
                if(!ret.contains(input, true)) ret.add(input);
            }
            i++;
        }

        return ret;
    }
    public static Array<Node> purgeNullBranches(Array<Node> nodes){
        Array<Node> used = new Array<>();
        Array<Node> checked = new Array<>();

        for(int i = 0; i < nodes.size; i++){
            Node curr = nodes.get(i);

            if(curr instanceof OutputNode){
                used.add(curr);
            }
        }

        int usedIndex = 0;
        while(used.size != checked.size){
            Node curr = used.get(usedIndex);
            int inputCount = curr.inputs.size;

            if(inputCount == 0 && (curr instanceof OutputNode)){
                used.removeIndex(usedIndex);
                //Don't increment the index counter, next element will fall down.
                continue;
            }

            for(int i = 0; i < inputCount; i++){
                Node input = curr.inputs.getKeyAt(i);
                if(!used.contains(input, false)){
                    used.add(input);
                }
            }
            checked.add(curr);
            usedIndex++;
        }

        nodes.clear();
        nodes = used;

        return nodes;
    }

    public static boolean isValidID(int type, int id){
        if(type == 0){
            if(id < INPUT_OPTION_COUNT) return true;
        }
        if(type == 1){
            if(id < OUTPUT_OPTION_COUNT) return true;
        }

        return false;
    }
    public static boolean isValidConnection(int tx, int rx){
        int txType = tx / 85;
        int rxType = rx / 85;

        if(txType == 0 && rxType > 0) return true;
        if(txType == 1 && rxType > 0) return true;

        return false;
    }

    public static Node getNodeByID(Array<Node> nodes, int id){
        for(int i = 0; i < nodes.size; i++){
            if(nodes.get(i).id == id) return nodes.get(i);
        }
        return null;
    }

    public static byte[] splitDNA(byte[] full, boolean male){
        byte[] half = new byte[full.length / 2];

        for (int i = (male ? 0 : 1); i < CHROMO1_GENES; i+=2){
            for(int j = 0; j < CHROMO1_GENE_LEN; j++){
                half[i / 2 * CHROMO1_GENE_LEN + j] = full[i * CHROMO1_GENE_LEN + j];
            }
        }

        int fullOff = CHROMO1_GENES * CHROMO1_GENE_LEN;
        int halfOff = fullOff / 2;

        for (int i = (male ? 0 : 1); i < CHROMO2_GENES; i+=2){
            for(int j = 0; j < CHROMO2_GENE_LEN; j++){
                half[i / 2 * CHROMO2_GENE_LEN + j + halfOff] = full[i * CHROMO2_GENE_LEN + j + fullOff];
            }
        }

        return half;
    }
    public static byte[] combineDNA(byte[] half1, byte[] half2, float mutation){
        byte[] ret = new byte[half1.length * 2];
        int genePos, secondPos, offsetDNA, offsetRNA;

        for(int i = 0; i < (CHROMO1_GENES / 2); i++){
            genePos = i * CHROMO1_GENE_LEN;
            secondPos = (i + CHROMO1_GENES / 2) * CHROMO1_GENE_LEN;

            for(int j = 0; j < CHROMO1_GENE_LEN; j++) {
                ret[genePos + j] = half1[genePos + j];
                ret[secondPos + j] = half2[genePos + j];
            }
        }
        offsetDNA = CHROMO1_GENES * CHROMO1_GENE_LEN;
        offsetRNA = offsetDNA / 2;

        for(int i = 0; i < (CHROMO2_GENES / 2); i++){
            genePos = i * CHROMO2_GENE_LEN;
            secondPos = (i + CHROMO2_GENES / 2) * CHROMO2_GENE_LEN;

            for(int j = 0; j < CHROMO2_GENE_LEN; j++) {
                ret[offsetDNA + genePos + j] = half1[offsetRNA + genePos + j];
                ret[offsetDNA + secondPos + j] = half2[offsetRNA + genePos + j];
            }
        }
        return mutateDNA(ret, mutation);
    }
    public static byte[] mutateDNA(byte[] dna, float mutation){
        byte[] ret = new byte[dna.length];
        boolean mutate, m_flip;
        int genePos, offsetDNA, m_pos, bi, by, b;

        for(int i = 0; i < CHROMO1_GENES; i++){
            mutate = MathUtils.random() < mutation;
            m_flip = MathUtils.randomBoolean();
            m_pos = MathUtils.random(CHROMO1_GENE_LEN * 8 - 1);

            genePos = i * CHROMO1_GENE_LEN;

            for(int j = 0; j < CHROMO1_GENE_LEN; j++) {
                ret[genePos + j] = dna[genePos + j];
            }

            if(mutate){
                by = m_pos / 8;
                bi = m_pos % 8;

                ret[genePos + by] = (byte)(ret[genePos + by] ^ (byte)(1 << bi));
                if(m_flip){
                    b = ret[genePos + by];
                    b = (b & 0xF0) >> 4 | (b & 0x0F) << 4;
                    b = (b & 0xCC) >> 2 | (b & 0x33) << 2;
                    b = (b & 0xAA) >> 1 | (b & 0x55) << 1;
                    ret[genePos + by] = (byte)b;
                }
            }
        }
        offsetDNA = CHROMO1_GENES * CHROMO1_GENE_LEN;

        for(int i = 0; i < CHROMO2_GENES; i++){
            mutate = MathUtils.random() < mutation;
            m_flip = MathUtils.randomBoolean();
            m_pos = MathUtils.random(CHROMO2_GENE_LEN * 8 - 1);

            genePos = i * CHROMO2_GENE_LEN;

            for(int j = 0; j < CHROMO2_GENE_LEN; j++) {
                ret[offsetDNA + genePos + j] = dna[offsetDNA + genePos + j];
            }

            if(mutate){
                by = m_pos / 8;
                bi = m_pos % 8;

                if(m_flip){
                    b = ret[genePos + by];
                    b = (b & 0xF0) >> 4 | (b & 0x0F) << 4;
                    b = (b & 0xCC) >> 2 | (b & 0x33) << 2;
                    b = (b & 0xAA) >> 1 | (b & 0x55) << 1;
                    ret[genePos + by] = (byte)b;
                }
                ret[offsetDNA + genePos + by] = (byte)(ret[offsetDNA + genePos + by] ^ (byte)(1 << bi));
            }
        }
        return ret;
    }

    public String toDNAString(){
        int groupSize = 4;

        StringBuilder builder = new StringBuilder();
        int dnaIndex = 0;
        for(int i = 0; i < CHROMO1_GENES; i++){
            for(int j = 0; j < CHROMO1_GENE_LEN; j++){
                builder.append(toHex(DNA[dnaIndex + j]));
            }
            if(i % groupSize == (groupSize - 1)) builder.append("\n");
            else builder.append(" ");
            dnaIndex+=CHROMO1_GENE_LEN;
        }
        if(builder.charAt(builder.length - 1) == ' ') builder.setLength(builder.length - 1);
        if(builder.charAt(builder.length - 1) == '\n') builder.setLength(builder.length - 1);

        builder.append("\n\n");
        for(int i = 0; i < CHROMO2_GENES; i++){
            for(int j = 0; j < CHROMO2_GENE_LEN; j++){
                builder.append(toHex(DNA[dnaIndex + j]));
            }

            if(i % groupSize == (groupSize - 1)) builder.append("\n");
            else builder.append(" ");
            dnaIndex+=CHROMO2_GENE_LEN;
        }
        if(builder.charAt(builder.length - 1) == ' ') builder.setLength(builder.length - 1);
        if(builder.charAt(builder.length - 1) == '\n') builder.setLength(builder.length - 1);

        return builder.toString();
    }
    public String toDNADetailString(){
        int groupSize = 4;
        Chromo1Gene c1 = new Chromo1Gene(1, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        Chromo2Gene c2 = new Chromo2Gene(2);

        StringBuilder builder = new StringBuilder();
        int dnaIndex = 0;
        for(int i = 0; i < CHROMO1_GENES; i++){
            for(int j = 0; j < CHROMO1_GENE_LEN; j++){
                builder.append(toHex(DNA[dnaIndex + j]));
            }
            //Add chromo 1 back in
            builder.append("\n");
            dnaIndex+=CHROMO1_GENE_LEN;
        }
        if(builder.charAt(builder.length - 1) == ' ') builder.setLength(builder.length - 1);
        if(builder.charAt(builder.length - 1) == '\n') builder.setLength(builder.length - 1);

        builder.append("\n\n");
        byte[] gene = new byte[CHROMO2_GENE_LEN];
        for(int i = 0; i < CHROMO2_GENES; i++){

            for(int j = 0; j < CHROMO2_GENE_LEN; j++){
                gene[j] = DNA[dnaIndex + j];
                builder.append(toHex(gene[j]));
            }
            //
            c2.setDNA(gene);
            builder.append(": ").append(c2.printFunction(nodes));

            builder.append("\n");
            dnaIndex+=CHROMO2_GENE_LEN;
        }
        if(builder.charAt(builder.length - 1) == ' ') builder.setLength(builder.length - 1);
        if(builder.charAt(builder.length - 1) == '\n') builder.setLength(builder.length - 1);

        return builder.toString();
    }

    public String toHex(byte b){
        int data = b & 0xFF;
        String ret = Integer.toHexString(data);
        return ret.length() == 1 ? "0" + ret : ret;
    }

    public abstract static class Gene{
        public final int chromo;
        public byte[] dna;
        public Gene(int chromo, byte... dna){
            this.dna = dna;
            this.chromo = chromo;
        }

        public Gene(int chromo){
            this.chromo = chromo;
        }

        public int getChromo(){
            return chromo;
        }
        public byte[] getDNA(){
            return dna;
        }

        public abstract String printFunction(Array<Node> nodes);
    }
    public static class Chromo1Gene extends Gene{
        public Chromo1Gene(int chromo, byte... dna) {
            super(chromo, dna);
        }

        public String printFunction(Array<Node> nodes) {
            return null;
        }
    }
    public static class Chromo2Gene extends Gene{
        int txID, rxID, txType, rxType, domP, domN, weightI;
        float weightF;

        public Chromo2Gene(int chromo) {
            super(chromo);
        }
        public Chromo2Gene(int chromo, byte... dna) {
            super(chromo, dna);

            this.dna = dna;
            txType = dna[0] & 0xFF;
            domP = txType & 0b01111111;
            txType = (txType & 0b10000000) >> 7;

            rxType = dna[2] & 0xFF;
            domN = rxType & 0b01111111;
            rxType = (rxType & 0b10000000) >> 7;

            txID = (dna[1] & 0xFF) % (txType == 1 ? PROCESS_OPTION_COUNT : INPUT_OPTION_COUNT);
            rxID = (dna[3] & 0xFF) % (rxType == 1 ? OUTPUT_OPTION_COUNT : PROCESS_OPTION_COUNT);

            weightI = (dna[4] & 0xFF) + (dna[5] & 0xFF) - 0xFF;
            weightF = (weightI / (float)(0xFF)) * 4F;
        }

        public void setDNA(byte... dna){
            this.dna = dna;
            txType = dna[0] & 0xFF;
            domP = txType & 0b01111111;
            txType = (txType & 0b10000000) >> 7;

            rxType = dna[2] & 0xFF;
            domN = rxType & 0b01111111;
            rxType = (rxType & 0b10000000) >> 7;

            txID = (dna[1] & 0xFF) % (txType == 1 ? PROCESS_OPTION_COUNT : INPUT_OPTION_COUNT);
            rxID = (dna[3] & 0xFF) % (rxType == 1 ? OUTPUT_OPTION_COUNT : PROCESS_OPTION_COUNT);

            weightI = (dna[4] & 0xFF) + (dna[5] & 0xFF) - 0xFF;
            weightF = (weightI / (float)(0xFF)) * 4F;
        }

        public String printFunction(Array<Node> nodes) {
            return (txType + ":" + txID + " --> " + rxType + ":" + rxID + ", Dominance: +" + domP + " -" + domN + ", Weight: " + weightF);
        }
    }
}
