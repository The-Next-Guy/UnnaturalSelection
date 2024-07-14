package com.xemplarsoft.nnt.neuro;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.xemplarsoft.nnt.XShapeRenderer;

public class NetworkRenderer {
    private Network net;

    //Network Drawing PreRaster
    public GlyphLayout glyphLayout;
    public Array<Boolean> self;
    public Array<Float> angles, values;
    public Array<Node> inCount, outCount, proCount;
    public Array<Connection> connections;
    public ArrayMap<Vector2, Vector2> arrows;
    protected boolean rasterized = false;

    //Network Drawing Colors
    public static final Color PROCESS_COLOR = new Color(0.5F, 0.5F, 0.5F, 1);
    public static final Color OUTPUT_COLOR = new Color(0.7F, 0.2F, 0.2F, 1);
    public static final Color INPUT_COLOR = new Color(0.2F, 0.2F, 0.8F, 1);

    public static final Color POS_CORR = new Color(0.1F, 1.0F, 0.1F, 1);
    public static final Color NEG_CORR = new Color(1.0F, 0.1F, 0.1F, 1);
    private static final float NODE_RADIUS = 0.40F;
    private static final float NODE_VT_OFFSET = 1.25F;
    private static final float NODE_HZ_OFFSET = 1.25F;

    public NetworkRenderer(){

    }

    public void setNetwork(Network n){
        this.net = n;
        rasterized = false;
    }

    public void rasterizeNetwork(){
        glyphLayout = new GlyphLayout();
        connections = new Array<>();
        arrows = new ArrayMap<>();
        values = new Array<>();
        angles = new Array<>();
        self = new Array<>();

        inCount = new Array<>();
        outCount = new Array<>();
        proCount = new Array<>();

        for(int i = 0; i < net.nodes.size; i++){
            Node curr = net.nodes.get(i);
            if(curr instanceof OutputNode){
                outCount.add(curr);
                continue;
            }
            if(curr instanceof ProcessingNode){
                proCount.add(curr);
                continue;
            }
            if(curr instanceof InputNode){
                inCount.add(curr);
            }
        }

        Vector2 to, from;
        for(int i = 0; i < outCount.size; i++){
            Node curr = outCount.get(i);
            int toX = i;
            int toY = 0;

            for(int j = 0; j < curr.inputs.size; j++){
                Node input = curr.inputs.getKeyAt(j);
                float weight = curr.inputs.getValueAt(j);

                int fromX = -1, fromY = -1;
                for(int k = 0; k < proCount.size; k++){
                    if(input == proCount.get(k)){
                        fromX = k;
                        fromY = 1;
                    }
                }
                if(fromX == -1) {
                    for (int k = 0; k < inCount.size; k++) {
                        if (input == inCount.get(k)) {
                            fromX = k;
                            fromY = 2;
                        }
                    }
                }
                to = new Vector2(toX * NODE_HZ_OFFSET, toY * NODE_VT_OFFSET);
                from = new Vector2(fromX * NODE_HZ_OFFSET + (fromY == 1 ? (NODE_VT_OFFSET / 2) : 0F), fromY * NODE_VT_OFFSET);
                connections.add(new Connection(from, to));
                values.add(weight);
                self.add(false);
            }
        }
        for(int i = 0; i < proCount.size; i++){
            Node curr = proCount.get(i);
            int toX = i;
            int toY = 1;

            for(int j = 0; j < curr.inputs.size; j++){
                Node input = curr.inputs.getKeyAt(j);
                float weight = curr.inputs.getValueAt(j);

                if(input == curr){ // How to draw self connection...
                    self.add(true);
                } else {
                    self.add(false);
                }

                int fromX = -1, fromY = -1;
                for(int k = 0; k < proCount.size; k++){
                    if(input == proCount.get(k)){
                        fromX = k;
                        fromY = 1;
                    }
                }
                if(fromX == -1) {
                    for (int k = 0; k < inCount.size; k++) {
                        if (input == inCount.get(k)) {
                            fromX = k;
                            fromY = 2;
                        }
                    }
                }

                to = new Vector2((toX * NODE_HZ_OFFSET) + (NODE_HZ_OFFSET / 2F), toY * NODE_VT_OFFSET);
                from = new Vector2(fromX * NODE_HZ_OFFSET + (fromY == 1 ? (NODE_HZ_OFFSET / 2F) : 0F), fromY * (NODE_VT_OFFSET));

                connections.add(new Connection(from, to));
                values.add(weight);
                angles.add(calculateAngle(from, to));
            }
        }

        rasterized = true;
    }

