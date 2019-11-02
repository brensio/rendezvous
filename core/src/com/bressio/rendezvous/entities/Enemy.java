package com.bressio.rendezvous.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bressio.rendezvous.entities.objects.NPCInventory;
import com.bressio.rendezvous.events.AI;
import com.bressio.rendezvous.events.SteeringBehavior;
import com.bressio.rendezvous.graphics.AnimationRegion;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public class Enemy extends Soldier implements SteeringBehavior, Lootable {

    private AI ai;
    private Vector2 target;
    private boolean isVisible;
    private boolean playerIsColliding;
    private Texture interactionButton;

    public Enemy(Match match, float radius, float linearDamping, int speed, Vector2 position) {
        super(match, position, radius, linearDamping, speed, AnimationRegion.SOLDIER, ENEMY_TAG,
                (short) (DEFAULT_TAG | BUILDING_TAG | LOOT_TAG | WATER_TAG | PLAYER_TAG | ENEMY_TAG | BULLET_TAG),
                null);
        isVisible = true;
        setUserData();
    }

    private void updateRotation(float delta) {
        if (target != null) {
            float angle = MathUtils.radiansToDegrees * MathUtils.atan2(
                    target.y - (getY() + pCenter(getHeight())),
                    target.x - (getX() + pCenter(getWidth())));

            if(angle < 0){
                angle += 360;
            }
            setRotation(angle - 90);
        }
    }

    @Override
    public void draw(Batch batch) {
        if (isVisible) {
            super.draw(batch);
        }
    }

    @Override
    protected void init() {
        super.init();
        setInventory(new NPCInventory(getMatch(), this));
        ai = new AI(getMatch(), this);
        interactionButton = getMatch().getResources().getTexture(ResourceHandler.TexturePath.INTERACTION_BUTTON);
    }

    @Override
    public void update(float delta) {
        if (!isDead()) {
            super.update(delta);
            getInventory().update(delta);
            ai.update(delta);
            updateRotation(delta);
        } else {
            if (playerIsColliding) {
                getMatch().handleLootInterface(delta, getInventory().getItems());
            }
        }
    }

    @Override
    protected void verifyItems() {
        Object selectedObjectClass = getInventory().getItem(ai.getSelectedInventorySlot()).getClass();
        Object selectedAmorClass = getInventory().getEquipmentItems().get(1).getClass();
        Object selectedHelmetClass = getInventory().getEquipmentItems().get(0).getClass();

        if (selectedObjectClass != getLastSelectedObjectClass() || selectedAmorClass != getLastEquippedArmorClass() ||
                selectedHelmetClass != getLastEquippedHelmetClass()) {
            getAnimator().verify(selectedAmorClass, selectedHelmetClass, selectedObjectClass);
        }
        setLastSelectedObjectClass(selectedObjectClass);
        setLastEquippedArmorClass(selectedAmorClass);
        setLastEquippedHelmetClass(selectedHelmetClass);
    }

    @Override
    public boolean seek(Vector2 target) {
        this.target = target;
        Vector2 direction = new Vector2();
        direction.x = target.x - getBody().getPosition().x;
        direction.y = target.y - getBody().getPosition().y;
        direction = direction.nor();
        getBody().applyForce(new Vector2(direction.x * getSpeed(), direction.y * getSpeed()), getBody().getWorldCenter(), true);
        return true;
    }

    @Override
    protected void die() {
        setDead(true);
        isVisible = true;
        getBody().getFixtureList().first().setSensor(true);
    }

    private void setUserData() {
        getFixture().setUserData(this);
    }

    public AI getAi() {
        return ai;
    }

    @Override
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void drawInteractionButton() {
        if (playerIsColliding && isDead()) {
            getMatch().getBatch().begin();
            getMatch().getBatch().draw(interactionButton,
                    getBody().getPosition().x - pScaleCenter(interactionButton.getWidth()),
                    getBody().getPosition().y - pScaleCenter(interactionButton.getHeight()),
                    pScale(interactionButton.getWidth()),
                    pScale(interactionButton.getHeight()));
            getMatch().getBatch().end();
        }
    }

    public void onPlayerEnter() {
        playerIsColliding = true;
    }

    public void onPlayerLeave() {
        playerIsColliding = false;
    }

    public void onEnemyEnter(Enemy enemy) {
        enemy.getAi().searchForItems(getInventory().getItems());
    }

    public void onEnemyLeave(Enemy enemy) {

    }
}
