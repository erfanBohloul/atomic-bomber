package com.mygdx.game.model.entity.damager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.entity.Player;

public class Bomb extends Damager {

    public static float WIDTH = 2f,
                        HEIGHT = 2f;

    public Bomb(Body playerBody) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        float posx = playerBody.getPosition().x,
                posy = playerBody.getPosition().y - Player.HEIGHT;

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyType.DynamicBody, false);
        body.setUserData(this);
        body.setLinearVelocity(playerBody.getLinearVelocity().x, playerBody.getLinearVelocity().y - 10);

        damage = 1;
    }

    public float getDamage() {
        return damage;
    }

    float getArea() {
        return WIDTH * HEIGHT;
    }
}
