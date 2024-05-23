package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.controller.KeyboardController;
import com.mygdx.game.loader.GameAssetManager;
import com.mygdx.game.model.entity.damager.*;
import com.mygdx.game.model.entity.Player;
import com.mygdx.game.model.entity.enemies.*;
import com.mygdx.game.views.MainScreen;

import java.util.ArrayList;
import java.util.Random;

public class GameModel {

    public World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private KeyboardController controller;
    public GameAssetManager assetManager;
    private static final double DEGREES_TO_RADIANS = (double)(Math.PI/180);

    public Body floor,
            ceil;
    public static final float floorHeight = 50;


    public Player player;
    public ArrayList<Body> toBeRemoved = new ArrayList<>();
    public ArrayList<Body> toBeAdded = new ArrayList<>();
    public ArrayList<Body> entities = new ArrayList<>();
    private Music boingMusic;
    public MainScreen parent;

    // game meta data:
    private int numKills,
            numCluster,
            numAtomic;

    private float frozenProgress;
    private boolean freezeStatus = false;
    private final double freezeTime = 3;

    private double timer = 0;
    public static GameModel curr;

    public int wave = 0;
    public int numBombs, numOnTarget;

    public GameModel(MainScreen parent, KeyboardController keyboardController, GameAssetManager assetManager) {
        this.parent = parent;
        curr = this;
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new GameContactListener(this));
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        this.controller = keyboardController;
        this.assetManager = assetManager;
        BodyFactory bodyFactory = BodyFactory.getInstance(world);
        boingMusic = Gdx.audio.newMusic(Gdx.files.internal("music/boing.mp3"));

        createFloor();
        createCeil();
        createPlayer();

