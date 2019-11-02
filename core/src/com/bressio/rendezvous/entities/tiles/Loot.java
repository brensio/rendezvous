package com.bressio.rendezvous.entities.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.entities.Lootable;
import com.bressio.rendezvous.entities.objects.EntityObject;
import com.bressio.rendezvous.entities.objects.Medkit;
import com.bressio.rendezvous.entities.objects.ammo.FiveFiveSix;
import com.bressio.rendezvous.entities.objects.ammo.Nine;
import com.bressio.rendezvous.entities.objects.ammo.SevenSixTwo;
import com.bressio.rendezvous.entities.objects.equipment.armor.MilitaryVest;
import com.bressio.rendezvous.entities.objects.equipment.armor.SoftVest;
import com.bressio.rendezvous.entities.objects.equipment.helmets.CombatHelmet;
import com.bressio.rendezvous.entities.objects.equipment.helmets.HalfHelmet;
import com.bressio.rendezvous.entities.objects.weapons.ars.STAR;
import com.bressio.rendezvous.entities.objects.weapons.ars.W16A;
import com.bressio.rendezvous.entities.objects.weapons.pistols.G21;
import com.bressio.rendezvous.entities.objects.weapons.pistols.P26;
import com.bressio.rendezvous.entities.objects.weapons.srs.AW3;
import com.bressio.rendezvous.entities.objects.weapons.srs.M20;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;
import com.bressio.rendezvous.scheme.MathUtils;

import java.util.ArrayList;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public abstract class Loot extends InteractiveTile implements Lootable {

    private SpriteBatch batch;
    private Texture interactionButton;
    private boolean playerIsColliding;

    private ArrayList<EntityObject> items;

    Loot(Rectangle bounds, Match match) {
        super(bounds, match, false, LOOT_TAG);
        this.batch = match.getBatch();
        init();
    }

    private void init() {
        items = new ArrayList<>();
        interactionButton = getMatch().getResources().getTexture(ResourceHandler.TexturePath.INTERACTION_BUTTON);
    }

    public void drawInteractionButton() {
        if (playerIsColliding) {
            batch.begin();
            batch.draw(interactionButton,
                    getBody().getPosition().x - pScaleCenter(interactionButton.getWidth()),
                    getBody().getPosition().y - pScaleCenter(interactionButton.getHeight()),
                    pScale(interactionButton.getWidth()),
                    pScale(interactionButton.getHeight()));
            batch.end();
        }
    }

    public void update(float delta) {
        if (playerIsColliding) {
            getMatch().handleLootInterface(delta, items);
        }
    }

    ArrayList<EntityObject> getItems() {
        return items;
    }

    public Match getMatch() {
        return super.getMatch();
    }

    @Override
    public void onPlayerEnter() {
        playerIsColliding = true;
    }

    @Override
    public void onPlayerLeave() {
        playerIsColliding = false;
    }

    @Override
    public void onEnemyEnter(Enemy enemy) {
        enemy.getAi().searchForItems(items);
    }

    @Override
    public void onEnemyLeave(Enemy enemy) {

    }

    public EntityObject getRandomItem() {
        int randomNumber = MathUtils.randomRange(1, 100);

        if (randomNumber <= 13 ) {
            return new Medkit(getMatch());
        } else if (randomNumber <= 25 ) {
            return new SoftVest(getMatch());
        } else if (randomNumber <= 34 ) {
            return new HalfHelmet(getMatch());
        } else if (randomNumber <= 40 ) {
            return new MilitaryVest(getMatch());
        } else if (randomNumber <= 43 ) {
            return new CombatHelmet(getMatch());
        } else if (randomNumber <= 55 ) {
            return new Nine(getMatch());
        } else if (randomNumber <= 64 ) {
            return new FiveFiveSix(getMatch());
        } else if (randomNumber <= 70 ) {
            return new SevenSixTwo(getMatch());
        } else if (randomNumber <= 81 ) {
            return new G21(getMatch());
        } else if (randomNumber <= 89 ) {
            return new P26(getMatch());
        } else if (randomNumber <= 94 ) {
            return new STAR(getMatch());
        } else if (randomNumber <= 97 ) {
            return new W16A(getMatch());
        } else if (randomNumber <= 99 ) {
            return new M20(getMatch());
        } else if (randomNumber <= 100 ) {
            return new AW3(getMatch());
        }

        return null;
    }
}
