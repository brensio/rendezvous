package com.bressio.rendezvous.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bressio.rendezvous.entities.objects.EntityObject;
import com.bressio.rendezvous.entities.objects.PlayerInventory;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.events.InputTracker;
import com.bressio.rendezvous.graphics.AnimationRegion;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public class Player extends Soldier {

    private boolean actionsBlocked;
    private boolean actionsSemiBlocked;

    public Player(Match match, float radius, float linearDamping, int speed, Vector2 position) {
        super(match, position, radius, linearDamping, speed, AnimationRegion.SOLDIER, PLAYER_TAG,
                (short) (DEFAULT_TAG | BUILDING_TAG | LOOT_TAG | WATER_TAG | ENEMY_TAG | BULLET_TAG), "player");
    }

    @Override
    protected void init() {
        super.init();
        setInventory(new PlayerInventory(getMatch(), this));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        getInventory().update(delta);
        handleKeyboardInput(delta);
        handleMouseInput(delta);
        checkKillCount(delta);
    }

    @Override
    protected void verifyItems() {
        Object selectedObjectClass = getInventory().getItem(getMatch().getHud().getSelectedSlot()).getClass();
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
    protected void die() {

    }

    private void handleKeyboardInput(float delta) {
        if (!actionsBlocked) {
            float directionX = 0;
            float directionY = 0;

            if (InputTracker.isPressed(InputTracker.LEFT)){
                directionX -= getSpeed();
            }

            if(InputTracker.isPressed(InputTracker.RIGHT)){
                directionX += getSpeed();
            }

            if (InputTracker.isPressed(InputTracker.UP)){
                directionY += getSpeed();
            }

            if(InputTracker.isPressed(InputTracker.DOWN)){
                directionY -= getSpeed();
            }

            if (directionX != 0 || directionY != 0) {
                    Vector2 normalizedDirection = new Vector2(directionX, directionY).nor();
                    getBody().applyForce(
                            new Vector2(normalizedDirection.x * getSpeed(), normalizedDirection.y * getSpeed()),
                            getBody().getWorldCenter(),
                            true
                    );
            }

            if (!actionsSemiBlocked) {
                if (InputTracker.isPressed(InputTracker.R) && !Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
                        (getMatch().getState() == Match.GameState.RUNNING ||
                                getMatch().getState() == Match.GameState.TACTICAL)) {

                    EntityObject selectedItem = getInventory().getItem(getMatch().getHud().getSelectedSlot());

                    if (Weapon.class.isAssignableFrom(selectedItem.getClass()) &&
                            ((Weapon)selectedItem).getBullets() < ((Weapon)selectedItem).getMagCapacity()) {
                        getInventory().reloadSelectedWeapon();
                    }
                }
            }
        }
    }

    private void handleMouseInput(float delta) {
        float angle = MathUtils.radiansToDegrees * MathUtils.atan2(
                InputTracker.getMousePos().y - (getY() + pCenter(getHeight())),
                InputTracker.getMousePos().x - (getX() + pCenter(getWidth())));

        if(angle < 0){
            angle += 360;
        }
        setRotation(angle - 90);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
                (getMatch().getState() == Match.GameState.RUNNING ||
                getMatch().getState() == Match.GameState.TACTICAL)) {
            getInventory().useSelectedItem(this);
        }
    }

    public void blockActions() {
        actionsBlocked = true;
    }

    public void slowDown() {
        setSpeed(7);
        actionsSemiBlocked = true;
    }

    public void unblockActions() {
        actionsBlocked = false;
        actionsSemiBlocked = false;
        setSpeed(8);
    }

    public boolean isActionsBlocked() {
        return actionsBlocked || actionsSemiBlocked;
    }

    private void checkKillCount(float delta) {
        if (getMatch().getHud().getKills() != getKills()) {
            getMatch().getHud().notifyKill(getKills());
        }
    }
}
