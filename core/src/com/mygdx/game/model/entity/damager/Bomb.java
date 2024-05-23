package com.mygdx.game.model.entity.damager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        this(playerBody.getPosition().x, playerBody.getPosition().y - Player.HEIGHT, playerBody.getLinearVelocity());
    }

    public Bomb(float posx, float posy, Vector2 velocity) {

        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyType.DynamicBody, false);
        body.setUserData(this);
        body.setLinearVelocity(velocity.x, velocity.y - 10);

        damage = 1;
    }

    public Bomb(Vector2 pos, Vector2 velocity) {
        this(pos.x, pos.y, velocity);
    }

    float getArea() {
        return WIDTH * HEIGHT;
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, getPosition().x - WIDTH/2f, getPosition().y - HEIGHT/2f, WIDTH, HEIGHT);
    }
}
