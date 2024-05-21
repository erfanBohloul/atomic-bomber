package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.loader.GameAssetManager;
import com.mygdx.game.model.entity.damager.Bomb;
import com.mygdx.game.model.entity.Player;
import com.mygdx.game.model.entity.Tank;
import com.mygdx.game.model.entity.damager.Damager;
import com.mygdx.game.model.entity.damager.TankBullet;
import com.mygdx.game.views.MainScreen;

import java.util.ArrayList;
import java.util.Random;

public class GameModel {

    public World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private KeyboardController controller;
    private GameAssetManager assetManager;
    private static final double DEGREES_TO_RADIANS = (double)(Math.PI/180);

    public Body floor,
            ceil;
    public static final float floorHeight = 50;


    public Player player;
    private BodyFactory bodyFactory;
    public ArrayList<Body> toBeRemoved = new ArrayList<>();
    public ArrayList<Tank> tanks = new ArrayList<>();
    public ArrayList<Damager> bullets = new ArrayList<>();
    private Music boingMusic;

    private MainScreen parent;

    public static GameModel curr;

    public GameModel(MainScreen parent, KeyboardController keyboardController, GameAssetManager assetManager) {
        this.parent = parent;
        curr = this;
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new GameContactListener(this));
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        this.controller = keyboardController;
        this.assetManager = assetManager;
        bodyFactory = BodyFactory.getInstance(world);
        boingMusic = Gdx.audio.newMusic(Gdx.files.internal("music/boing.mp3"));

        createFloor();
        createCeil();
        createPlayer();
        createTank();
    }

    public void createPlayer() {
        player = new Player();
    }

    public void logicStep(float delta) {

        // removing
        handleObjectsOutOfBound();
        removeObjects();


        if (controller.down) {
            player.body.applyForceToCenter(0, -500, true);
        }
        else if (controller.up) {
            player.body.applyForceToCenter(0, 500, true);
        }
        else if (controller.left) {
            player.body.applyForceToCenter(-500, 0, true);
        }
        else if (controller.right) {
            player.body.applyForceToCenter(500, 0, true);
        }
        else if (controller.bomb) {
            dropBomb();
        }

        if (controller.leftBracket) {
            player.body.setTransform(player.getPosition(), player.body.getAngle() + (float) DEGREES_TO_RADIANS * 1);
        }
        else if (controller.rightBracket) {
            player.body.setTransform(player.getPosition(), player.body.getAngle() + (float) DEGREES_TO_RADIANS * (-1f));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            createTank();
        }




        // shoot to player
        tanksShootPlayer();


        // applying drag force
//        applyDragForce(player.body);


        // player
        player.body.setLinearVelocity(MathUtils.cos(player.body.getAngle()) * 10, MathUtils.sin(player.body.getAngle()) * 10);

        player.body.applyForceToCenter(new Vector2(0, player.getArea() * 10), true);
        player.decreaseReloadTimer();
//        System.out.println(player.getPosition().x + " " + player.getPosition().y);
        if (player.getPosition().x >= MainScreen.WIDTH/2f) {
            player.body.setTransform(-MainScreen.WIDTH/2f, player.getPosition().y, 0);
        }
        else if (player.getPosition().x <= -MainScreen.WIDTH/2f) {
            player.body.setTransform(MainScreen.WIDTH/2f, player.getPosition().y, 0);
        }

        // bombs
        for (Damager damager : bullets) {
            damager.body.applyForceToCenter(0, -10, true);
        }

        world.step(delta, 6, 2);
    }

    private void removeObjects() {
        for (Body body : toBeRemoved) {
            removeObjFromData(body.getUserData());
        }

        for (Body body: toBeRemoved) {
            world.destroyBody(body);
        }
        toBeRemoved.clear();
    }

    private void handleObjectsOutOfBound() {
        for (Tank tank : tanks) {
            if (tank.getPosition().x > MainScreen.WIDTH/2f || tank.getPosition().x < -MainScreen.WIDTH/2f) {
                toBeRemoved.add(tank.body);
            }
        }

        for (Damager damager : bullets) {
            if (damager.getPosition().x > MainScreen.WIDTH/2f || damager.getPosition().x < -MainScreen.WIDTH/2f) {
                toBeRemoved.add(damager.body);
            }

            else if (damager.getPosition().y > MainScreen.HEIGHT/2f || damager.getPosition().y < -MainScreen.HEIGHT/2f) {
                toBeRemoved.add(damager.body);
            }
        }
    }

    public void calculateBodyScore(Body body) {
        StringBuilder scoreText = parent.score.getText();

        int score = 0;
        try {
            score = Integer.parseInt(scoreText.substring(7));
        } catch (NumberFormatException e) {
            System.out.println(scoreText.substring(7));
            throw new RuntimeException();
        }
        if (body.getUserData() instanceof Tank) {
            score += 4;
        }

        String newLabel = "score: " + score;
        parent.score.setText(newLabel);
    }

    private void tanksShootPlayer() {
        for (Tank tank : tanks) {
            if (tank.reloadTimer-- > 0)
                continue;

            if (!tank.canSee(player.body))
                continue;

            TankBullet tankBullet = tank.shootBullet(player.getPosition());
            tankBullet.body.setLinearVelocity(tankBullet.velocity);
            bullets.add(tankBullet);
        }
    }

    private void removeObjFromData(Object object) {
        if (object instanceof Tank)
            tanks.remove((Tank) object);

        else if (object instanceof Damager)
            bullets.remove((Damager) object);
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

        Bomb newBomb = new Bomb(player.body);
        bullets.add(newBomb);

        player.reload();
    }

    public void createFloor() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, -MainScreen.HEIGHT / 2f); // set the position to the center of the screen on the x-axis and 0 on the y-axis
        floor = world.createBody(bodyDef);
        floor.setUserData("FLOOR");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Gdx.graphics.getWidth(), 50); // set the box dimensions to half of the screen width and 0.5 on the y-axis
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        floor.createFixture(fixtureDef);


        shape.dispose();
    }

    public void createCeil() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, MainScreen.HEIGHT/2f); // set the position to the center of the screen on the x-axis and 0 on the y-axis
        ceil = world.createBody(bodyDef);
        ceil.setUserData("CEIL");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Gdx.graphics.getWidth(), -25); // set the box dimensions to half of the screen width and 0.5 on the y-axis
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        ceil.createFixture(fixtureDef);


        shape.dispose();
    }

    public void createTank() {
        Tank tank = new Tank(getRandomXPosition(), -MainScreen.HEIGHT / 2f + floorHeight);
        tanks.add(tank);
    }

    private float getRandomXPosition() {
        Random rand = new Random();
        float rand_x;
        do {
            rand_x = rand.nextInt(1000) * (MainScreen.WIDTH / 1000f) - MainScreen.WIDTH / 2f;
        } while (!isRandomSValid(rand_x));

        return rand_x;
    }

    private boolean isRandomSValid(float x) {
        float a, b;
        for (Tank tank : tanks) {
            a = tank.getPosition().x - Tank.WIDTH/2;
            b = a + Tank.WIDTH;
            if (a <= x && x <= b)
                return false;
        }

        return true;
    }
}
