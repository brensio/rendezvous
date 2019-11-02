package com.bressio.rendezvous.entities.objects.weapons.pistols;

import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class G21 extends Pistol {

    public G21(Match match) {
        super(match);
        setAttributes();
    }

    private void setAttributes() {
        setName(getI18n().getBundle().get("g21"));
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.G21));
        setDamage(10);
        setRateOfFire(.6f);
        setReloadTime(2);
        setMagCapacity(15);
        setAccuracy(3);
        setRarity(11);
    }
}