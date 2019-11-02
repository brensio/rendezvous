package com.bressio.rendezvous.entities.objects.weapons.ars;

import com.bressio.rendezvous.entities.objects.ammo.FiveFiveSix;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.scenes.Match;

public abstract class AssaultRifle extends Weapon {

    private FiveFiveSix ammo;

    public AssaultRifle(Match match) {
        super(match);
        ammo = new FiveFiveSix(match);
    }

    @Override
    public Object getAmmoType() {
        return ammo.getClass();
    }

    @Override
    public float getTimeToTransform() {
        return .03f;
    }
}
