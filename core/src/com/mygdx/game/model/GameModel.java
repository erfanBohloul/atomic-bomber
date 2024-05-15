package com.mygdx.game.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.loader.GameAssetManager;
import com.mygdx.game.model.entity.Bomb;
import com.mygdx.game.model.entity.Player;

import java.util.ArrayList;

public class GameModel {

    public World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private KeyboardController controller;
    private GameAssetManager assetManager;
    private int reloadingCounter = 0;

    private final float GRAVITY_EARTH = -9.8f;

    private Body floor;
    public Player player;
    private ArrayList<Bomb> bombs;
    private BodyFactory bodyFactory;
    public ArrayList<Body> toBeRemoved = new ArrayList<>();

    public GameModel(KeyboardController keyboardController, GameAssetManager assetManager) {
        world = new World(new Vector2(0, -3), true);
        world.setContactListener(new GameContactListener(this));
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        this.controller = keyboardController;
        this.assetManager = assetManager;
        bodyFactory = BodyFactory.getInstance(world);
        bombs = new ArrayList<>();

        createFloor();
        createPlayer();
    }

    public void createPlayer() {
        player = new Player();
    }

    public void logicStep(float delta) {

        // removing
        for (Body body: toBeRemoved) {
            world.destroyBody(body);
        }
        toBeRemoved.clear();


        if (controller.down) {
            player.body.applyForceToCenter(0, -10, true);
        }
        else if (controller.up) {
            player.body.applyForceToCenter(0, 30, true);
        }
        else if (controller.left) {
            player.body.applyForceToCenter(-10, 0, true);
        }
        else if (controller.right) {
            player.body.applyForceToCenter(10, 0, true);
        }
        if (controller.bomb) {
            dropBomb();
        }

        // applying drag force
        applyDragForce(player.body);

        // player
        player.decreaseReloadTimer();

        world.step(delta, 6, 2);
    }

    private static void applyDragForce(Body body) {
        Vector2 v = body.getLinearVelocity();
        float dragForce = getDragForce(v);
        body.applyForceToCenter(v.nor().scl(dragForce), true);
    }

    private static float getDragForce(Vector2 v) {
        float H = 0.5f;
        float vsqur = v.x * v.x + v.y * v.y;
        float fmag = H * vsqur;
        return -fmag;
    }

    private void dropBomb() {
        if ( !player.readyForBombing() )
            return;

        player.reload();

        Bomb newBomb = new Bomb(player.getPosition().x, player.getPosition().y);
        bombs.add(newBomb);
    }

    public void createFloor() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        floor = world.createBody(bodyDef);
        floor.setUserData("FLOOR");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, .9f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        floor.createFixture(fixtureDef);

        shape.dispose();
    }
}
