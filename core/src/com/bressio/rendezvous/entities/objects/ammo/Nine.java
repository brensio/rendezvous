package com.bressio.rendezvous.entities.objects.ammo;

import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class Nine extends Ammo {

    public Nine(Match match) {
        super(match);
        setAttributes();
    }

    @Override
    public void updateName() {
        setName(getI18n().getBundle().get("nine") + " (" + getAmount() + "x)");
    }

    private void setAttributes() {
        setAmount(50);
        updateName();
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.NINE));
        setRarity(12);
    }
}