    public void drawText(SpriteBatch batch, BitmapFont font, float x, float y, float w, float h){
        x += 1F;
        y += 1.5F;

        for(int i = 0; i < outCount.size; i++){
            glyphLayout.setText(font, outCount.get(i).getName());
            font.draw(batch, glyphLayout, x + (i * NODE_HZ_OFFSET) - (glyphLayout.width / 2), y + (glyphLayout.height));
        }

        for(int i = 0; i < proCount.size; i++){
            glyphLayout.setText(font, proCount.get(i).getName());
            font.draw(batch, glyphLayout, x + (i * NODE_HZ_OFFSET) + (NODE_HZ_OFFSET / 2) - (glyphLayout.width / 2), y + (NODE_VT_OFFSET) + (glyphLayout.height));
        }

        for(int i = 0; i < inCount.size; i++){
            glyphLayout.setText(font, inCount.get(i).getName());
            font.draw(batch, glyphLayout, x + (i * NODE_HZ_OFFSET) - (glyphLayout.width / 2), y + (NODE_VT_OFFSET * 2) + (glyphLayout.height));
        }
    }
    public void drawFill(XShapeRenderer renderer, float x, float y, float w, float h){
        if(!rasterized) rasterizeNetwork();

        x += 1F;
        y += 1F;

        renderer.setColor(OUTPUT_COLOR);
        for(int i = 0; i < outCount.size; i++){
            renderer.circle(x + (i * NODE_HZ_OFFSET), y + 0.5F, NODE_RADIUS, 20);
        }

        renderer.setColor(PROCESS_COLOR);
        for(int i = 0; i < proCount.size; i++){
            renderer.circle(x + (i * NODE_HZ_OFFSET) + (NODE_HZ_OFFSET / 2F), y + 0.5F + NODE_VT_OFFSET, NODE_RADIUS, 20);
        }

        renderer.setColor(INPUT_COLOR);
        for(int i = 0; i < inCount.size; i++){
            renderer.circle(x + (i * NODE_HZ_OFFSET), y + 0.5F + (NODE_VT_OFFSET * 2), NODE_RADIUS, 20);
        }

        y += 0.5F;

        float radius = 0, value;
        Vector2 from, to, mid;

        for(int i = 0; i < connections.size; i++){
            from = connections.get(i).from.cpy();
            to = connections.get(i).to.cpy();
            value = values.get(i);
            renderer.setColor(value > 0F ? POS_CORR : NEG_CORR);

            mid = calculateMidPoint(from, to);
            mid = findOffsetPoint(mid, from, to, -0.5F);
            radius = mid.dst(to);

            calculateCircleIntersect(midPoint, radius, from, NODE_RADIUS);
            if(intersectA.dst(mid) > intersectB.dst(mid)){
                from.set(intersectA.x, intersectA.y);
            } else {
                from.set(intersectB.x, intersectB.y);
            }

            calculateCircleIntersect(midPoint, radius, to, NODE_RADIUS);
            if(intersectA.dst(mid) > intersectB.dst(mid)){
                to.set(intersectA.x, intersectA.y);
            } else {
                to.set(intersectB.x, intersectB.y);
            }

            float fromDeg = MathUtils.radiansToDegrees * MathUtils.atan2(from.y - mid.y, from.x - mid.x);
            float toDeg = MathUtils.radiansToDegrees * MathUtils.atan2(to.y - mid.y, to.x - mid.x);

            renderer.rectArcA(x + mid.x, y + mid.y, radius, fromDeg, toDeg - fromDeg, 0.2F * Math.abs(value / 4F));
        }
    }
    public void drawLines(XShapeRenderer renderer, float x, float y, float w, float h){
        if(!rasterized) rasterizeNetwork();

        x += 1F;
        y += 1.5F;
        float radius = 0;
        Vector2 from, to, mid;
        Array<Vector2> intersections;

        for(int i = 0; i < connections.size; i++){renderer.setColor(values.get(i) > 0F ? POS_CORR : NEG_CORR);
            from = connections.get(i).from.cpy();
            to = connections.get(i).to.cpy();

            mid = calculateMidPoint(from, to);
            mid = findOffsetPoint(mid, from, to, -0.5F);
            radius = mid.dst(to);

            calculateCircleIntersect(midPoint, radius, from, NODE_RADIUS);
            if(intersectA.dst(mid) > intersectB.dst(mid)){
                from.set(intersectA.x, intersectA.y);
            } else {
                from.set(intersectB.x, intersectB.y);
            }

            calculateCircleIntersect(midPoint, radius, to, NODE_RADIUS);
            if(intersectA.dst(mid) > intersectB.dst(mid)){
                to.set(intersectA.x, intersectA.y);
            } else {
                to.set(intersectB.x, intersectB.y);
            }

            float fromDeg = MathUtils.radiansToDegrees * MathUtils.atan2(from.y - mid.y, from.x - mid.x);
            float toDeg = MathUtils.radiansToDegrees * MathUtils.atan2(to.y - mid.y, to.x - mid.x);

            renderer.arcA(x + mid.x, y + mid.y, radius, fromDeg, toDeg - fromDeg);
        }
    }

