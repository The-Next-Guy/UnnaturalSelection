package com.xemplarsoft.nnt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.xemplarsoft.gameutils.screens.ScreenAdapter;
import com.xemplarsoft.gameutils.screens.ui.AbstractComponent;
import com.xemplarsoft.gameutils.screens.ui.Action;
import com.xemplarsoft.gameutils.screens.ui.Button;
import com.xemplarsoft.gameutils.screens.ui.bg.Background;
import com.xemplarsoft.gameutils.screens.ui.bg.BackgroundLayered;
import com.xemplarsoft.gameutils.screens.ui.bg.BackgroundPress;
import com.xemplarsoft.gameutils.screens.ui.bg.BackgroundSwitch;
import com.xemplarsoft.nnt.Graph;
import com.xemplarsoft.nnt.TestGame;
import com.xemplarsoft.nnt.World;
import com.xemplarsoft.nnt.XShapeRenderer;
import com.xemplarsoft.nnt.entity.Unit;
import com.xemplarsoft.nnt.neuro.NetworkRenderer;

public class SimScreen extends ScreenAdapter implements Action{
    //Control Vars
    public static final float ROUND_START_DELAY = 0.5F;
    public static float TICK_SPEED = 0.00833F;

    //Simulation Vars
    public static int LIFETIME_DURATION = 500;
    public static final int GRID_SIZE = 256;

    //Graph Vars
    public NetworkRenderer networkRenderer;
    public Graph populationGraph;

    //Operation Vars

    public XShapeRenderer shape;
    public SpriteBatch batch;

    public World w;

    public int LIFETIME_COUNTER;
    public static boolean PAUSE_STATE, MUTATIONS = true;

    //Buttons
    protected Button btn_cpyDNA, btn_evolve, btn_reset, btn_barriers, btn_pause, btn_mutations;
    protected GlyphLayout button_glyph;

    public SimScreen(){
        w = new World();

        networkRenderer = new NetworkRenderer();
        populationGraph = new Graph(WORLD_HEIGHT, WORLD_HEIGHT * 3/4 - 0.55F,WORLD_WIDTH - WORLD_HEIGHT, WORLD_HEIGHT / 4);
        populationGraph.maxSamples = 100000;

        button_glyph = new GlyphLayout();

        batch = new SpriteBatch();
        shape = new XShapeRenderer();

        initUI();

        Gdx.input.setInputProcessor(this);
    }

    private void initUI(){
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 1F);
        camera.update();

        viewport = new ExtendViewport(WORLD_WIDTH_MIN, WORLD_HEIGHT, WORLD_WIDTH_MAX, WORLD_HEIGHT);
        viewport.setCamera(camera);

        this.btn_cpyDNA = new Button();
        this.btn_evolve = new Button();
        this.btn_reset = new Button();
        this.btn_barriers = new Button();
        this.btn_pause = new Button();
        this.btn_mutations = new Button();

        btn_cpyDNA.action = this;
        btn_evolve.action = this;
        btn_reset.action = this;
        btn_barriers.action = this;
        btn_pause.action = this;
        btn_mutations.action = this;

        btn_cpyDNA.setBg(new BackgroundPress(TestGame.textures, 0, 1, 3, "button"));
        btn_evolve.setBg(new BackgroundPress(TestGame.textures, 0, 1, 3, "button"));
        btn_reset.setBg(new BackgroundPress(TestGame.textures, 0, 1, 3, "button"));
        btn_pause.setBg(new BackgroundLayered(
                new BackgroundPress(TestGame.textures, 0, 1, 3, "button"),
                new BackgroundSwitch(
                        TestGame.textures.findRegion("run"),
                        TestGame.textures.findRegion("pause")
                )
        ));

        btn_barriers.setBg(new BackgroundLayered(
                new BackgroundSwitch(
                        new BackgroundPress(TestGame.textures, 0, 1, 3, "button"),
                        new BackgroundPress(TestGame.textures, 0, 1, 3, "button_active")
                ),
                new BackgroundSwitch(
                        TestGame.textures.findRegion("borders_off"),
                        TestGame.textures.findRegion("borders_on")
                )
        ));

        btn_mutations.setBg(new BackgroundLayered(
                new BackgroundSwitch(
                        new BackgroundPress(TestGame.textures, 0, 1, 3, "button_active"),
                        new BackgroundPress(TestGame.textures, 0, 1, 3, "button")
                ),
                new Background(TestGame.textures.findRegion("mutations"))
        ));

