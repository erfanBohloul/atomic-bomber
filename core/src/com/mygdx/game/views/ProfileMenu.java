package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.controller.Database;
import com.mygdx.game.model.account.User;
import com.mygdx.game.views.element.Notification;
import org.w3c.dom.Text;
import sun.tools.jconsole.Tab;

public class ProfileMenu implements Screen {

    private AtomicBomberMain parent;
    private Stage stage;
    private User user;
    private Skin skin;

    public ProfileMenu(AtomicBomberMain parent, User user) {
        this.parent = parent;
        this.user = user;
        skin = parent.assetManager.manager.get("skin/glassy-ui.json");

        stage = new Stage(new ScreenViewport());



    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        Label newUsername = new Label("new Username", skin);
        Label newPassword = new Label("new Password", skin);

        TextField usernameField = new TextField(null, skin);
        TextField passwordField = new TextField(null, skin);

        TextButton avatarMenuButton = new TextButton("avatar menu", skin);
        TextButton backButton = new TextButton("back", skin);
        TextButton deleteAccountButton = new TextButton("delete account", skin);
        TextButton applyButton = new TextButton("Apply", skin);

        table.add(avatarMenuButton).fillX().uniformX();
        table.row();
        table.add(newUsername);
        table.add(usernameField);
        table.row();
        table.add(newPassword);
        table.add(passwordField);
        table.row();
        table.add(deleteAccountButton);
        table.row();
        table.add(applyButton);
        table.add(backButton);

        avatarMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.AVATAR, user, AtomicBomberMain.PROFILE);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.MENU, user, AtomicBomberMain.PROFILE);
            }
        });

        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String newUsername = usernameField.getText();
                String newPassword = passwordField.getText();

                try {
                    if (newUsername != null && !newPassword.isEmpty() && !newUsername.equals("")) {
                        System.out.println("newUsername: "+ newUsername);
                        Database.changeUsername(user.getUsername(), newUsername);
                        user.setUsername(newUsername);
                    }

                    if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
                        System.out.println("newPassword: "+ newPassword);
                        Database.changePassword(user.getUsername(), newPassword);
                        user.setPassword(newPassword);
                    }
                } catch (Exception e) {
                    System.out.println("[ERROR] " + e.getMessage());
                    Notification notification = new Notification(e.getMessage(), Notification.NotificationType.FAIL);
                    table.addActor(notification);
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.MENU, user, AtomicBomberMain.PROFILE);
            }
        });

        deleteAccountButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Database.deleteUser(user.getUsername())) {
                    System.out.println("[SUCC] " + user.getUsername() + "has been deleted");
                    user = null;
                    parent.changScreen(AtomicBomberMain.LOGIN, user, AtomicBomberMain.PROFILE);
                }
                else {
                    System.out.println("[ERROR] " + user.getUsername() + "has not been deleted");
                }
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

    }
}
