package com.mygdx.game.model.entity.damager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.entity.Player;

public class AtomicBomb extends Damager {

    public static float WIDTH = 4f,
                        HEIGHT = 4f;

    public AtomicBomb(Body playerBody) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        float posx = playerBody.getPosition().x;
        float posy = playerBody.getPosition().y - Player.HEIGHT;
        Vector2 velocity = playerBody.getLinearVelocity();

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.DynamicBody, false);
        body.setUserData(this);
        body.setLinearVelocity(velocity.x, velocity.y - 10);

        damage = 10;
    }

    public float getArea() {
        return WIDTH * HEIGHT;
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, getPosition().x - WIDTH/2f, getPosition().y - HEIGHT/2f, WIDTH, HEIGHT);
    }
}
