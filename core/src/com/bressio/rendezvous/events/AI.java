package com.bressio.rendezvous.events;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.entities.Lootable;
import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.entities.objects.Empty;
import com.bressio.rendezvous.entities.objects.EntityObject;
import com.bressio.rendezvous.entities.objects.equipment.armor.Armor;
import com.bressio.rendezvous.entities.objects.equipment.helmets.Helmet;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.entities.objects.weapons.ars.AssaultRifle;
import com.bressio.rendezvous.entities.objects.weapons.pistols.Pistol;
import com.bressio.rendezvous.entities.objects.weapons.srs.SniperRifle;
import com.bressio.rendezvous.forge.WorldBuilder;
import com.bressio.rendezvous.scenes.Match;
import com.bressio.rendezvous.scheme.MathUtils;

import java.util.ArrayList;
import java.util.Collections;

public class AI {

    private Match match;
    private WorldBuilder worldBuilder;
    private Soldier soldier;
    private Vector2 target;
    private Vector2 altTarget;
    private ArrayList<Vector2> spatialMemory;

    private boolean isLooting;
    private boolean isGoingByAltPath;
    private boolean isAltPathTimedOut;

    private int selectedInventorySlot;
    private Soldier enemyOnTarget;

    private enum State {
        IDLE, CALCULATING_ROUTE, SEEKING_LOOT, LOOTING, FINDING_PATH, GOING_TO_RENDEZVOUS, CHASING
    }

    private State state;
    private State previousState;

    public AI(Match match, Soldier soldier) {
        state = State.IDLE;
        this.match = match;
        this.soldier = soldier;
        init();
    }

    private void init() {
        spatialMemory = new ArrayList<>();
        selectedInventorySlot = 0;
    }

    public void update(float delta) {
//        System.out.println(state.name());
        switch (state) {
            case CALCULATING_ROUTE:
                calculateRoute();
                break;
            case SEEKING_LOOT:
                seekLoot();
                break;
            case LOOTING:
                loot();
                break;
            case FINDING_PATH:
                findPath();
                break;
            case GOING_TO_RENDEZVOUS:
                goToRendezvous();
                break;
            case CHASING:
                chase();
                break;
        }

        checkAvailableWeapons();
        checkRendezvousTime();
        if (state != State.CHASING && state != State.GOING_TO_RENDEZVOUS) {
            checkSoldiersAround();
        }
    }

    public void wakeUp(WorldBuilder worldBuilder) {
        this.worldBuilder = worldBuilder;
        setState(State.CALCULATING_ROUTE);
    }

    private void calculateRoute() {
        ArrayList<Lootable> loot = worldBuilder.getAllLoot();
        double nearestLootDistance = 0;
        Lootable nearestLoot = null;

        for (int i = 0; i < loot.size(); i++) {
            if (i == 0 || MathUtils.distance(
                    soldier.getBody().getPosition(), loot.get(i).getBody().getPosition()
            ) < nearestLootDistance && !spatialMemory.contains(loot.get(i).getBody().getPosition())) {
                nearestLoot = loot.get(i);
                nearestLootDistance = MathUtils.distance(
                        soldier.getBody().getPosition(), loot.get(i).getBody().getPosition()
                );
            }
        }
        if (nearestLoot != null) {
            target = nearestLoot.getBody().getPosition();
        }
        setState(State.SEEKING_LOOT);
    }

