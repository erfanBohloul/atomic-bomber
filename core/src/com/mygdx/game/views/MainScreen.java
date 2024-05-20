package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.entity.Player;
import com.mygdx.game.model.entity.Tank;

public class MainScreen implements Screen {

    private AtomicBomberMain parent;
    private KeyboardController controller;
    private GameModel model;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;

    public static final int WIDTH = 240,
                    HEIGHT = 360;

    private Texture playerTexture,
        floorTexture,
        tankTexture;

    public Label score;

    private Skin skin;
//    private Stage stage;

    public MainScreen(AtomicBomberMain parent) {
        this.parent = parent;


        controller = new KeyboardController();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        model = new GameModel(this, controller, parent.assetManager);

        // images
        parent.assetManager.queueAddImages();
        parent.assetManager.manager.finishLoading();

        playerTexture = parent.assetManager.manager.get("images/img.png");
        model.player.setTexture(playerTexture);
        floorTexture = parent.assetManager.manager.get("images/floor.jpg");
        tankTexture = parent.assetManager.manager.get("images/tank.png");

        // skin
        skin = parent.assetManager.manager.get("skin/glassy-ui.json");


        String text = "score: 0";

        score = new Label(text, skin);
        score.setPosition(-WIDTH/2f + 30 , HEIGHT / 2f - 100);
        score.setSize(30, 30);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
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

        for (Tank tank : model.tanks) {
            batch.draw(tankTexture, tank.body.getPosition().x-7f, tank.body.getPosition().y-5f, 14, 10);
        }

        score.draw(batch, 1);

        batch.end();
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
