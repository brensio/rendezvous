package com.bressio.rendezvous.entities.objects.weapons.srs;

import com.bressio.rendezvous.entities.objects.ammo.SevenSixTwo;
import com.bressio.rendezvous.entities.objects.weapons.Weapon;
import com.bressio.rendezvous.scenes.Match;

public abstract class SniperRifle extends Weapon {

    private SevenSixTwo ammo;

    public SniperRifle(Match match) {
        super(match);
        ammo = new SevenSixTwo(match);
    }

    @Override
    public Object getAmmoType() {
        return ammo.getClass();
    }

    @Override
    public float getTimeToTransform() {
        return .04f;
    }
}
