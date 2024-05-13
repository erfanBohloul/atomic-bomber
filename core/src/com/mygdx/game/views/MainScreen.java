package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.model.GameModel;

public class MainScreen implements Screen {

    private AtomicBomberMain parent;
    private KeyboardController controller;
    private GameModel model;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;

    private Texture playerTexture;

    public MainScreen(AtomicBomberMain parent) {
        this.parent = parent;

        controller = new KeyboardController();
        model = new GameModel(controller, parent.assetManager);
        camera = new OrthographicCamera(32, 24);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        parent.assetManager.queueAddImages();
        parent.assetManager.manager.finishLoading();

        playerTexture = parent.assetManager.manager.get("images/img.png");

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
        batch.draw(playerTexture, model.player.getPosition().x-1, model.player.getPosition().y-1, 2, 2);
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
