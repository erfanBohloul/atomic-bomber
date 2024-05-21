package com.mygdx.game.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.entity.damager.TankBullet;

import java.util.Random;

public class Tank {

    public Body body;
    public float health;
    public static final float WIDTH = 14;
    public static final float HEIGHT = 10;
    public static final int RELOAD_TIME = 300;
    public int reloadTimer;

    // true -> right
    // false -> left
    public final boolean directionToMove;

    public Tank(float posx, float posy) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.KinematicBody, true);
        body.setUserData(this);

        health = 2;
        reloadTimer = RELOAD_TIME;

        // set direction
        Random rand = new Random();
        directionToMove = rand.nextDouble() < 0.5;

        setVelocity();
    }

    public void setVelocity() {
        float zarib;
        if (directionToMove) zarib = 1;
        else zarib = -1;

        setVelocity(zarib * 5);
    }

    public void setVelocity(float x) {
        body.setLinearVelocity(new Vector2(x, 0));
    }

    public void takeDamage(float damage) {
        System.out.println("health: " + health);
        health -= damage;

        if (health <= 0) {
            GameModel.curr.toBeRemoved.add(body);
            GameModel.curr.calculateBodyScore(body);
        }
    }

    public Vector2 getPosition() {
        return body.getPosition();
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
}
