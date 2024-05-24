package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.account.User;
import com.mygdx.game.model.entity.Bonus;
import com.mygdx.game.model.entity.damager.*;
import com.mygdx.game.model.entity.enemies.*;
import com.mygdx.game.model.entity.enemies.Tree;

import java.util.ArrayList;

public class MainScreen implements Screen {

    private AtomicBomberMain parent;
    private KeyboardController controller;
    private GameModel model;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private User user;

    public static final int WIDTH = 240,
                    HEIGHT = 360;

    public Texture floorTexture,
        clusterTexture,
        tankTexture,
        treeTexture,
        buildingTexture,
        nukeTexture,
        planeTexture,
        truckTexture,
        freezeBarTexture,
        bonusClusterTexture,
        bonusAtomicTexture,
        bunkerTexture,
        migTexture;


    private Label killCountLabel,
        remainCluster,
        remainAtomicBomb,
        frozenProgress,
        accuracyLabel,
        waveLabel;

    public Table table,
        accuracyTable,
        pauseTable;
    public Stage pauseStage,
        endGameStage;

    public TextButton exitButton, saveButton, settingButton;
    private Skin skin;

    public MainScreen(AtomicBomberMain parent, User user) {
        this.parent = parent;
        this.user = user;
    }

    private void createPauseTable() {
        pauseStage = new Stage();
        pauseTable = new Table(skin);
        pauseStage.addActor(pauseTable);
        pauseTable.setFillParent(true);
        pauseTable.setSize(15, 15);
        pauseTable.setPosition(0, 0);
        pauseTable.setPosition(-15, -12.5f);


        exitButton = new TextButton("exit?", skin);
        exitButton.setPosition(0, 0);
        saveButton = new TextButton("Save", skin);
        saveButton.setPosition(0, -10);
        settingButton = new TextButton("setting", skin);
        settingButton.setPosition(0, -20);

        pauseTable.add(exitButton).center();
        pauseTable.row();
        pauseTable.add(saveButton).center();
        pauseTable.row();
        pauseTable.add(settingButton).center();
    }

    private void createEndGameStage() {
        Table endTable = new Table(skin);
        endTable.setFillParent(true);
        endGameStage.addActor(endTable);

        Label gameOverLabel = new Label("Game Over!", skin);
        TextButton returnButton = new TextButton("Return", skin);

        endTable.add(gameOverLabel);
        endTable.row().pad(10, 0, 10, 0);
        endTable.add(returnButton);
    }

    private void createAccuracyTable() {
        accuracyTable = new Table();
        accuracyTable.setSize(30, 30);
        accuracyTable.setPosition(-accuracyTable.getWidth()/2f, 0);
        accuracyTable.setSkin(skin);

        waveLabel = new Label(null, skin);
        waveLabel.setFontScale(0.5f);

        accuracyLabel = new Label(null, skin);
        accuracyLabel.setFontScale(.5f);

        accuracyTable.add(waveLabel).center();
        accuracyTable.row();
        accuracyTable.add(accuracyLabel).center();
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
        table.row();
        table.add(waveLabel).left();
    }

    private void loadTextures() {
        parent.assetManager.queueAddImages();
        parent.assetManager.manager.finishLoading();

        Texture playerTexture = parent.assetManager.manager.get("images/plane.png");
        model.player.setTexture(playerTexture);
        floorTexture = parent.assetManager.manager.get("images/floor.jpg");
        tankTexture = parent.assetManager.manager.get("images/tank.png");
        clusterTexture = parent.assetManager.manager.get("images/cluster.png");
        treeTexture = parent.assetManager.manager.get("images/tree.png");
        buildingTexture = parent.assetManager.manager.get("images/building.png");
        nukeTexture = parent.assetManager.manager.get("images/nukebomb.png");
        planeTexture = parent.assetManager.manager.get("images/plane.png");
        truckTexture = parent.assetManager.manager.get("images/truck.png");
        freezeBarTexture = parent.assetManager.manager.get("images/freezeBar.png");
        bonusAtomicTexture = parent.assetManager.manager.get("images/bonusnuke.png");
        bonusClusterTexture = parent.assetManager.manager.get("images/bonuscluster.png");
        bunkerTexture = parent.assetManager.manager.get("images/bunker.png");
        migTexture = parent.assetManager.manager.get("images/mig.png");
    }

