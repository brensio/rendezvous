package com.bressio.rendezvous.entities.objects;

import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.entities.objects.ammo.Ammo;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.scenes.Match;

public class PlayerInventory extends Inventory{

    public PlayerInventory(Match match, Soldier soldier) {
        super(match, soldier);
    }

    @Override
    public void useSelectedItem(Soldier soldier) {
        Object selectedSlot = getItem(getMatch().getHud().getSelectedSlot());
        if (selectedSlot.getClass() == Medkit.class) {
            setSelectedBeingUsed(true);
            getMatch().getProgress().setProgressSpeed(Medkit.timeToTransform());
            getMatch().getPlayer().blockActions();
            getMatch().getProgress().setActivity("healing");
        } else if (Weapon.class.isAssignableFrom(selectedSlot.getClass())) {
            ((Weapon)selectedSlot).shoot(soldier);
        }
    }

    @Override
    public void reloadSelectedWeapon() {
        Object selectedSlotClass = getItem(getMatch().getHud().getSelectedSlot());

        if (hasAmmoForWeaponType((Weapon)selectedSlotClass)) {
            setSelectedBeingUsed(true);
            getMatch().getProgress().setProgressSpeed(((Weapon)selectedSlotClass).getTimeToTransform());
            getMatch().getPlayer().slowDown();
            getMatch().getProgress().setActivity("reloading");
        }
    }

    @Override
    public void applyAction() {
        getItem(getMatch().getHud().getSelectedSlot()).transformSoldier(getMatch().getPlayer());
        if (getItems().get(getMatch().getHud().getSelectedSlot()).getClass() == Medkit.class) {
            getItems().set(getMatch().getHud().getSelectedSlot(), new Empty(getMatch()));
        }
    }

    @Override
    public void transferAmmo(int bulletsInWeapon) {
        int bullets = bulletsInWeapon;
        int amountNeeded = ((Weapon)getItem(getMatch().getHud().getSelectedSlot())).getMagCapacity() - bulletsInWeapon;
        for (int i = 0; i < getItems().size(); i++) {

            if (Ammo.class.isAssignableFrom(getItems().get(i).getClass()) && amountNeeded > 0 &&
                    ((Weapon)getItem(getMatch().getHud().getSelectedSlot())).getAmmoType() == getItems().get(i).getClass()) {

                Ammo ammoBox = ((Ammo)getItems().get(i));
                if (ammoBox.getAmount() >= amountNeeded) {
                    bullets += amountNeeded;
                    ammoBox.useAmount(amountNeeded);
                    amountNeeded = 0;
                } else {
                    bullets += ammoBox.getAmount();
                    amountNeeded -= ammoBox.getAmount();
                    ammoBox.useAll();
                }
                if (ammoBox.getAmount() == 0) {
                    getItems().set(i, new Empty(getMatch()));
                } else {
                    ammoBox.updateName();
                }
            }
        }
        ((Weapon)getItem(getMatch().getHud().getSelectedSlot())).setBullets(bullets);
    }

    @Override
    public void update(float delta)  {
        setArmorPoints(delta);
    }

    @Override
    public int getBulletsInMagazine() {
        if (Weapon.class.isAssignableFrom(getItem(getMatch().getHud().getSelectedSlot()).getClass())) {
            return ((Weapon)getItem(getMatch().getHud().getSelectedSlot())).getBullets();
        } else {
            return -1;
        }
    }

    @Override
    public int getBulletsInAmmoBoxes()  {
        if (Weapon.class.isAssignableFrom(getItem(getMatch().getHud().getSelectedSlot()).getClass())) {
            int bullets = 0;
            for (EntityObject item : getItems()) {
                if (Ammo.class.isAssignableFrom(item.getClass()) &&
                        ((Weapon)getItem(getMatch().getHud().getSelectedSlot())).getAmmoType() == item.getClass()) {
                    bullets += ((Ammo)item).getAmount();
                }
            }
            return bullets;
        } else {
            return -1;
        }
    }
}
