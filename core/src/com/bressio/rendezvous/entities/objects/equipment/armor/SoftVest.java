package com.bressio.rendezvous.entities.objects.equipment.armor;

import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class SoftVest extends Armor {

    public SoftVest(Match match) {
        super(match);
        setAttributes();
    }

    @Override
    public void updateName() {
        setName(getI18n().getBundle().get("softVest") + " (" + getArmorPoints() + "%)");
    }

    @Override
    public boolean transformSoldier(Soldier soldier) {
        return false;
    }

    private void setAttributes() {
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.SOFT_VEST));
        setArmorPoints(20);
        takeDamage(0);
        setRarity(12);
        updateName();
    }
}