    @Override
    public void show() {
        controller = new KeyboardController();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        model = new GameModel(this, controller, parent.assetManager);

        // skin
        skin = parent.assetManager.manager.get("skin/glassy-ui.json");

        endGameStage = new Stage(new ScreenViewport());

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        Gdx.input.setInputProcessor(controller);
        createEndGameStage();
        createAccuracyTable();
        createTable();
        createPauseTable();

        loadTextures();
    }

    @Override
    public void render(float delta) {

        if (controller.escape) {
            batch.begin();
            pauseStage.draw();
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                controller.escape = false;
                quitUser();
            }

            else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                parent.changScreen(AtomicBomberMain.PREFERENCES, user, AtomicBomberMain.APPLICATION);
            }
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (model.hasGameEnded()) {
            endGameStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            endGameStage.draw();

            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                model.player.health = 3;
            }

            else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (user.isGuest()) {
                    parent.changScreen(AtomicBomberMain.LOGIN, null, AtomicBomberMain.APPLICATION);
                    return;
                }

                else {
                    parent.changScreen(AtomicBomberMain.MENU, user, AtomicBomberMain.PREFERENCES);
                    return;
                }
            }
            return;
        }

        if (controller.S) {
            batch.begin();
            loadScreenBetweenWaves(batch);
            batch.end();
            return;
        }


        model.logicStep(delta);
        debugRenderer.render(model.world, camera.combined);

        batch.begin();
        batch.draw(floorTexture, -WIDTH/2f, model.floor.getPosition().y, Gdx.graphics.getWidth(), GameModel.floorHeight);

        if (model.getFreezeStatus()) {
            batch.draw(freezeBarTexture, -WIDTH/2f, -HEIGHT/2f, WIDTH, HEIGHT);
        }

        model.player.render(batch);

        ArrayList<Enemy> enemies = model.getEnemies();
        for (Enemy enemy : enemies) {
            if (enemy instanceof Tank)
                enemy.render(batch, tankTexture);

            else if (enemy instanceof Tree)
                enemy.render(batch, treeTexture);

            else if (enemy instanceof Truck)
                enemy.render(batch, truckTexture);

            else if (enemy instanceof Building)
                enemy.render(batch, buildingTexture);

            else if (enemy instanceof Bunker)
                enemy.render(batch, bunkerTexture);

            else if (enemy instanceof Mig)
                enemy.render(batch, migTexture);
        }

        ArrayList<Damager> damagers = model.getDamagers();
        for (Damager damager : damagers) {
            if (damager instanceof ClusterBomb)
                damager.render(batch, clusterTexture);

            else if (damager instanceof Bomb)
                damager.render(batch, clusterTexture);

            else if (damager instanceof TankBullet)
                damager.render(batch, clusterTexture);

            else if (damager instanceof AtomicBomb)
                damager.render(batch, clusterTexture);

        }

        for (int i = Bonus.bonuses.size()-1; i >= 0; i--) {
            Bonus.bonuses.get(i).render(batch, delta);
        }
        // table
        updateTable();
        table.draw(batch, 1);

        batch.end();
    }

    private void quitUser() {
        if (user.isGuest()) {
            parent.changeResetScreen(AtomicBomberMain.LOGIN, null);
        }

        else {
            parent.changeResetScreen(AtomicBomberMain.MENU, null);
        }
    }

    private void loadScreenBetweenWaves(SpriteBatch batch) {
        renderAccuracyTable(batch);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            controller.S = false;
            model.startNextWave();
        }
    }

    private void renderAccuracyTable(SpriteBatch batch) {
        waveLabel.setText("Wave " + model.wave + ".");

        float accuracy = model.getAccuracy() * 100;
        accuracyLabel.setText("Accuracy: %" + accuracy + ".");
        accuracyTable.draw(batch, 1);
    }

    private void updateTable() {
        killCountLabel.setText("kill: " + model.getNumKills());
        remainCluster.setText("cluster: " + model.getNumCluster());
        remainAtomicBomb.setText("atomic: " + model.getNumAtomic());
        frozenProgress.setText("frozen: %" + (int) (model.getFrozenProgress() * 100));
        waveLabel.setText("wave " + model.wave + ".");
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
        batch.dispose();
        pauseStage.dispose();
        endGameStage.dispose();
        batch.dispose();
        floorTexture.dispose();
        clusterTexture.dispose();
        tankTexture.dispose();
        treeTexture.dispose();
        truckTexture.dispose();
        buildingTexture.dispose();
        planeTexture.dispose();
        freezeBarTexture.dispose();
        nukeTexture.dispose();
        bunkerTexture.dispose();
        this.dispose();
    }
}
