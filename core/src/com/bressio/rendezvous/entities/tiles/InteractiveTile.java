package com.bressio.rendezvous.entities.tiles;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.forge.BodyBuilder;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public abstract class InteractiveTile {

    private TiledMap map;
    private Body body;
    private Fixture fixture;
    private Rectangle bounds;
    private boolean isSensor;
    private Match match;
    private short categoryBits;

    InteractiveTile(Rectangle bounds, Match match, boolean isSensor, short categoryBits) {
        this.map = match.getOverMap();
        this.bounds = bounds;
        this.isSensor = isSensor;
        this.match = match;
        this.categoryBits = categoryBits;
        buildBody();
    }

    private void buildBody() {
        body = new BodyBuilder(
                match.getWorld(), pScale(bounds.getX() + pCenter(bounds.getWidth()),
                bounds.getY() + pCenter(bounds.getHeight())))
                .withBodyType(BodyDef.BodyType.StaticBody)
                .withWidth(pScaleCenter(bounds.getWidth()))
                .withHeight(pScaleCenter(bounds.getHeight()))
                .withSensor(isSensor)
                .withCategoryBits(categoryBits)
                .build();

        fixture = body.getFixtureList().first();
    }

    public abstract void onPlayerEnter();

    public abstract void onPlayerLeave();

    public abstract void onEnemyEnter(Enemy enemy);

    public abstract void onEnemyLeave(Enemy enemy);

    TiledMap getMap() {
        return map;
    }

    public Body getBody() {
        return body;
    }

    Fixture getFixture() {
        return fixture;
    }

    public Match getMatch() {
        return match;
    }
}
