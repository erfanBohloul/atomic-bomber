package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class BodyFactory {

    private final World world;
    private static BodyFactory thisInstance = null;

    private BodyFactory(World world) {
        this.world = world;
    }

    public static BodyFactory getInstance(World world){
        if(thisInstance == null){
            thisInstance = new BodyFactory(world);
        }
        return thisInstance;
    }

    public static BodyFactory getInstance(){
        if (thisInstance == null) {
            throw new RuntimeException("You must call BodyFactory#getInstance() first");
        }
        return thisInstance;
    }

    public static FixtureDef makeFixture(Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;


        fixtureDef.density = 7f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f;
        return fixtureDef;
    }

    public Body makeBoxPolyBody(float posx, float posy, float width, float height, BodyDef.BodyType bodyType, boolean fixedRotation, boolean isSensor){
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width/2, height/2);

        FixtureDef fixtureDef = makeFixture(poly);
        fixtureDef.isSensor = isSensor;
        boxBody.createFixture(fixtureDef);

        poly.dispose();

        return boxBody;
    }

    public Body makeBoxPolyBody(float posx, float posy, float width, float height, BodyDef.BodyType bodyType, boolean fixedRotation){
        return makeBoxPolyBody(posx, posy, width, height, bodyType, fixedRotation, false);
    }
}
