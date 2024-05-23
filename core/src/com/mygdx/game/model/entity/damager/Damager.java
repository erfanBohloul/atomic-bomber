package com.mygdx.game.model.entity.damager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Damager {
    public float damage;
    public Body body;
    public Vector2 getPosition() {
        return body.getPosition();
    }

    public abstract void render(SpriteBatch batch, Texture texture);
}
