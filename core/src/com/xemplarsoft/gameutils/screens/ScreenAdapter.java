package com.xemplarsoft.gameutils.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.xemplarsoft.gameutils.screens.ui.AbstractComponent;
import com.xemplarsoft.gameutils.screens.ui.AbstractContainer;
public abstract class ScreenAdapter extends AbstractContainer implements Screen, InputProcessor {
    public static final float TILE_MODIFIER = 1F;
    public static float WORLD_WIDTH = 16F;
    public static float WORLD_HEIGHT = 9F, WORLD_WIDTH_MAX = 20F, WORLD_WIDTH_MIN = 16F;

    protected int screenWidth, screenHeight;
    protected SpriteBatch batch;
    protected ShapeRenderer debugRenderer;
    public ExtendViewport viewport;
    public OrthographicCamera camera, hud;

    public ScreenAdapter(){
        hud = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    }

    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    public void pause() {
        Gdx.input.setInputProcessor(null);
    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {

    }

    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

        float ratio = width / (float) height;
        WORLD_WIDTH = WORLD_HEIGHT * ratio;

        hud.viewportWidth = WORLD_WIDTH;
        hud.viewportHeight = WORLD_HEIGHT;
        hud.position.set(WORLD_WIDTH / 2F, WORLD_HEIGHT / 2F, 1F);

        hud.update();
    }

    protected Vector2 getGridUnits(int sx, int sy){
        return new Vector2(((float)sx / (float)screenWidth * WORLD_WIDTH), (screenHeight - (float)sy) / (float)screenHeight * WORLD_HEIGHT);
    }

    protected Vector2 getWorldUnits(int sx, int sy){
        return new Vector2(((float)sx / (float)screenWidth * WORLD_WIDTH) + camera.position.x - (WORLD_WIDTH / 2F), ((screenHeight - (float)sy) / (float)screenHeight * WORLD_HEIGHT) + camera.position.y - (WORLD_HEIGHT / 2F));
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 hudPos = getGridUnits(screenX, screenY);
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.touchDown(hudPos.x, hudPos.y, pointer, button);
        }
        return ret;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 hudPos = getGridUnits(screenX, screenY);
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.touchUp(hudPos.x, hudPos.y, pointer, button);
        }
        return ret;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 hudPos = getGridUnits(screenX, screenY);
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.touchDragged(hudPos.x, hudPos.y, pointer);
        }
        return ret;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 hudPos = getGridUnits(screenX, screenY);
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.mouseMoved(hudPos.x, hudPos.y);
        }
        return ret;
    }

    public boolean scrolled(float amountX, float amountY) {
        boolean ret = false;
        for(int i = 0; i < components.size; i++){
            AbstractComponent comp = components.getKeyAt(i);
            ret |= comp.scrolled(amountX, amountY);
        }
        return ret;
    }

    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public void render(float delta) {

    }

    public void renderComp(SpriteBatch batch) {
        super.renderComp(batch);
    }
}
