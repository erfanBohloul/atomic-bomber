package com.mygdx.game.model.entity.damager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;

public class TankBullet extends Damager {

    public static final float WIDTH = 2;
    public static final float HEIGHT = 2;
    public Vector2 velocity;

    public TankBullet(float posx, float posy, Vector2 target) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new RuntimeException("BodyFactory is null");
        }

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.KinematicBody, false);
        body.setUserData(this);

        velocity = target.sub(body.getPosition()).scl(10);
        damage = 1;
    }

    public static float getArea() {
        return WIDTH * HEIGHT;
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, getPosition().x - WIDTH/2f, getPosition().y - HEIGHT/2f, WIDTH, HEIGHT);
    }
}
