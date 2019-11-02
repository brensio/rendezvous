package com.bressio.rendezvous.entities.objects.equipment.armor;

import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class MilitaryVest extends Armor {

    public MilitaryVest(Match match) {
        super(match);
        setAttributes();
    }

    @Override
    public void updateName() {
        setName(getI18n().getBundle().get("militaryVest") + " (" + getArmorPoints() + "%)");
    }

    @Override
    public boolean transformSoldier(Soldier soldier) {
        return false;
    }

    private void setAttributes() {
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.MILITARY_VEST));
        setArmorPoints(40);
        takeDamage(0);
        setRarity(6);
        updateName();
    }
}