package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AtomicBomberMain;

public class MenuScreen implements Screen {

    private AtomicBomberMain parent;
    private Stage stage;

    private Skin skin;

    private SpriteBatch batch;
    private Texture avatarTexture;
    public MenuScreen(AtomicBomberMain parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();

        // todo maybe need to eleminate this two line
        parent.assetManager.queueAddSkin();
        parent.assetManager.manager.finishLoading();
        skin = parent.assetManager.manager.get("skin/glassy-ui.json");
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        TextButton newGameButton = new TextButton("New Game", skin);
        TextButton quitButton = new TextButton("Quit", skin);
        TextButton preferenceButton = new TextButton("Preferences", skin);

        // images
        Image avatar = new Image(new Texture(Gdx.files.internal("images/img.png")));


        table.add(avatar).width(50).height(50);
        table.row().pad(30, 0, 0, 0);
        table.add(newGameButton).fillX().uniformX();
        table.row();
        table.add(preferenceButton).fillX().uniformX();
        table.row();
        table.add(quitButton).fillX().uniformX();


        // listeners
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.APPLICATION);
            }
        });

        // todo add preferenceButton listener

        // textures
        avatarTexture = new Texture(Gdx.files.internal("images/img.png"));
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

//        batch.begin();
//        batch.draw(avatarTexture, avatarContainer.getX(), avatarContainer.getY());
//        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
