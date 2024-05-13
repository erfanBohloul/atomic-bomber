package com.mygdx.game.views;

import com.badlogic.gdx.Screen;
import com.mygdx.game.AtomicBomberMain;

public class LoadingScreen implements Screen {

    AtomicBomberMain parent;

    public LoadingScreen(AtomicBomberMain parent) {
        this.parent = parent;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        parent.changScreen(AtomicBomberMain.MENU);
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