        btn_cpyDNA.setIcon(TestGame.textures.findRegion("copy_dna"));
        btn_evolve.setIcon(TestGame.textures.findRegion("mutate"));
        btn_reset.setIcon(TestGame.textures.findRegion("reset"));
        btn_mutations.setIcon(TestGame.textures.findRegion("mutations"));

        btn_cpyDNA.iconPadding = 0.1F;
        btn_evolve.iconPadding = 0.1F;
        btn_reset.iconPadding = 0.1F;
        btn_barriers.iconPadding = 0.1F;

        addComponent(btn_cpyDNA);
        addComponent(btn_evolve);
        addComponent(btn_reset);
        addComponent(btn_barriers);
        addComponent(btn_mutations);
        addComponent(btn_pause);
    }

    public void action(AbstractComponent sender, String command) {
        if(sender == btn_cpyDNA){
            if(w.selectedUnit == null){
                System.out.println("Select Unit First");
                return;
            }
            System.out.println("Copy DNA");
            Gdx.app.getClipboard().setContents(w.selectedUnit.network.toDNADetailString());
        }
        if(sender == btn_evolve){
            if(w.selectedUnit == null){
                System.out.println("Select Unit First");
                return;
            }
            System.out.println("Evolve");
            w.evolve(w.selectedUnit.network.DNA);
            populationGraph.getData().clear();
            LIFETIME_COUNTER = 0;
        }
        if(sender == btn_mutations){
            MUTATIONS = !MUTATIONS;
        }
        if(sender == btn_barriers){
            w.setBarriersNextGen(!w.barriersNextGen);
        }
        if(sender == btn_reset){
            resetSim();
        }
        if(sender == btn_pause){
            PAUSE_STATE = !PAUSE_STATE;
        }
    }

    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        populationGraph.setSize(WORLD_WIDTH - WORLD_HEIGHT, WORLD_HEIGHT / 4);
        camera.update();

        this.btn_cpyDNA.setBounds(WORLD_HEIGHT + 0.125F, 0.125F, 1F, 1F);
        this.btn_evolve.setBounds(WORLD_HEIGHT + 1.25F, 0.125F, 1F, 1F);
        this.btn_reset.setBounds(WORLD_HEIGHT + 2.375F, 0.125F, 1F, 1F);
        this.btn_barriers.setBounds(WORLD_HEIGHT + 3.5F, 0.125F, 1F, 1F);
        this.btn_mutations.setBounds(WORLD_HEIGHT + 4.625F, 0.125F, 1F, 1F);
        this.btn_pause.setBounds(WORLD_HEIGHT + 5.75F, 0.125F, 1F, 1F);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        update(delta);

        shape.setProjectionMatrix(camera.combined);
        shape.begin(XShapeRenderer.ShapeType.Filled);
        renderFilled();
        shape.end();

        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.begin();
        renderSprite();
        renderComp(batch);
        batch.end();

        shape.begin(XShapeRenderer.ShapeType.Line);
        renderLines();
        shape.end();
    }

    private void renderFilled(){
        shape.setColor(Color.WHITE);
        shape.rect(0, 0, WORLD_HEIGHT, WORLD_HEIGHT);

        w.render(shape);

        shape.setColor(0.5F, 1F, 0.5F, 0.25F);
        for(Shape2D survival : w.shelterRegions){
            if(survival instanceof Rectangle){
                Rectangle r = (Rectangle) survival;
                shape.rect(r.x / GRID_SIZE * WORLD_HEIGHT, r.y / GRID_SIZE * WORLD_HEIGHT, r.width / GRID_SIZE * WORLD_HEIGHT, r.height / GRID_SIZE * WORLD_HEIGHT);
            }
            if(survival instanceof Circle){
                Circle r = (Circle) survival;
                shape.circle(r.x / GRID_SIZE * WORLD_HEIGHT, r.y / GRID_SIZE * WORLD_HEIGHT, r.radius / GRID_SIZE * WORLD_HEIGHT, 20);
            }
        }

        if(w.selectedUnit != null){
            shape.setColor(1F, 0, 0, 0.8F);

            shape.rectLine((w.selectedUnit.x + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F + 2F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.x + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F + 6F) / GRID_SIZE * WORLD_HEIGHT, 0.05F);
            shape.rectLine((w.selectedUnit.x + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F - 2F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.x + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F - 6F) / GRID_SIZE * WORLD_HEIGHT, 0.05F);
            shape.rectLine((w.selectedUnit.x + 0.5F + 2F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.x + 0.5F + 6F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F) / GRID_SIZE * WORLD_HEIGHT, 0.05F);
            shape.rectLine((w.selectedUnit.x + 0.5F - 2F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.x + 0.5F - 6F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F) / GRID_SIZE * WORLD_HEIGHT, 0.05F);

            shape.circleRectA((w.selectedUnit.x + 0.5F) / GRID_SIZE * WORLD_HEIGHT, (w.selectedUnit.y + 0.5F) / GRID_SIZE * WORLD_HEIGHT, WORLD_HEIGHT / GRID_SIZE * 4, 0.05F, 12);
        }

        populationGraph.drawGraphFill(shape);

        shape.setColor(1, 1, 1, 1);
        shape.rect(WORLD_HEIGHT, WORLD_HEIGHT - 0.5F, WORLD_WIDTH - WORLD_HEIGHT, 0.5F);


        shape.rect(WORLD_HEIGHT, WORLD_HEIGHT * 5/8, WORLD_WIDTH - WORLD_HEIGHT, 0.6F);

        shape.setColor(0.9F, 0.9F, 0.9F, 1);
        shape.rect(WORLD_HEIGHT, 0, WORLD_WIDTH - WORLD_HEIGHT, WORLD_HEIGHT * 5/8);

        if(w.selectedUnit != null) networkRenderer.drawFill(shape, WORLD_HEIGHT, 1F, WORLD_WIDTH - WORLD_HEIGHT, WORLD_HEIGHT * 5/8);
    }
    private void renderLines(){
        populationGraph.drawGraphLines(shape);
    }
    private void renderSprite(){
        TestGame.font.draw(batch, "Population: " + w.units.size, WORLD_HEIGHT + 0.1F, WORLD_HEIGHT);
        TestGame.font.draw(batch, "Births: " + w.birthCount, WORLD_HEIGHT - 0.1F, WORLD_HEIGHT, WORLD_WIDTH - WORLD_HEIGHT, Align.right, false);

        TestGame.font.draw(batch, "Genetics:", WORLD_HEIGHT - 0.1F, WORLD_HEIGHT * 5/8 + 0.5F, WORLD_WIDTH - WORLD_HEIGHT, Align.center, false);

        if(w.selectedUnit != null) networkRenderer.drawText(batch, TestGame.networkLabel, WORLD_HEIGHT, 1F, WORLD_WIDTH - WORLD_HEIGHT, WORLD_HEIGHT * 5/8);
    }

    private void update(float delta){
        if(PAUSE_STATE) return;
        if(LIFETIME_COUNTER % 100 == 0){
            populationGraph.addData(w.units.size);
            populationGraph.setYMax(Math.max(w.units.size, populationGraph.yMax));
        }
        if(w.units.size == 0){
            resetSim();
        }
        LIFETIME_COUNTER += w.update(delta) ? 1 : 0;
    }
    public void resetSim(){
        w.resetWorld();
        populationGraph.getData().clear();
        LIFETIME_COUNTER = 0;
    }

    public void dispose () {
        batch.dispose();
    }
    public void resume() {
        Gdx.input.setInputProcessor(this);
    }
    public void pause() {
        Gdx.input.setInputProcessor(null);
    }

    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.R){
            resetSim();
        }
        if(keycode == Input.Keys.UP) TICK_SPEED *= 0.75;
        if(keycode == Input.Keys.DOWN) TICK_SPEED *= 1.25;

        if(keycode == Input.Keys.P) {
            PAUSE_STATE = !PAUSE_STATE;
        }

        return false;
    }
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(super.touchDown(screenX, screenY, pointer, button)) return true;

        int gridX = MathUtils.floor(screenX / (float)screenWidth * GRID_SIZE * WORLD_WIDTH / WORLD_HEIGHT);
        int gridY = MathUtils.floor((1F - (screenY / (float)screenHeight)) * GRID_SIZE);

        System.out.println("Touch: " + new Vector2(gridX, gridY));

        Unit u = w.getUnitAt(gridX, gridY);
        if(u != null) {
            System.out.println("Color: " + u.color.toString());
            w.selectedUnit = u;
            networkRenderer.setNetwork(u.network);

            u.network.printNetwork();
        } else {
            w.selectedUnit = null;
        }
        return false;
    }
}
