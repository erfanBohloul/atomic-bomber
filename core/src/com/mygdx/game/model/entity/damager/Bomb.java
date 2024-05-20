package com.mygdx.game.model.entity.damager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.model.BodyFactory;

public class Bomb extends Damager {

    public static float WIDTH = 2f,
                        HEIGHT = 2f;

    public Bomb(float posx, float posy) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyType.DynamicBody, false);
        body.setUserData(this);

        damage = 1;
    }

    public float getDamage() {
        return damage;
    }

    float getArea() {
        return WIDTH * HEIGHT;
    }
}