        frozenProgress = 0; //default
    }

    public void setFreezeStatus(boolean freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public boolean getFreezeStatus() {
        return freezeStatus;
    }

    public void createPlayer() {
        player = new Player();
    }

    public void logicStep(float delta) {
        handleDamagersOutOfBound();
        handleEnemiesOutOfBound();

        // removing
        removeObjects();
        // adding
        addObjects();

        // handle frozen state
        if (freezeStatus) {
            timer += delta;

            if (timer >= freezeTime) {
                timer = 0;
                unFreezeAction();
            }
        }

        handleInputs();
        // shoot to player
        tanksShootPlayer();


        // player
        handlePlayer();

        // bombs
        world.step(delta, 6, 2);
    }

    public void loadEnemies() {
        createTank();
        createTank();
        createTank();
        createTree();
        createBuilding();
        createTruck();
        createTruck();
    }

    public void startNextWave() {
        wave++;
        numBombs = 0;
        numOnTarget = 0;

        loadEnemies();
    }

    public float getAccuracy() {
        if (numBombs <= 0)  return 0;
        return (float) numOnTarget / (float) numBombs;
    }

    public boolean waveHasEnded() {
        return getEnemies().isEmpty();
    }

    private void handleInputs() {
        if (controller.bomb) {
            dropBomb();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            dropCluster();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            dropAtomic();
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            freezeAction();
        }

        if (controller.leftBracket) {
            player.body.setTransform(player.getPosition(), player.body.getAngle() + (float) DEGREES_TO_RADIANS * 2);
        }
        else if (controller.rightBracket) {
            player.body.setTransform(player.getPosition(), player.body.getAngle() + (float) DEGREES_TO_RADIANS * (-2f));
        }

        if (controller.accelerate) {
            player.accelerate();
        }

        // cheat codes
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            createTank();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            numCluster++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            numAtomic++;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            frozenProgress = 1f;
        }

    }

    private void unFreezeAction() {
        setFreezeStatus(false);

        ArrayList<Enemy> enemies = getEnemies();
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    private void freezeAction() {
        if (!canFreeze()) {
            return;
        }

        frozenProgress = 0f;
        setFreezeStatus(true);

        // remove all enemy damagers
        ArrayList<Damager> damagers = getDamagers();
        for (Damager damager : damagers) {
            if (damager instanceof TankBullet)
                toBeRemoved.add(damager.body);
        }


        // set velocity of enemies to zero
        ArrayList<Enemy> enemies = getEnemies();
        for (Enemy enemy : enemies) {
            enemy.stop();
        }
    }

    public ArrayList<Enemy> getEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        for (Body body : entities) {
            if (body.getUserData() instanceof Enemy)
                enemies.add((Enemy) body.getUserData());
        }

        return enemies;
    }

    private boolean canFreeze() {
        return frozenProgress >= 1f && !getFreezeStatus();
    }

    private void addObjects() {
        entities.addAll(toBeAdded);
        toBeAdded.clear();
    }

    private void removeObjects() {
        for (Body body: toBeRemoved) {
            entities.remove(body);
            world.destroyBody(body);
        }
        toBeRemoved.clear();
    }

    private void handlePlayer() {
        player.body.setLinearVelocity(MathUtils.cos(player.body.getAngle()) * 25, MathUtils.sin(player.body.getAngle()) * 25);

        player.body.applyForceToCenter(new Vector2(0, player.getArea() * 10), true);
        player.decreaseReloadTimer();
        handlePlayerOutOfBound();
    }

    private void handlePlayerOutOfBound() {
        if (player.getPosition().x >= MainScreen.WIDTH/2f) {
            player.body.setTransform(-MainScreen.WIDTH/2f, player.getPosition().y, player.body.getAngle());
        }
        else if (player.getPosition().x <= -MainScreen.WIDTH/2f) {
            player.body.setTransform(MainScreen.WIDTH/2f, player.getPosition().y, player.body.getAngle());
        }
    }

    private void handleDamagersOutOfBound() {
        ArrayList<Damager> damagers = getDamagers();
        for (Damager damager : damagers) {
            if (damager.getPosition().x > MainScreen.WIDTH/2f || damager.getPosition().x < -MainScreen.WIDTH/2f) {
                toBeRemoved.add(damager.body);
            }

            else if (damager.getPosition().y > MainScreen.HEIGHT/2f || damager.getPosition().y < -MainScreen.HEIGHT/2f) {
                toBeRemoved.add(damager.body);
            }
        }
    }

    private void handleEnemiesOutOfBound() {
        ArrayList<Enemy> enemies = getEnemies();
        for (Enemy enemy : enemies) {
            if (enemy.getPosition().x >= MainScreen.WIDTH/2f) {
                enemy.body.setTransform(-MainScreen.WIDTH/2f, enemy.getPosition().y, 0);
            }
            else if (enemy.getPosition().x <= -MainScreen.WIDTH/2f) {
                enemy.body.setTransform(MainScreen.WIDTH/2f, enemy.getPosition().y, 0);
            }
        }
    }

    public void calculateBodyScore(Body body) {
        float n = 0;
        if (body.getUserData() instanceof Tank) {
            n = 4;
        }
        else if (body.getUserData() instanceof Truck) {
            n = 2;
        }
        else if (body.getUserData() instanceof Building) {
            n = 15;
        }

        addKills( (int) n);
        addFrozenProgress(n / 20f);
    }

    private void addFrozenProgress(float n) {
        setFrozenProgress(Math.min(1, getFrozenProgress() + n));
    }

    private void tanksShootPlayer() {
        if (getFreezeStatus())
            return;

        ArrayList<Tank> tanks = getTanks();
        for (Tank tank : tanks) {
            if (tank.reloadTimer-- > 0)
                continue;

            if (!tank.canSee(player.body))
                continue;

            TankBullet tankBullet = tank.shootBullet(player.getPosition());
            tankBullet.body.setLinearVelocity(tankBullet.velocity);
            entities.add(tankBullet.body);
        }
    }

    public ArrayList<Damager> getDamagers() {
        ArrayList<Damager> damagers = new ArrayList<>();
        for (Body body : entities) {
            if (body.getUserData() instanceof Damager)
                damagers.add((Damager) body.getUserData());
        }

        return damagers;
    }

    public ArrayList<ClusterBomb> getClusters() {
        ArrayList<ClusterBomb> clusters = new ArrayList<>();
        for (Body body : entities) {
            if (body.getUserData() instanceof ClusterBomb)
                clusters.add((ClusterBomb) body.getUserData());
        }

        return clusters;
    }

    public ArrayList<Tank> getTanks() {
        ArrayList<Tank> tanks = new ArrayList<>();
        for (Body body : entities) {
            if (body.getUserData() instanceof Tank)
                tanks.add((Tank) body.getUserData());
        }

        return tanks;
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

    private void dropAtomic() {
        if (!player.readyForBombing())
            return;

        if (getNumAtomic() <= 0)
            return;

        numAtomic--;

        AtomicBomb atomic = new AtomicBomb(player.body);
        entities.add(atomic.body);

        player.reload();
    }

    private void dropCluster() {
        if (!player.readyForBombing())
            return;

        if (numCluster <= 0)
            return;

        numCluster--;

        ClusterBomb cluster = new ClusterBomb(player.body);
        entities.add(cluster.body);

        player.reload();
    }

    private void dropBomb() {
        if ( !player.readyForBombing() )
            return;

        Bomb newBomb = new Bomb(player.body);
        entities.add(newBomb.body);

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
        entities.add(tank.body);
        tank.move();
    }

    public void createTree() {
        Tree tree = new Tree(getRandomXPosition(), -MainScreen.HEIGHT / 2f + floorHeight);
        entities.add(tree.body);
    }

    public void createBuilding() {
        Building building = new Building(getRandomXPosition(), -MainScreen.HEIGHT / 2f + floorHeight);
        entities.add(building.body);
    }

    public void createTruck() {
        Truck truck = new Truck(getRandomXPosition(), -MainScreen.HEIGHT / 2f + floorHeight);
        entities.add(truck.body);

        truck.move();
    }

    public float getFrozenProgress() {
        return frozenProgress;
    }

    public void setFrozenProgress(float frozenProgress) {
        this.frozenProgress = frozenProgress;
    }

    public int getNumKills() {
        return numKills;
    }

    public int getNumAtomic() {
        return numAtomic;
    }

    public int getNumCluster() {
        return numCluster;
    }

    public void setNumAtomic(int numAtomic) {
        this.numAtomic = numAtomic;
    }

    public void setNumKills(int numKills) {
        this.numKills = numKills;
    }

    public void setNumCluster(int numCluster) {
        this.numCluster = numCluster;
    }

    public void addKills(int add) {
        setNumKills(getNumKills() + add);
    }

    public void addCluster() {
        setNumCluster(getNumCluster() + 1);
    }

    public void addAtomic() {
        setNumAtomic(getNumAtomic() + 1);
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
        ArrayList<Tank> tanks = getTanks();
        for (Tank tank : tanks) {
            a = tank.getPosition().x - Tank.WIDTH/2;
            b = a + Tank.WIDTH;
            if (a <= x && x <= b)
                return false;
        }

        return true;
    }
}
