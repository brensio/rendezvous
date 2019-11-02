package com.bressio.rendezvous.entities.objects.equipment.helmets;

import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class HalfHelmet extends Helmet {

    public HalfHelmet(Match match) {
        super(match);
        setAttributes();
    }

    @Override
    public void updateName() {
        setName(getI18n().getBundle().get("halfHelmet") + " (" + getArmorPoints() + "%)");
    }

    @Override
    public boolean transformSoldier(Soldier soldier) {
        return false;
    }

    private void setAttributes() {
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.HALF_HELMET));
        setArmorPoints(30);
        takeDamage(0);
        setRarity(9);
        updateName();
    }
}