package com.bressio.rendezvous.entities.objects;

import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.entities.objects.ammo.Ammo;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.scenes.Match;

public class NPCInventory extends Inventory{

    public NPCInventory(Match match, Soldier soldier) {
        super(match, soldier);
    }

    @Override
    public void useSelectedItem(Soldier soldier) {

    }

    @Override
    public void reloadSelectedWeapon() {
        transferAmmo(0);
    }

    @Override
    public void applyAction() {

    }

    @Override
    public void transferAmmo(int bulletsInWeapon) {
        int bullets = bulletsInWeapon;
        int amountNeeded = ((Weapon)getItem(((Enemy)getSoldier()).getAi().getSelectedInventorySlot())).getMagCapacity() - bulletsInWeapon;
        for (int i = 0; i < getItems().size(); i++) {

            if (Ammo.class.isAssignableFrom(getItems().get(i).getClass()) && amountNeeded > 0) {

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
        ((Weapon)getItem(((Enemy)getSoldier()).getAi().getSelectedInventorySlot())).setBullets(bullets);
    }

    @Override
    public void update(float delta) {
        setArmorPoints(delta);
    }

    @Override
    public int getBulletsInMagazine() {
        if (Weapon.class.isAssignableFrom(getItem(((Enemy)getSoldier()).getAi().getSelectedInventorySlot()).getClass())) {
            return ((Weapon)getItem(((Enemy)getSoldier()).getAi().getSelectedInventorySlot())).getBullets();
        } else {
            return -1;
        }
    }

    @Override
    public int getBulletsInAmmoBoxes() {
        return -1;
    }
}
