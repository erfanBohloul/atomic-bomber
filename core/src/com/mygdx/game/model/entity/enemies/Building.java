package com.mygdx.game.model.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.entity.Bonus;

public class Building extends Enemy {

    public static final float WIDTH = 10f,
            HEIGHT = 10f;

    public Building(float posx, float posy) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.KinematicBody, true);
        body.setUserData(this);

        health = 3f;

        lastVelocity = Vector2.Zero;
    }

    public static float getArea() {
        return WIDTH * HEIGHT;
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, getPosition().x - WIDTH/2f, getPosition().y - HEIGHT/2f, WIDTH, HEIGHT);
    }

    @Override
    public void takeDamage(float damage) {
        health -= damage;

        if (health <= 0) {
            Bonus bonus = new Bonus(getPosition().x, getPosition().y, Bonus.BonusType.ATOMIC);

            GameModel.curr.toBeRemoved.add(body);
            GameModel.curr.calculateBodyScore(body);
        }
    }
}
