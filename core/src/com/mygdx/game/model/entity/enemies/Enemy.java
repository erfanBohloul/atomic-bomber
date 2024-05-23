package com.mygdx.game.model.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.model.GameModel;

import java.util.Random;

public abstract class Enemy {
    public Body body;
    public float health;
    public Vector2 lastVelocity;

    // true -> right
    // false -> left
    public boolean directionToMove;

    public Enemy() {
        Random rand = new Random();
        directionToMove = rand.nextDouble() < 0.5;
    }

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void stop() {
        lastVelocity = new Vector2( body.getLinearVelocity() );
        setVelocity(new Vector2(0, 0));
    }

    public void move() {
        setVelocity(lastVelocity);

        System.out.println("tank first velocity: " + lastVelocity);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public abstract void render(SpriteBatch batch, Texture texture);

    public void takeDamage(float damage) {
        health -= damage;

        if (health <= 0) {
            GameModel.curr.toBeRemoved.add(body);
            GameModel.curr.calculateBodyScore(body);
        }
    }
}
