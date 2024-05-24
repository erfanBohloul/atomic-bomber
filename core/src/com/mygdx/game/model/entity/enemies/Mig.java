package com.mygdx.game.model.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.entity.damager.TankBullet;

public class Mig extends Enemy{

    public static final float WIDTH = 10, HEIGHT = 10;
    public static final int RELOAD_TIME = 60;
    public int reloadTimer;

    public Mig(float posx, float posy) {
        BodyFactory bodyFactory = BodyFactory.getInstance();

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.KinematicBody, true);
        body.setUserData(this);

        health = 5;

        lastVelocity = new Vector2(10, 0);
    }

    public TankBullet shootBullet(Vector2 direction) {
        reloadTimer = RELOAD_TIME;
        float tmp;
        if (direction.y > getPosition().y)  tmp = getPosition().y + HEIGHT/2f;
        else    tmp = getPosition().y - HEIGHT/2f;

        return new TankBullet(getPosition().x, getPosition().y + tmp, direction);
    }

    public boolean canSee(Body body) {
        Vector2 distance = body.getPosition().sub(getPosition());
        return distance.x * distance.x + distance.y * distance.y < 10000;
    }

    public static float getArea() {
        return WIDTH * HEIGHT;
    }

    public void decreaseReloadTimer() {
        reloadTimer = Math.max(reloadTimer-1, 0);
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, getPosition().x - WIDTH/2f, getPosition().y - HEIGHT/2f, WIDTH, HEIGHT);
    }
}
