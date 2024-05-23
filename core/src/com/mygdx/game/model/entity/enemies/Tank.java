package com.mygdx.game.model.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.entity.damager.TankBullet;

import java.util.Random;

public class Tank extends Enemy {

    public static final float WIDTH = 14;
    public static final float HEIGHT = 10;
    public static final int RELOAD_TIME = 300;
    public int reloadTimer;


    public Tank(float posx, float posy) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.KinematicBody, true);
        body.setUserData(this);

        health = 2;
        reloadTimer = RELOAD_TIME;

        Vector2 velocity = new Vector2(0, 0);
        velocity.x = (directionToMove ? 5 : -5);
        lastVelocity = velocity;
    }

    public boolean canSee(Body body) {
        Vector2 distance = body.getPosition().sub(body.getPosition());
        return distance.x * distance.x + distance.y * distance.y < 250;
    }

    public TankBullet shootBullet(Vector2 direction) {
        reloadTimer = RELOAD_TIME;
        return new TankBullet(getPosition().x, getPosition().y + Tank.HEIGHT, direction);
    }

    public static float getArea() {
        return WIDTH * HEIGHT;
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, getPosition().x - WIDTH/2f, getPosition().y - HEIGHT/2f, WIDTH, HEIGHT);
    }
}
