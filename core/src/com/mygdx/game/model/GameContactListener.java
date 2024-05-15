package com.mygdx.game.model;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

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

        if (fixtureA.getBody().getType() == BodyType.StaticBody) {
            if (bodyB.getUserData().equals("BOMB")) {
                parent.toBeRemoved.add(bodyB);
            }
        }

        if (fixtureB.getBody().getType() == BodyType.StaticBody) {
            if (bodyA.getUserData().equals("BOMB")) {
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
