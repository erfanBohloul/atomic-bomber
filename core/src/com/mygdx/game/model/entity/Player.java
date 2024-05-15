package com.mygdx.game.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;

public class Player {

    public static int DEFAULT_X = 1, DEFAULT_Y = 1, DEFAULT_WIDTH = 1, DEFAULT_HEIGHT = 1;

    public Body body;
    public int reloadTimer;

    public Player(int posx, int posy, int width, int height) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        this.body = bodyFactory.makeBoxPolyBody(1, 1, 2, 2, BodyDef.BodyType.DynamicBody, false);
        body.setUserData("PLAYER");
        reloadTimer = 0;
    }

    public Player() {
        this(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public boolean readyForBombing() {
        return reloadTimer <= 0;
    }

    public void decreaseReloadTimer() {
        reloadTimer = Math.max(0, reloadTimer - 1);
    }

    public void reload() {
        this.reloadTimer = 30;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}
