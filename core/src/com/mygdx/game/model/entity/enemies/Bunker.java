package com.mygdx.game.model.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.entity.Bonus;

public class Bunker extends Enemy{

    public final static float WIDTH = 32,
        HEIGHT = 32;

    public Bunker(float posx, float posy) {
        BodyFactory bodyFactory = BodyFactory.getInstance();

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.KinematicBody, true);
        body.setUserData(this);

        health = 10;

        Vector2 velocity = new Vector2(0, 0);
        lastVelocity = velocity;
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, getPosition().x - WIDTH/2f, getPosition().y - HEIGHT/2f, WIDTH, HEIGHT);
    }

    @Override
    public void takeDamage(float damage) {
        health -= damage;

        if (health <= 0) {
            Bonus bonus = new Bonus(getPosition().x, getPosition().y, Bonus.BonusType.CLUSTER);

            GameModel.curr.toBeRemoved.add(body);
            GameModel.curr.calculateBodyScore(body);
        }
    }
}
