package com.bressio.rendezvous.entities.objects;

import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

public class Medkit extends EntityObject{

    public Medkit(Match match) {
        super(match);
        setAttributes();
    }

    @Override
    public boolean transformSoldier(Soldier soldier) {
        soldier.changeHealth(50);
        return true;
    }

    private void setAttributes() {
        setName(getI18n().getBundle().get("medkit"));
        setIcon(getResources().getTexture(ResourceHandler.TexturePath.MEDKIT_ICON));
        setRarity(13);
    }

    public static float timeToTransform() {
        return .05f;
    }
}
