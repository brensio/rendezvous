package com.bressio.rendezvous.entities.objects.ammo;

import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class SevenSixTwo extends Ammo {

    public SevenSixTwo(Match match) {
        super(match);
        setAttributes();
    }

    @Override
    public void updateName() {
        setName(getI18n().getBundle().get("sevenSixTwo") + " (" + getAmount() + "x)");
    }

    private void setAttributes() {
        setAmount(30);
        updateName();
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.SEVEN_SIX_TWO));
        setRarity(6);
    }
}
