package com.mygdx.game.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.model.BodyFactory;
import com.mygdx.game.model.GameModel;

import java.util.ArrayList;

public class Bonus {

    public static final float WIDTH = 4f,
                                HEIGHT = 4f;

    public static ArrayList<Bonus> bonuses = new ArrayList<>();

    public enum BonusType {
        CLUSTER,
        ATOMIC
    }

    public BonusType type;
    public Vector2 vel;
    public Sprite sprite;

    public Bonus(float posx, float posy, BonusType type) {
        BodyFactory bodyFactory = BodyFactory.getInstance();
        if (bodyFactory == null) {
            throw new AssertionError();
        }

        this.type = type;

        vel = new Vector2(0, 10);

        Texture texture;
        if (type.equals(BonusType.ATOMIC)) {
            texture = GameModel.curr.parent.bonusAtomicTexture;
        }
        else if (type.equals(BonusType.CLUSTER)) {
            texture = GameModel.curr.parent.bonusClusterTexture;
        }
        else {
            throw new RuntimeException();
        }

        sprite = new Sprite(texture);
        sprite.setPosition(posx, posy);
        sprite.setSize(WIDTH, HEIGHT);

        bonuses.add(this);
    }

    public void render(SpriteBatch batch, float delta) {

        // if out of the frame:
        if (sprite.getY() > Gdx.graphics.getHeight()) {
            bonuses.remove(this);
            return;
        }

        if (colideWithPlayer()) {
            if (type.equals(BonusType.CLUSTER))
                GameModel.curr.addCluster();
            else if (type.equals(BonusType.ATOMIC))
                GameModel.curr.addAtomic();

            bonuses.remove(this);
        }


        float dx = vel.cpy().scl(delta).x,
                dy = vel.cpy().scl(delta).y;
        sprite.setPosition(sprite.getX() + dx, sprite.getY() + dy);
        sprite.draw(batch);
    }

    public boolean colideWithPlayer() {
        Player player = GameModel.curr.player;
        return player.getPosition().x - Player.WIDTH/2f <= sprite.getX() &&
            sprite.getX() <= player.getPosition().x + Player.WIDTH/2f &&
            player.getPosition().y - Player.HEIGHT/2f <= sprite.getY() &&
            sprite.getY() <= player.getPosition().y + Player.HEIGHT/2f;
    }

}
