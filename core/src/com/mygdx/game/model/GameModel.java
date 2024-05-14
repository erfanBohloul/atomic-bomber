package com.mygdx.game.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.loader.GameAssetManager;

public class GameModel {

    public World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private KeyboardController controller;
    private GameAssetManager assetManager;

    private final float GRAVITY_EARTH = -9.8f;

    private Body floor;
    public Body player;

    public GameModel(KeyboardController keyboardController, GameAssetManager assetManager) {
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        this.controller = keyboardController;
        this.assetManager = assetManager;

        createFloor();

        BodyFactory bodyFactory = BodyFactory.getInstance(world);
        player = bodyFactory.makeBoxPolyBody(
          1, 1, 2,2,BodyFactory.GOLD, BodyDef.BodyType.DynamicBody, false
        );
    }

    public void logicStep(float delta) {
        if (controller.down) {
            player.applyForceToCenter(0, -10, true);
        }
        else if (controller.up) {
            player.applyForceToCenter(0, 30, true);
        }

        else if (controller.left) {
            player.applyForceToCenter(-10, 0, true);
        }

        else if (controller.right) {
            player.applyForceToCenter(10, 0, true);
        }

        // applying drag force
        float H = 0.5f;
        Vector2 velocity = player.getLinearVelocity();
        float vsqur = velocity.x * velocity.x + velocity.y * velocity.y;
        float fmag = H * vsqur;

        Vector2 fd = velocity.nor().scl(-fmag);
        player.applyForceToCenter(fd, true);


//        Vector2 gravity = new Vector2(0, GRAVITY_EARTH);
//        player.applyForceToCenter(gravity, true);

        world.step(delta, 6, 2);
    }

    public void createFloor() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        floor = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, .9f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = .5f;
        floor.createFixture(fixtureDef);

        shape.dispose();
    }
}
