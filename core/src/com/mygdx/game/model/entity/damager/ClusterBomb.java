package com.mygdx.game.model.entity.damager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.GameModel;
import com.mygdx.game.model.entity.Player;

import java.util.ArrayList;

public class ClusterBomb extends Damager {

    public static float WIDTH = 2.5f,
            HEIGHT = 2.5f;

    public Texture texture = null;
    public Sprite sprite;
    private final Vector2 firstPos;

    public ClusterBomb(Body playerBody) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        float posx = playerBody.getPosition().x,
            posy = playerBody.getPosition().y - Player.HEIGHT;

        body = bodyFactory.makeBoxPolyBody(posx, posy, WIDTH, HEIGHT, BodyDef.BodyType.DynamicBody, false);
        body.setUserData(this);
        body.setLinearVelocity(playerBody.getLinearVelocity().x, playerBody.getLinearVelocity().y - 10);

        firstPos = new Vector2(posx, posy);

        sprite = new Sprite();
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setSize(WIDTH, HEIGHT);

        damage = 3;
    }

    public void render(SpriteBatch batch, Texture texture) {
        if (canExplode()) {
            GameModel.curr.toBeRemoved.add(body);

            ArrayList<Body> bombs = new ArrayList<>();
            // draw 5 regular bomb in 5 direction
            bombs.add(new Bomb(body.getPosition(), new Vector2(16, 0)).body);
            bombs.add(new Bomb(body.getPosition(), new Vector2(4, -4)).body);
            bombs.add(new Bomb(body.getPosition(), new Vector2(0, -16)).body);
            bombs.add(new Bomb(body.getPosition(), new Vector2(-4, -4)).body);
            bombs.add(new Bomb(body.getPosition(), new Vector2(-16, 0)).body);

            GameModel.curr.toBeAdded.addAll(bombs);
        }

        else {
            batch.draw(texture, getPosition().x-ClusterBomb.WIDTH/2f, getPosition().y-ClusterBomb.HEIGHT/2F, ClusterBomb.WIDTH, ClusterBomb.HEIGHT);
        }
    }

    public boolean canExplode() {
        Vector2 dist = body.getPosition().sub(firstPos);
        return dist.x * dist.x + dist.y * dist.y > 100;
    }

}
