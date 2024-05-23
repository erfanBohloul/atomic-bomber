package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AtomicBomberMain;
import com.mygdx.game.controller.AccountController;
import com.mygdx.game.model.account.User;
import com.mygdx.game.views.element.Notification;

public class LoginScreen implements Screen {

    private AtomicBomberMain parent;
    private Stage stage;
    private Skin skin;

    private TextField usernameInput,
        passwordInput;

    private TextButton loginButton,
        registerButton,
        guestButton;

    public LoginScreen(AtomicBomberMain parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());


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

        // inputs
        usernameInput = new TextField("", skin);
        usernameInput.setMessageText("Enter your username");

        passwordInput = new TextField("", skin);
        passwordInput.setMessageText("Enter your password");

        // buttons
        loginButton = new TextButton("Login", skin);
        registerButton = new TextButton("Register", skin);
        guestButton  = new TextButton("Guest", skin);


        table.add(usernameInput).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(passwordInput).fillX().uniformX();
        table.row();
        table.add(loginButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(registerButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(guestButton).fillX().uniformX();

        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameInput.getText();
                String password = passwordInput.getText();


                try {
                    login(username, password);

                    System.out.println("[SUCC] Login Successful");
                    parent.changScreen(AtomicBomberMain.MENU);
                } catch (Exception e) {
                    System.out.println("[ERROR] " + e.getMessage());
                    Notification notification = new Notification(e.getMessage(), Notification.NotificationType.FAIL);
                    table.addActor(notification);

                    usernameInput.setText("");
                    passwordInput.setText("");
                }


            }
        });

        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameInput.getText();
                String password = passwordInput.getText();


                try {
                    register(username, password);

                    Notification notification = new Notification("register", Notification.NotificationType.SUCCESS);
                    table.addActor(notification);
                    System.out.println("[REGISTER] " + username + " " + password);
                } catch (Exception e) {
                    System.out.println("[ERROR] " + e.getMessage());
                    Notification notification = new Notification(e.getMessage(), Notification.NotificationType.FAIL);
                    table.addActor(notification);

                }

                usernameInput.setText("");
                passwordInput.setText("");
            }
        });

        guestButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changScreen(AtomicBomberMain.APPLICATION);
                System.out.println("[SUCC] Entering as Guest");
            }
        });
    }

    private void login(String username, String password) throws Exception {
        AccountController.login(username, password);
    }

    private void register(String username, String password) throws Exception {
        AccountController.register(username, password);
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
