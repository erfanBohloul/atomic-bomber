package com.mygdx.game.model;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.entity.Player;
import com.mygdx.game.model.entity.damager.AtomicBomb;
import com.mygdx.game.model.entity.damager.Bomb;
import com.mygdx.game.model.entity.damager.ClusterBomb;
import com.mygdx.game.model.entity.enemies.Enemy;
import com.mygdx.game.model.entity.enemies.Tank;
import com.mygdx.game.model.entity.damager.Damager;

public class GameContactListener implements ContactListener {

    private final GameModel parent;

    public GameContactListener(GameModel model) {
        this.parent = model;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        if (bodyA == null || bodyB == null) {
            return;
        }

        System.out.println("Contact: " + bodyA.getUserData().getClass().getSimpleName() + " " + bodyB.getUserData().getClass().getSimpleName());

        if (bodyA.getUserData() instanceof AtomicBomb) {
            atomicAffector(bodyA, bodyB);
        }

        else if (bodyB.getUserData() instanceof AtomicBomb) {
            atomicAffector(bodyB, bodyA);
        }

        floorTank(bodyA, bodyB);
        damagerFloor(bodyA, bodyB);
        damagerEnemy(bodyA, bodyB);
        damagerPlayer(bodyB, bodyA);
        damagerDamager(bodyA, bodyB);
    }

    private void damagerDamager(Body bodyA, Body bodyB) {
        if (bodyA.getUserData() instanceof Damager &&
            bodyA.getType().equals(BodyDef.BodyType.DynamicBody)) {

            if (bodyB.getUserData() instanceof Damager &&
                bodyB.getType().equals(BodyDef.BodyType.KinematicBody)) {
                parent.toBeRemoved.add(bodyA);
                parent.toBeRemoved.add(bodyB);
            }
        }


        else if (bodyB.getUserData() instanceof Damager &&
                bodyB.getType().equals(BodyDef.BodyType.DynamicBody)) {

            if (bodyA.getUserData() instanceof Damager &&
                    bodyA.getType().equals(BodyDef.BodyType.KinematicBody)) {
                parent.toBeRemoved.add(bodyA);
                parent.toBeRemoved.add(bodyB);
            }
        }
    }

    private void atomicAffector(Body atomic, Body body) {
        if (body.getUserData() instanceof Player ||
            body.getUserData() instanceof Damager)
            return;

        parent.toBeRemoved.addAll(parent.entities);
    }

    private void floorTank(Body bodyA, Body bodyB) {
        if (bodyA.getUserData() instanceof Tank) {
            if (bodyB.getUserData().equals("Floor")) {
                Tank tank = (Tank) bodyA.getUserData();
                tank.body.applyForceToCenter(0, Tank.getArea() * 10, true);
            }
        }


        if (bodyB.getUserData() instanceof Tank) {
            if (bodyA.getUserData().equals("Floor")) {
                Tank tank = (Tank) bodyB.getUserData();
                tank.body.applyForceToCenter(0, Tank.getArea() * 10, true);
            }
        }
    }

    private void damagerPlayer(Body bodyA, Body bodyB) {
        if (bodyA.getUserData() instanceof Player) {
            if (bodyB.getUserData() instanceof Damager) {
                Player player = (Player) bodyA.getUserData();
                Damager damager = (Damager) bodyB.getUserData();
                player.takeDamage(damager.damage);

                parent.toBeRemoved.add(bodyB);
            }
        }

        else if (bodyB.getUserData() instanceof Player) {
            if (bodyA.getUserData() instanceof Damager) {
                parent.toBeRemoved.add(bodyA);


                Player player = (Player) bodyB.getUserData();
                Damager damager = (Damager) bodyA.getUserData();
                player.takeDamage(damager.damage);
            }
        }
    }

    private void damagerEnemy (Body bodyA, Body bodyB) {
        if (bodyA.getUserData() instanceof Enemy) {
            if (bodyB.getUserData() instanceof Damager) {


                if (bodyB.getUserData() instanceof Bomb || bodyB.getUserData() instanceof ClusterBomb) {
                    parent.numOnTarget++;
                }

                parent.toBeRemoved.add(bodyB);

                Enemy enemy = (Enemy) bodyA.getUserData();
                Damager bomb = (Damager) bodyB.getUserData();
                enemy.takeDamage(bomb.damage);
            }
        }

        else if (bodyB.getUserData() instanceof Enemy) {
            if (bodyA.getUserData() instanceof Damager) {
                if (bodyA.getUserData() instanceof Bomb || bodyA.getUserData() instanceof ClusterBomb) {
                    parent.numOnTarget++;
                }

                parent.toBeRemoved.add(bodyA);


                Enemy enemy = (Enemy) bodyB.getUserData();
                Damager bomb = (Damager) bodyA.getUserData();
                enemy.takeDamage(bomb.damage);
            }
        }
    }

    private void damagerFloor(Body bodyA, Body bodyB) {
        if (bodyA.getUserData().equals("FLOOR")) {
            if (bodyB.getUserData() instanceof Damager) {

                parent.toBeRemoved.add(bodyB);
            }
        }

        if (bodyB.getUserData().equals("FLOOR")) {
            if (bodyA.getUserData() instanceof Damager) {
                parent.toBeRemoved.add(bodyA);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
