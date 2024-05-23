package com.mygdx.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;

public class KeyboardController implements InputProcessor {

    public boolean accelerate ,bomb, T, leftBracket, rightBracket, cluster, atomic, frozen, escape = false;

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;
        switch (keycode) {
            case Input.Keys.D:
                accelerate = true;
                keyProcessed = true;
                break;
            case Input.Keys.F:
                bomb = true;
                keyProcessed = true;
                break;
            case Input.Keys.T:
                T = true;
                keyProcessed = true;
                break;
            case Input.Keys.LEFT_BRACKET:
                leftBracket = true;
                keyProcessed = true;
                break;
            case Input.Keys.RIGHT_BRACKET:
                rightBracket = true;
                keyProcessed = true;
                break;
            case Input.Keys.C:
                cluster = true;
                keyProcessed = true;
                break;
            case Input.Keys.R:
                atomic = true;
                keyProcessed = true;
                break;

            case Input.Keys.TAB:
                frozen = true;
                keyProcessed = true;
                break;

            case Input.Keys.ESCAPE:
                escape = !escape;
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
            case Input.Keys.D:
                accelerate = false;
                keyProcessed = true;
                break;
            case Input.Keys.F:
                bomb = false;
                keyProcessed = true;
                break;
            case Input.Keys.T:
                T = false;
                keyProcessed = true;
                break;

            case Input.Keys.LEFT_BRACKET:
                leftBracket = false;
                keyProcessed = true;
                break;

            case Input.Keys.RIGHT_BRACKET:
                rightBracket = false;
                keyProcessed = true;
                break;

            case Input.Keys.C:
                cluster = false;
                keyProcessed = true;
                break;

            case Input.Keys.R:
                atomic = false;
                keyProcessed = true;
                break;

            case Input.Keys.TAB:
                frozen = false;
                keyProcessed = true;
                break;
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
