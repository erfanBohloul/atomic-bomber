package com.mygdx.game.model.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.GameModel;

public class Player {

    public static int DEFAULT_X = 1, DEFAULT_Y = 1, WIDTH = 10, HEIGHT = 10;

    public Body body;
    public int reloadTimer;
    public Sprite sprite = null;
    public Texture texture;
    public float health;

    public Player(int posx, int posy, int width, int height) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        this.body = bodyFactory.makeBoxPolyBody(posx, posy, width, height, BodyDef.BodyType.DynamicBody, true);
        body.setUserData(this);
        body.setLinearVelocity(new Vector2(2000, 0));
        reloadTimer = 0;
        health = 3;
    }

    public Player() {
        this(DEFAULT_X, DEFAULT_Y, WIDTH, HEIGHT);
    }

    public void setTexture(Texture texture) {
        sprite = new Sprite(texture);
        sprite.setSize(Player.WIDTH, Player.HEIGHT);
    }

    public void render(SpriteBatch batch) {
        if (sprite == null) {
            throw new RuntimeException();
        }


        sprite.setPosition(body.getPosition().x - Player.WIDTH/2f, body.getPosition().y - Player.HEIGHT/2f);
//        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        sprite.draw(batch);

    }

    public boolean readyForBombing() {
        return reloadTimer <= 0;
    }

    public void decreaseReloadTimer() {
        reloadTimer = Math.max(0, reloadTimer - 1);
    }

    public void reload() {
        this.reloadTimer = 30;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getArea() {
        return WIDTH * HEIGHT;
    }

    public void takeDamage(float damage) {
        health -= damage;

        if (health <= 0) {
            GameModel.curr.toBeRemoved.add(body);
        }

        // todo end the game
    }

    public void accelerate() {
        body.setLinearVelocity(body.getLinearVelocity().x * 2, body.getLinearVelocity().y * 2);
    }
}
