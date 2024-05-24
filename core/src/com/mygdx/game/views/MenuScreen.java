package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.model.account.User;

public class MenuScreen implements Screen {

    private AtomicBomberMain parent;
    private Stage stage;
    private User user;
    private Skin skin;

    private Texture avatarTexture;
    public MenuScreen(AtomicBomberMain parent, User user) {
        this.parent = parent;
        this.user = user;
        stage = new Stage(new ScreenViewport());

        parent.assetManager.queueAddAvatar();
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

        Label nameLabel = new Label(user.getUsername(),  skin);
        TextButton newGameButton = new TextButton("New Game", skin);
        TextButton quitButton = new TextButton("Quit", skin);
        TextButton preferenceButton = new TextButton("Preferences", skin);
        TextButton profileButton = new TextButton("Profile", skin);


        switch (user.numAvatar) {
            case 0:
                avatarTexture = parent.assetManager.manager.get("images/avatar1.png");
                break;
            case 1:
                avatarTexture = parent.assetManager.manager.get("images/avatar2.jpeg");
                break;
            case 2:
                avatarTexture = parent.assetManager.manager.get("images/avatar3.png");
                break;
            case 3:
                avatarTexture = parent.assetManager.manager.get("images/avatar4.png");
                break;
            default:
                avatarTexture = parent.assetManager.manager.get("images/avatar2.jpeg");
                break;
        }
        // images
        Image avatar = new Image(avatarTexture);


        table.add(avatar).width(50).height(50);
        table.row().pad(30, 0, 0, 0);
        table.add(nameLabel).fillX().uniformX();
        table.row();
        table.add(newGameButton).fillX().uniformX();
        table.row();
        table.add(preferenceButton).fillX().uniformX();
        table.row();
        table.add(quitButton).fillX().uniformX();
        table.row();
        table.add(profileButton).fillX().uniformX();

        profileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.PROFILE, user, AtomicBomberMain.MAIN);
            }
        });

        // listeners
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.LOGIN, null, AtomicBomberMain.MENU);
            }
        });

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.APPLICATION, user, AtomicBomberMain.MENU);
            }
        });

        preferenceButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.PREFERENCES, user, AtomicBomberMain.MENU);
            }
        });

    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

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
        avatarTexture.dispose();
    }
}
