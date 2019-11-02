package com.bressio.rendezvous.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.bressio.rendezvous.entities.objects.Empty;
import com.bressio.rendezvous.entities.objects.Inventory;
import com.bressio.rendezvous.entities.objects.equipment.armor.Armor;
import com.bressio.rendezvous.entities.objects.equipment.helmets.Helmet;
import com.bressio.rendezvous.entities.projectiles.Bullet;
import com.bressio.rendezvous.forge.BodyBuilder;
import com.bressio.rendezvous.graphics.AnimationRegion;
import com.bressio.rendezvous.graphics.Animator;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public abstract class Soldier extends Entity {

    private final float radius;
    private final float linearDamping;
    private int speed;
    private final short categoryBits;
    private final short maskBits;
    private final Object userData;
    private AnimationRegion animationRegion;
    private Object lastSelectedObjectClass;
    private Object lastEquippedArmorClass;
    private Object lastEquippedHelmetClass;

    private Fixture fixture;

    private Animator animator;
    private int health = 100;
    private int armor = 0;
    private Inventory inventory;

    private Texture pointlight;

    private boolean isFiring;
    private int kills;

    private boolean isDead;

    Soldier(Match match, Vector2 position, float radius, float linearDamping, int speed,
            AnimationRegion animationRegion, short categoryBits, short maskBits, Object userData) {
        super(match, position, animationRegion, ResourceHandler.TextureAtlasPath.SOLDIER_ATLAS);

        this.radius = radius;
        this.linearDamping = linearDamping;
        this.speed = speed;
        this.animationRegion = animationRegion;
        this.categoryBits = categoryBits;
        this.userData = userData;
        this.maskBits = maskBits;
        init();
        buildBody();
    }

    protected void init() {
        animator = new Animator(this, animationRegion);
        setOrigin(pScaleCenter(animationRegion.getFrameWidth()), pScaleCenter(animationRegion.getFrameHeight()));
        setBounds(0, 0, pScale(animationRegion.getFrameWidth()), pScale(animationRegion.getFrameHeight()));
        setRegion(animator.getIdleTexture());
        pointlight = getMatch().getResources().getTexture(ResourceHandler.TexturePath.POINTLIGHT);
    }

    @Override
    protected void buildBody() {
        setBody(new BodyBuilder(getMatch().getWorld(), getPosition())
                .withBodyType(BodyDef.BodyType.DynamicBody)
                .withRadius(pScale(radius))
                .withLinearDamping(linearDamping)
                .withCategoryBits(categoryBits)
                .withMaskBits(maskBits)
                .withUserData(userData)
                .build());
        fixture = getBody().getFixtureList().first();
    }

    public void update(float delta) {
        setPosition(
                getBody().getPosition().x - pCenter(getWidth()),
                getBody().getPosition().y - pCenter(getHeight()));
        setRegion(animator.getFrame(delta, .7f));
        verifyItems();
        verifyHealth();
    }

    protected abstract void verifyItems();

    private void verifyHealth() {
        if (health <= 0) {
            health = 0;
            die();
        }
    }

    protected abstract void die();

    public void takeDangerZoneDamage() {
        changeHealth(-5);
    }

    @Override
    public void draw(Batch batch) {
        if (!isDead) {
            if (isFiring) {
                batch.draw(pointlight,
                        getBody().getPosition().x - pScaleCenter(pointlight.getWidth()),
                        getBody().getPosition().y - pScaleCenter(pointlight.getHeight()),
                        pScale(pointlight.getWidth()),
                        pScale(pointlight.getHeight()));
            }
            super.draw(batch);
        }
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public void switchAnimation(AnimationRegion animationRegion, ResourceHandler.TextureAtlasPath textureAtlasPath) {
        this.animationRegion = animationRegion;
        setRegion(getMatch().getResources().getTextureAtlas(textureAtlasPath).findRegion(animationRegion.getRegion()));
        animator = new Animator(this, animationRegion);
        setBounds(0, 0, pScale(animationRegion.getFrameWidth()), pScale(animationRegion.getFrameHeight()));
        setOrigin(pScaleCenter(animationRegion.getFrameWidth()), pScaleCenter(animationRegion.getFrameHeight()));
    }

    public void changeHealth(int difference) {
        health = health + difference < 0 ? 0 : health + difference > 100 ? 100 : health + difference;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Object getLastSelectedObjectClass() {
        return lastSelectedObjectClass;
    }

    public Object getLastEquippedArmorClass() {
        return lastEquippedArmorClass;
    }

    public Object getLastEquippedHelmetClass() {
        return lastEquippedHelmetClass;
    }

    public void setLastSelectedObjectClass(Object lastSelectedObjectClass) {
        this.lastSelectedObjectClass = lastSelectedObjectClass;
    }

    public void setLastEquippedArmorClass(Object lastEquippedArmorClass) {
        this.lastEquippedArmorClass = lastEquippedArmorClass;
    }

    public void setLastEquippedHelmetClass(Object lastEquippedHelmetClass) {
        this.lastEquippedHelmetClass = lastEquippedHelmetClass;
    }

    public Animator getAnimator() {
        return animator;
    }

    public void getShot(Bullet bullet) {
        if (armor > 0) {
            if (getInventory().getEquipmentItems().get(0).getClass() != Empty.class) {
                ((Helmet)getInventory().getEquipmentItems().get(0)).takeDamage(bullet.getWeapon().getDamage() / 2);
                ((Helmet)getInventory().getEquipmentItems().get(0)).updateName();
            }
            if (getInventory().getEquipmentItems().get(1).getClass() != Empty.class) {
                ((Armor)getInventory().getEquipmentItems().get(1)).takeDamage(bullet.getWeapon().getDamage() / 2);
                ((Armor)getInventory().getEquipmentItems().get(1)).updateName();
            }
        } else {
            health -= bullet.getWeapon().getDamage();
            if (health <= 0) {
                bullet.getWeapon().getLastSoldierToShoot().accountKill();
            }
        }
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    private void accountKill() {
        kills++;
    }

    public int getKills() {
        return kills;
    }
}
