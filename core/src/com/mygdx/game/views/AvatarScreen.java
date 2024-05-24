package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.model.account.User;

public class AvatarScreen implements Screen {

    public AtomicBomberMain parent;
    public User user;
    public Stage stage;
    private Skin skin;
    private Texture avatar;

    public AvatarScreen(AtomicBomberMain parent, User user) {
        this.parent = parent;
        this.user = user;

        stage = new Stage(new ScreenViewport());
        skin = parent.assetManager.manager.get("skin/glassy-ui.json");
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        switch (user.numAvatar) {
            case 0:
                avatar = parent.assetManager.manager.get("images/avatar1.png");
                break;
            case 1:
                avatar = parent.assetManager.manager.get("images/avatar2.jpeg");
                break;
            case 2:
                avatar = parent.assetManager.manager.get("images/avatar3.png");
                break;
            case 3:
                avatar = parent.assetManager.manager.get("images/avatar4.png");
                break;
            default:
                avatar = parent.assetManager.manager.get("images/avatar2.jpeg");
                break;
        }

        TextButton backButton = new TextButton("Back", skin);

        Image avatarImage = new Image(avatar);
        table.add(avatarImage);
        table.row();
        table.add(backButton);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.PROFILE, user, AtomicBomberMain.AVATAR);
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
        avatar.dispose();
    }
}
