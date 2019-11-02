package com.bressio.rendezvous.entities.objects;

import com.badlogic.gdx.graphics.Texture;
import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.languages.Internationalization;
import com.bressio.rendezvous.scenes.Match;

public abstract class EntityObject implements Comparable<EntityObject>{

    private Match match;
    private String name;
    private Texture icon;
    private ResourceHandler resources;
    private Internationalization i18n;
    private int rarity;

    public EntityObject(Match match) {
        this.match = match;
        init();
    }

    private void init() {
        resources = match.getResources();
        i18n = match.getI18n();
    }

    public abstract boolean transformSoldier(Soldier soldier);

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Texture icon) {
        this.icon = icon;
    }

    public Texture getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public ResourceHandler getResources() {
        return resources;
    }

    public Internationalization getI18n() {
        return i18n;
    }

    public Match getMatch() {
        return match;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    @Override
    public int compareTo(EntityObject o) {
        return Integer.compare(this.getRarity(), o.getRarity());
    }
}
