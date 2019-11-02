package com.bressio.rendezvous.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Timer;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.forge.BodyBuilder;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public class Bullet extends Sprite {

    private Match match;
    private Weapon weapon;
    private Body body;
    private Vector2 position;
    private float radius;
    private short categoryBits;
    private short maskBits;
    private int velocity;

    private boolean canDrawTexture;
    private boolean isDestroyed;

    public Bullet(Match match, Weapon weapon) {
        super(match.getResources().getTexture(ResourceHandler.TexturePath.BULLET));
        this.match = match;
        this.weapon = weapon;
        init();
        buildBody();
        delayTextureAppearance();
    }

    private void init() {
        setBounds(0, 0, pScale(getWidth()), pScale(getHeight()));
        setOrigin(pScaleCenter(getWidth()) + .1f, pScaleCenter(getHeight()) + .1f);
        velocity = 10;
        position = weapon.getLastSoldierToShoot().getBody().getPosition();
        radius = 5;
        categoryBits = BULLET_TAG;
        maskBits =  (short) (DEFAULT_TAG | LOOT_TAG | PLAYER_TAG | ENEMY_TAG);
    }

    public void moveForward() {
        Vector2 direction = new Vector2(
                (float) Math.cos(Math.toRadians(getRotation() + 90)),
                        (float) Math.sin(Math.toRadians(getRotation() + 90))
        );
        body.setLinearVelocity(direction.x * velocity, direction.y * velocity);
        setPosition(body.getPosition().x - .1f, body.getPosition().y - .1f);
    }

    private void buildBody() {
        body = new BodyBuilder(match.getWorld(), position)
                .withBodyType(BodyDef.BodyType.DynamicBody)
                .withRadius(pScale(radius))
                .withCategoryBits(categoryBits)
                .withMaskBits(maskBits)
                .withSensor(true)
                .withUserData(this)
                .build();
        setRotation(weapon.getLastSoldierToShoot().getRotation());
    }

    private void delayTextureAppearance() {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                canDrawTexture = true;
            }
        }, .05f);
    }

    @Override
    public void draw(Batch batch) {
        if (canDrawTexture) {
            super.draw(batch);
        }
    }

    public void destroy() {
        match.getBullets().remove(this);
        isDestroyed = true;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }


}
