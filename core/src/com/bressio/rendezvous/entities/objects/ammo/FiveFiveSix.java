package com.bressio.rendezvous.entities.objects.ammo;

import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class FiveFiveSix extends Ammo{

    public FiveFiveSix(Match match) {
        super(match);
        setAttributes();
    }

    @Override
    public void updateName() {
        setName(getI18n().getBundle().get("fiveFiveSix") + " (" + getAmount() + "x)");
    }

    private void setAttributes() {
        setAmount(80);
        updateName();
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.FIVE_FIVE_SIX));
        setRarity(9);
    }
}
