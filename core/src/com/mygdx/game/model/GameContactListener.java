package com.mygdx.game.model;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.model.entity.Player;
import com.mygdx.game.model.entity.damager.Bomb;
import com.mygdx.game.model.entity.Tank;
import com.mygdx.game.model.entity.damager.Damager;

import javax.swing.*;

public class GameContactListener implements ContactListener {

    private GameModel parent;

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

        System.out.println("Contact: " + bodyA.getUserData() + " " + bodyB.getUserData());

        damagerFloor(bodyA, bodyB);
        bombTank(bodyA, bodyB);
        damagerPlayer(bodyB, bodyA);

    }

    private void damagerPlayer(Body bodyA, Body bodyB) {
        if (bodyA.getUserData() instanceof Player) {
            if (bodyB.getUserData() instanceof Damager) {
                parent.toBeRemoved.add(bodyB);

                Player player = (Player) bodyA.getUserData();
                Damager damager = (Damager) bodyB.getUserData();
                player.takeDamage(damager.damage);
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

    private void bombTank (Body bodyA, Body bodyB) {
        if (bodyA.getUserData() instanceof Tank) {
            if (bodyB.getUserData() instanceof Bomb) {
                parent.toBeRemoved.add(bodyB);

                Tank tank = (Tank) bodyA.getUserData();
                Bomb bomb = (Bomb) bodyB.getUserData();
                tank.takeDamage(bomb.damage);
            }
        }

        else if (bodyB.getUserData() instanceof Tank) {
            if (bodyA.getUserData() instanceof Bomb) {
                parent.toBeRemoved.add(bodyA);


                Tank tank = (Tank) bodyB.getUserData();
                Bomb bomb = (Bomb) bodyA.getUserData();
                tank.takeDamage(bomb.damage);
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
