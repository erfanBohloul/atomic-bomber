package com.mygdx.game.model.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.model.BodyFactory;

public class Bomb {

    public Body body;

    public Bomb(float posx, float posy) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        body = bodyFactory.makeBoxPolyBody(posx, posy, 0.5f, 0.5f, BodyType.DynamicBody, false);
        body.setUserData("BOMB");
    }


}
