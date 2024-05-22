package com.mygdx.game.model.entity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Enemy {
    public Body body;
    public float health;
    public Vector2 lastVelocity;

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void stop() {
        lastVelocity = new Vector2( body.getLinearVelocity() );
        setVelocity(Vector2.Zero);

        System.out.println("tank last velocity: " + lastVelocity);
    }

    public void move() {
        setVelocity(lastVelocity);

        System.out.println("tank first velocity: " + lastVelocity);
    }
}
