package com.xemplarsoft.nnt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class XShapeRenderer extends ShapeRenderer {
    private final Vector2 st = new Vector2(), end = new Vector2();
    private final ImmediateModeRenderer renderer;

    public XShapeRenderer(){
        renderer = super.getRenderer();
    }

    public void circleRectA(float x, float y, float radius, float width, int segments) {
        if (segments <= 0) throw new IllegalArgumentException("segments must be > 0.");
        float colorBits = this.getColor().toFloatBits();
        float angle = 2 * MathUtils.PI / segments;
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);

        float cx = radius, cy = 0;
        float vx = radius, vy = 0;

        for (int i = 0; i < segments; i++) {
            renderer.color(colorBits);

            vx = cx;
            vy = cy;

            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            rectLine(x + cx, y + cy, x + vx, y + vy, width);

            circle(x + cx, y + cy, width / 2, segments);
        }
        // Ensure the last segment is identical to the first.
        renderer.color(colorBits);
        vx = cx;
        vy = cy;

        cx = radius;
        cy = 0;
        rectLine(x + cx, y + cy, x + vx, y + vy, width);

        circle(x + cx, y + cy, width / 2, segments);
    }

    /** Draws an arc using {@link ShapeType#Line} or {@link ShapeType#Filled}. */
    public void arcA(float x, float y, float radius, float start, float degrees) {
        int segments = 20;
        float colorBits = getColor().toFloatBits();
        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);

        float vx, vy;
        for (int i = 0; i < segments; i++) {
            renderer.color(colorBits);

            vx = x + cx;
            vy = y + cy;
            renderer.vertex(vx, vy, 0);
            if(i == (segments - 1)) st.set(vx, vy);

            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            renderer.color(colorBits);

            vx = x + cx;
            vy = y + cy;
            renderer.vertex(vx, vy, 0);
            if(i == (segments - 1)) end.set(vx, vy);
        }

        Vector2[] points = calculateArrowhead(st, end, 0.25F, 0.5F);
        renderer.color(colorBits);
        renderer.vertex(points[1].x, points[1].y, 0);

        renderer.color(colorBits);
        renderer.vertex(points[0].x, points[0].y, 0);

        renderer.color(colorBits);
        renderer.vertex(points[2].x, points[2].y, 0);

        renderer.color(colorBits);
        renderer.vertex(points[0].x, points[0].y, 0);
    }

    public void rectArcA(float x, float y, float radius, float start, float degrees, float width) {
        int segments = 20;
        float colorBits = getColor().toFloatBits();
        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);

        float vx, vy;
        for (int i = 0; i < segments; i++) {
            vx = x + cx;
            vy = y + cy;
            if(i == (segments - 1)) st.set(vx, vy);

            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;

            rectLine(vx, vy, x + cx, y + cy, width);

            vx = x + cx;
            vy = y + cy;
            circle(vx, vy, width / 2, 8);
            if(i == (segments - 1)) end.set(vx, vy);
        }

        Vector2[] points = calculateArrowhead(st, end, 0.25F, 0.5F);
        rectLine(points[0].x, points[0].y, points[1].x, points[1].y, width);
        rectLine(points[0].x, points[0].y, points[2].x, points[2].y, width);
    }

    public static Vector2[] calculateArrowhead(Vector2 startPoint, Vector2 endPoint, float arrowLength, float arrowWidth) {
        // Calculate the direction vector from start to end
        Vector2 direction = endPoint.cpy().sub(startPoint).nor();

        // Calculate the perpendicular vector
        Vector2 perpendicular = new Vector2(-direction.y, direction.x);

        // Calculate points for arrowhead
        Vector2[] arrowheadPoints = new Vector2[3];
        arrowheadPoints[0] = endPoint.cpy(); // Tip of the arrowhead
        arrowheadPoints[1] = endPoint.cpy().sub(direction.cpy().scl(arrowLength)).add(perpendicular.cpy().scl(arrowWidth / 2));
        arrowheadPoints[2] = endPoint.cpy().sub(direction.cpy().scl(arrowLength)).add(perpendicular.cpy().scl(-arrowWidth / 2));

        return arrowheadPoints;
    }
}
