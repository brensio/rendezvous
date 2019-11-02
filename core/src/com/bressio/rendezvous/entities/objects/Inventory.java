package com.bressio.rendezvous.entities.objects;

import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.entities.objects.ammo.Ammo;
import com.bressio.rendezvous.entities.objects.equipment.armor.Armor;
import com.bressio.rendezvous.entities.objects.equipment.helmets.Helmet;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.scenes.Match;

import java.util.ArrayList;

public abstract class Inventory {

    private Match match;
    private ArrayList<EntityObject> items;
    private ArrayList<EntityObject> equipmentItems;
    private boolean isSelectedBeingUsed;
    private Soldier soldier;

    public Inventory(Match match, Soldier soldier) {
        this.match = match;
        this.soldier = soldier;
        addItems();
    }

    private void addItems() {
        items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            items.add(new Empty(match));
        }

        equipmentItems = new ArrayList<>();
        equipmentItems.add(new Empty(match));
        equipmentItems.add(new Empty(match));
    }

    public boolean isSelectedBeingUsed() {
        return isSelectedBeingUsed;
    }

    public void setSelectedBeingUsed(boolean selectedBeingUsed) {
        isSelectedBeingUsed = selectedBeingUsed;
    }

    public abstract void useSelectedItem(Soldier soldier);

    public abstract void reloadSelectedWeapon();

    protected boolean hasAmmoForWeaponType(Weapon weapon) {
        for (EntityObject item : items) {
            if (Ammo.class.isAssignableFrom(item.getClass()) && weapon.getAmmoType() == item.getClass()) {
                return true;
            }
        }
        return false;
    }

    public abstract void applyAction();

    public abstract void transferAmmo(int bulletsInWeapon);

    public abstract void update(float delta);

    protected void setArmorPoints(float delta) {
        int armorPoints = 0;
        armorPoints += equipmentItems.get(0).getClass() != Empty.class ? ((Helmet)equipmentItems.get(0)).getArmorPoints() : 0;
        armorPoints += equipmentItems.get(1).getClass() != Empty.class ? ((Armor)equipmentItems.get(1)).getArmorPoints() : 0;
        soldier.setArmor(armorPoints);
    }

    public abstract int getBulletsInMagazine();

    public abstract int getBulletsInAmmoBoxes();

    public ArrayList<EntityObject> getItems() {
        return items;
    }

    public ArrayList<EntityObject> getEquipmentItems() {
        return equipmentItems;
    }

    public EntityObject getItem(int index) {
        return items.get(index);
    }

    public Match getMatch() {
        return match;
    }

    public Soldier getSoldier() {
        return soldier;
    }
}
