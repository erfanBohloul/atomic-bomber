package com.mygdx.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyboardController implements InputProcessor {

    public boolean down, up, left, right;

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;
        switch (keycode) {
            case Input.Keys.UP:
                up = true;
                keyProcessed = true;
                break;
            case Input.Keys.DOWN:
                down = true;
                keyProcessed = true;
                break;
            case Input.Keys.LEFT:
                left = true;
                keyProcessed = true;
                break;
            case Input.Keys.RIGHT:
                right = true;
                keyProcessed = true;
                break;
        }

        return keyProcessed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;
        switch (keycode)
        {
            case Input.Keys.LEFT:
                left = false;
                keyProcessed = true;
                break;
            case Input.Keys.RIGHT:
                right = false;
                keyProcessed = true;
                break;
            case Input.Keys.UP:
                up = false;
                keyProcessed = true;
                break;
            case Input.Keys.DOWN:
                down = false;
                keyProcessed = true;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}