    private static final Vector2 midPoint = new Vector2();
    public static Vector2 calculateMidPoint(Vector2 a, Vector2 b){
        return midPoint.set((a.x + b.x) / 2, (a.y + b.y) / 2);
    }
    public static float calculateAngle(Vector2 point1, Vector2 point2) {
        return (float) Math.toDegrees(Math.atan((point2.y - point1.y) / (point2.x - point1.x)));
    }
    public static Vector2 findOffsetPoint(Vector2 midpoint, Vector2 point1, Vector2 point2, float offsetDistance) {
        Vector2 vector = new Vector2(-(point2.y - point1.y), point2.x - point1.x);
        float magnitude = (float)Math.sqrt((vector.x * vector.x) + (vector.y * vector.y));
        vector.set(vector.x / magnitude, vector.y / magnitude);
        vector.set(vector.x * offsetDistance, vector.y * offsetDistance);
        vector.set(midpoint.x + vector.x, midpoint.y + vector.y);

        return vector;
    }

    public static final Array<Vector2> intersect = new Array<>();
    public static final Vector2 intersectA = new Vector2();
    public static final Vector2 intersectB = new Vector2();
    public static Array<Vector2> calculateCircleIntersect(Vector2 circle1_center, float circle1_radius, Vector2 circle2_center, float circle2_radius){
        intersect.clear();
        float distance_centers = circle1_center.dst(circle2_center);

        // Check if the circles are separate or one is contained within the other
        if(distance_centers > circle1_radius + circle2_radius || distance_centers < Math.abs(circle1_radius - circle2_radius)){
            return intersect;
        }

        // Calculate the distance from circle1_center to the line that connects the two intersection points
        float a = (float)(Math.pow(circle1_radius, 2) - Math.pow(circle2_radius, 2) + Math.pow(distance_centers, 2)) / (2 * distance_centers);

        // Calculate the distance from the intersection point to either intersection point
        float h = (float) Math.sqrt(Math.pow(circle1_radius, 2) - Math.pow(a, 2));

        intersect.add(intersectA.set(circle1_center.x + a * (circle2_center.x - circle1_center.x) / distance_centers + h * (circle2_center.y - circle1_center.y) / distance_centers, circle1_center.y + a * (circle2_center.y - circle1_center.y) / distance_centers - h * (circle2_center.x - circle1_center.x) / distance_centers));
        intersect.add(intersectB.set(circle1_center.x + a * (circle2_center.x - circle1_center.x) / distance_centers - h * (circle2_center.y - circle1_center.y) / distance_centers, circle1_center.y + a * (circle2_center.y - circle1_center.y) / distance_centers + h * (circle2_center.x - circle1_center.x) / distance_centers));

        return intersect;
    }

    public static class Connection{
        private static long ID_COUNTER = 0;
        public final long ID;
        public final Vector2 from, to;

        public Connection(Vector2 from, Vector2 to){
            ID = ID_COUNTER++;
            this.from = from;
            this.to = to;
        }
    }
}