    private void seekLoot() {
        if (MathUtils.distance(soldier.getBody().getPosition(), target) < 1) {
            spatialMemory.add(target);
            soldier.getBody().getFixtureList().first().setSensor(false);
            setState(State.LOOTING);
        } else {
            if (((SteeringBehavior)soldier).seek(target)) {
                if (soldier.getBody().getLinearVelocity().x == 0 && soldier.getBody().getLinearVelocity().y == 0) {
                    if (!match.getCamera().frustum.pointInFrustum(
                            soldier.getBody().getPosition().x, soldier.getBody().getPosition().y, 0
                    )) {
                        soldier.getBody().getFixtureList().first().setSensor(true);
                        ((Enemy)soldier).setVisible(false);
                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                ((Enemy)soldier).setVisible(true);
                                soldier.getBody().getFixtureList().first().setSensor(false);
                            }
                        }, 1);
                    } else {
                        setState(State.FINDING_PATH);
                    }
                }
            }
        }
    }

    private void loot() {
        if (!isLooting) {
            isLooting = true;
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    isLooting = false;
                    setState(State.CALCULATING_ROUTE);
                }
            }, 3);
        }
    }

    private void findPath() {
        if (!isGoingByAltPath) {
            altTarget = new Vector2();
            altTarget.x = soldier.getBody().getPosition().x + MathUtils.randomRange(-30, 30);
            altTarget.y = soldier.getBody().getPosition().y + MathUtils.randomRange(-30, 30);
            isGoingByAltPath = true;
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    isAltPathTimedOut = true;
                }
            }, 5);
        } else {
            if (MathUtils.distance(soldier.getBody().getPosition(), altTarget) < 1 || isAltPathTimedOut) {
                setState(State.CALCULATING_ROUTE);
                isGoingByAltPath = false;
                isAltPathTimedOut = false;
            } else {
                ((SteeringBehavior)soldier).seek(altTarget);
            }
        }
    }

    private void goToRendezvous() {
        target = new Vector2(41 + match.getRendezvousController()
                .getSafezoneOffsets()[match.getRendezvousController().getCurrentOffset()].x,
                41 + match.getRendezvousController()
                        .getSafezoneOffsets()[match.getRendezvousController().getCurrentOffset()].y);
        ((SteeringBehavior)soldier).seek(target);

        if (!match.getRendezvousController().soldierIsInDangerZone(soldier)) {
            setState(State.SEEKING_LOOT);
        }
    }

    private void chase() {
        if (MathUtils.distance(soldier.getBody().getPosition(), enemyOnTarget.getBody().getPosition()) > 4) {
            if (MathUtils.distance(soldier.getBody().getPosition(), enemyOnTarget.getBody().getPosition()) < 7) {
                ((SteeringBehavior)soldier).seek(enemyOnTarget.getBody().getPosition());
            } else {
                setState(State.CALCULATING_ROUTE);
            }
        }
        checkAvailableWeapons();
        if (hasWeapon()) {
            if (soldier.getInventory().getBulletsInMagazine() < 1) {
                soldier.getInventory().reloadSelectedWeapon();
            } else {
                ((Weapon)soldier.getInventory().getItem(selectedInventorySlot)).shoot(soldier);
            }
        }
        if (enemyOnTarget.isDead()) {
            setState(State.CALCULATING_ROUTE);
        }
    }

    private void setState(State state) {
        previousState = this.state;
        this.state = state;
    }

    public void searchForItems(ArrayList<EntityObject> items) {
        Collections.sort(items);
        for (int i = 0; i < items.size(); i++) {
            exchangeItem(items, i);
        }
        // Print items of this enemy
//        System.out.println(" = = = = = = = = = = = = =");
//        for (EntityObject item : soldier.getInventory().getItems()) {
//            System.out.println(item.getName());
//        }
//        System.out.println("- - -");
//        for (EntityObject item : soldier.getInventory().getEquipmentItems()) {
//            System.out.println(item.getName());
//        }
    }

    private void exchangeItem(ArrayList<EntityObject> items, int index) {
        if (!hasItem(items.get(index))) {
            if (Helmet.class.isAssignableFrom(items.get(index).getClass())) {
                if (!hasHelmet()) {
                    if (soldier.getInventory().getEquipmentItems().get(0).getClass() == Empty.class) {
                        soldier.getInventory().getEquipmentItems().set(0, items.get(index));
                        items.set(index, new Empty(match));
                    }
                } else if (isItemWorthSwapping(items.get(index))) {
                    EntityObject escrow = soldier.getInventory().getEquipmentItems().get(0);
                    soldier.getInventory().getEquipmentItems().set(0, items.get(index));
                    items.set(index, escrow);
                }
            } else if (Armor.class.isAssignableFrom(items.get(index).getClass())) {
                if (!hasArmor()) {
                    if (soldier.getInventory().getEquipmentItems().get(1).getClass() == Empty.class) {
                        soldier.getInventory().getEquipmentItems().set(1, items.get(index));
                        items.set(index, new Empty(match));
                    }
                } else if (isItemWorthSwapping(items.get(index))) {
                    EntityObject escrow = soldier.getInventory().getEquipmentItems().get(1);
                    soldier.getInventory().getEquipmentItems().set(1, items.get(index));
                    items.set(index, escrow);
                }
            } else {
                if (!isInventoryFull()) {
                    if (!Weapon.class.isAssignableFrom(items.get(index).getClass()) || !hasWeapon()) {
                        for (int j = 0; j < soldier.getInventory().getItems().size(); j++) {
                            if (soldier.getInventory().getItems().get(j).getClass() == Empty.class) {
                                soldier.getInventory().getItems().set(j, items.get(index));
                                items.set(index, new Empty(match));
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < soldier.getInventory().getItems().size(); i++) {
                        if (isItemWorthSwapping(items.get(index), soldier.getInventory().getItems().get(i))) {
                            EntityObject escrow = soldier.getInventory().getItems().get(i);
                            soldier.getInventory().getItems().set(i, items.get(index));
                            items.set(index, escrow);
                        }
                    }
                }
            }
        }
    }

    private boolean hasItem(EntityObject lootItem) {
        if (Armor.class.isAssignableFrom(lootItem.getClass()) ||
                Helmet.class.isAssignableFrom(lootItem.getClass())) {
            for (EntityObject item : soldier.getInventory().getEquipmentItems()) {
                if (lootItem.getClass() == item.getClass()) {
                    return true;
                }
            }
        } else {
            for (EntityObject item : soldier.getInventory().getItems()) {
                if (lootItem.getClass() == item.getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasWeapon() {
        for (EntityObject item : soldier.getInventory().getItems()) {
            if (Weapon.class.isAssignableFrom(item.getClass())) {
                return true;
            }
        }
        return false;
    }

    private boolean isItemWorthSwapping(EntityObject lootItem) {
        if (Helmet.class.isAssignableFrom(lootItem.getClass())) {
            return lootItem.getRarity() < soldier.getInventory().getEquipmentItems().get(0).getRarity();
        } else if (Armor.class.isAssignableFrom(lootItem.getClass())) {
            return lootItem.getRarity() < soldier.getInventory().getEquipmentItems().get(1).getRarity();
        }
        return false;
    }

    private boolean isItemWorthSwapping(EntityObject lootItem, EntityObject inventoryItem) {
         if (Weapon.class.isAssignableFrom(lootItem.getClass()) && Weapon.class.isAssignableFrom(inventoryItem.getClass())) {
             return lootItem.getRarity() < inventoryItem.getRarity();
        }
        return false;
    }

    private boolean isInventoryFull() {
        int capacity = soldier.getInventory().getItems().size();

        for (EntityObject item : soldier.getInventory().getItems()) {
            if (item.getClass() != Empty.class) {
                capacity--;
            }
        }

        return capacity == 1;
    }

    private boolean hasHelmet() {
        return soldier.getInventory().getEquipmentItems().get(0).getClass() != Empty.class;
    }

    private boolean hasArmor() {
        return soldier.getInventory().getEquipmentItems().get(1).getClass() != Empty.class;
    }

    public int getSelectedInventorySlot() {
        return selectedInventorySlot;
    }

    private void checkAvailableWeapons() {
        ArrayList<EntityObject> items = soldier.getInventory().getItems();
        for (int i = 0; i < soldier.getInventory().getItems().size(); i++) {
            if (SniperRifle.class.isAssignableFrom(items.get(i).getClass())) {
                selectedInventorySlot = i;
                return;
            }
        }
        for (int i = 0; i < soldier.getInventory().getItems().size(); i++) {
             if (AssaultRifle.class.isAssignableFrom(items.get(i).getClass())) {
                 selectedInventorySlot = i;
                 return;
            }
        }
        for (int i = 0; i < soldier.getInventory().getItems().size(); i++) {
            if (Pistol.class.isAssignableFrom(items.get(i).getClass())) {
                selectedInventorySlot = i;
                return;
            }
        }
    }

    private void checkRendezvousTime() {
        if (match.getRendezvousController().getSecondsToNextEvent() <
                match.getRendezvousController().getSECONDS_NEXT() / 2 &&
                !match.getRendezvousController().isInRendezvous()) {
            setState(State.GOING_TO_RENDEZVOUS);
        }
    }

    private void checkSoldiersAround() {
        ArrayList<Soldier> soldiersAround = new ArrayList<>();
        for (Soldier enemySoldier : worldBuilder.getSoldiers()) {
            if (MathUtils.distance(soldier.getBody().getPosition(), enemySoldier.getBody().getPosition()) < 5 &&
                    hasWeapon() && soldier != enemySoldier && !enemySoldier.isDead()) {
                soldiersAround.add(enemySoldier);
            }
        }
        if (soldiersAround.size() > 0) {
            enemyOnTarget = soldiersAround.get(MathUtils.randomRange(0, soldiersAround.size() - 1));
            setState(State.CHASING);
        }
    }
}
