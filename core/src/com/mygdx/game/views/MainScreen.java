package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.entity.enemies.Tank;
import com.mygdx.game.model.entity.damager.ClusterBomb;
import com.mygdx.game.views.element.FreezeBar;

import java.util.ArrayList;

public class MainScreen implements Screen {

    private AtomicBomberMain parent;
    private KeyboardController controller;
    private GameModel model;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;

    public static final int WIDTH = 240,
                    HEIGHT = 360;

    private Texture floorTexture,
        clusterTexture,
        tankTexture;


    private Label killCountLabel,
        remainCluster,
        remainAtomicBomb,
        frozenProgress;

    public FreezeBar freezeBar;

    public ProgressBar frozenBar;

    public Table table;

    private Skin skin;

    public MainScreen(AtomicBomberMain parent) {
        this.parent = parent;


        controller = new KeyboardController();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        model = new GameModel(this, controller, parent.assetManager);

        loadTextures();
        // skin
        skin = parent.assetManager.manager.get("skin/glassy-ui.json");

        createTable();

        freezeBar = new FreezeBar();


        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    private void createTable() {

        table = new Table();
        table.setSize(20, 70);
        table.setPosition(-WIDTH / 2f+ 70f, HEIGHT/2f - 100f);

        killCountLabel = new Label("kill: 0", skin);
        killCountLabel.setFontScale(0.7f);

        remainCluster = new Label("cluster: 0", skin);
        remainCluster.setFontScale(0.7f);

        remainAtomicBomb = new Label("atomic: 0", skin);
        remainAtomicBomb.setFontScale(0.7f);

        frozenProgress = new Label("frozen: %0", skin);
        frozenProgress.setFontScale(0.7f);


        table.add(killCountLabel).left();
        table.row();
        table.add(remainCluster).left();
        table.row();
        table.add(remainAtomicBomb).left();
        table.row();
        table.add(frozenProgress).left();
    }

    private void loadTextures() {
        parent.assetManager.queueAddImages();
        parent.assetManager.manager.finishLoading();

        Texture playerTexture = parent.assetManager.manager.get("images/img.png");
        model.player.setTexture(playerTexture);
        floorTexture = parent.assetManager.manager.get("images/floor.jpg");
        tankTexture = parent.assetManager.manager.get("images/tank.png");
        clusterTexture = parent.assetManager.manager.get("images/cluster.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        model.logicStep(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(model.world, camera.combined);

        batch.begin();
        model.player.getSprite().draw(batch);
        batch.draw(floorTexture, -WIDTH/2f, model.floor.getPosition().y, Gdx.graphics.getWidth(), GameModel.floorHeight);

        ArrayList<Tank> tanks = model.getTanks();
        for (Tank tank : tanks) {
            batch.draw(tankTexture, tank.getPosition().x - Tank.WIDTH/2f, tank.getPosition().y - Tank.HEIGHT/2f, Tank.WIDTH, Tank.HEIGHT);
        }



        ArrayList<ClusterBomb> clusters = model.getClusters();
        for (ClusterBomb cluster : clusters) {
            cluster.render(batch, clusterTexture);
        }

        // table
        updateTable();
        table.draw(batch, 1);

        batch.end();
    }

    private void updateTable() {
        killCountLabel.setText("kill: " + model.getNumKills());
        remainCluster.setText("cluster: " + model.getNumCluster());
        remainAtomicBomb.setText("atomic: " + model.getNumAtomic());
        frozenProgress.setText("frozen: %" + (int) (model.getFrozenProgress() * 100));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
