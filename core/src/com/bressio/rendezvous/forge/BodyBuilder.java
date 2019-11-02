package com.bressio.rendezvous.forge;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public class BodyBuilder{

    private World world;
    private Vector2 position;

    private float width;
    private float height;
    private float linearDamping;
    private float radius;
    private float[] vertices;
    private boolean sensor;
    private BodyDef.BodyType bodyType;
    private short categoryBits;
    private short maskBits;
    private Object userData;

    public BodyBuilder(World world, Vector2 position){
        this.world = world;
        this.position = position;
        init();
    }

    private void init() {
        // by default, all objects collide with other default objects, soldiers and bullets
        this.categoryBits = DEFAULT_TAG;
        this.maskBits = DEFAULT_TAG | PLAYER_TAG | BULLET_TAG | ENEMY_TAG;
    }

    public BodyBuilder withWidth(float width) {
        this.width = width;
        return this;
    }

    public BodyBuilder withHeight(float height) {
        this.height = height;
        return this;
    }

    public BodyBuilder withLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
        return this;
    }

    public BodyBuilder withRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public BodyBuilder withVertices(float[] vertices) {
        this.vertices = vertices;
        return this;
    }

    public BodyBuilder withBodyType(BodyDef.BodyType bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public BodyBuilder withSensor(boolean isSensor) {
        this.sensor = isSensor;
        return this;
    }

    public BodyBuilder withCategoryBits(short categoryBits) {
        this.categoryBits = categoryBits;
        return this;
    }

    public BodyBuilder withMaskBits(short maskBits) {
        this.maskBits = maskBits;
        return this;
    }

    public BodyBuilder withUserData(Object userData) {
        this.userData = userData;
        return this;
    }

    public Body build(){
        Body body;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;

        bodyDef.linearDamping = linearDamping;
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        body = world.createBody(bodyDef);

        fixtureDef.isSensor = sensor;

        if (width != 0 && height != 0) {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width, height);
            fixtureDef.shape = shape;
        } else if (radius != 0) {
            CircleShape shape = new CircleShape();
            shape.setRadius(radius);
            fixtureDef.shape = shape;
        } else if (vertices.length > 0) {
            PolygonShape shape = new PolygonShape();
            shape.set(vertices);
            fixtureDef.shape = shape;
        }

        body.createFixture(fixtureDef).setUserData(userData);

        return body;
    }
}