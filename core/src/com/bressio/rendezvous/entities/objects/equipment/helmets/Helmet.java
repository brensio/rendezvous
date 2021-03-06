package com.bressio.rendezvous.entities.objects.equipment.helmets;

import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.entities.objects.EntityObject;
import com.bressio.rendezvous.scenes.Match;

public abstract class Helmet extends EntityObject {

    private int armorPoints;
    private int damage;

    Helmet(Match match) {
        super(match);
    }

    public abstract void updateName();

    @Override
    public abstract boolean transformSoldier(Soldier soldier);

    public int getArmorPoints() {
        return armorPoints;
    }

    void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    public int getDamage() {
        return damage;
    }

    public void takeDamage(int damage) {
        armorPoints -= damage;
    }
}