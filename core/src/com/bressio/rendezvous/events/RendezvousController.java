package com.bressio.rendezvous.events;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.entities.Player;
import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;
import com.bressio.rendezvous.scheme.PhysicsAdapter;

import static com.bressio.rendezvous.scheme.MathUtils.distance;
import static com.bressio.rendezvous.scheme.MathUtils.randomRange;
import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class RendezvousController {

    private float rendezvousTimeCount;
    private int secondsToNextEvent;
    private int event;
    private String rendezvousLabel;

    private Match match;

    private Texture safezone;
    private Texture dangerZone;

    private Vector2[] safezoneOffsets;

    private Player player;
    private float damageTimeCount;

    private final int SAFEZONE_RANGE = 30;
    private final int SECONDS_NEXT = 60;

    public RendezvousController(Match match) {
        this.match = match;
        this.player = match.getPlayer();
        init();
        setupHud();
    }

    private void init() {
        secondsToNextEvent = SECONDS_NEXT;
        event = 1;
        safezone = match.getResources().getTexture(ResourceHandler.TexturePath.SAFEZONE);
        dangerZone = match.getResources().getTexture(ResourceHandler.TexturePath.DANGER_ZONE);
        safezoneOffsets = generateSafezoneOffsets();
    }

    private void setupHud() {
        rendezvousLabel = match.getI18n().getBundle().get("nextRendezvous");
        match.getHud().updateEventLabel(rendezvousLabel, isInRendezvous());
        match.getHud().updateTimeLabel(PhysicsAdapter.formatSeconds(secondsToNextEvent, false));
    }

    private Vector2[] generateSafezoneOffsets() {
        Vector2[] offsets = new Vector2[4];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = new Vector2(
                    randomRange(-SAFEZONE_RANGE, SAFEZONE_RANGE), randomRange(-SAFEZONE_RANGE, SAFEZONE_RANGE)
            );
        }
        return offsets;
    }

    public void update(float delta) {
        if (event < 8) {
            rendezvousTimeCount += delta;

            if (secondsToNextEvent == -1) {
                event++;
                if (event == 2) {
                    rendezvousLabel = match.getI18n().getBundle().get("firstRendezvous");
                } else if (event == 4) {
                    rendezvousLabel = match.getI18n().getBundle().get("secondRendezvous");
                } else if (event == 6) {
                    rendezvousLabel = match.getI18n().getBundle().get("thirdRendezvous");
                } else if (event == 8) {
                    rendezvousLabel = match.getI18n().getBundle().get("finalRendezvous");
                } else {
                    rendezvousLabel = match.getI18n().getBundle().get("nextRendezvous");
                }
                match.getHud().updateEventLabel(rendezvousLabel, isInRendezvous());
                secondsToNextEvent = SECONDS_NEXT;
                match.getHud().updateTimeLabel(PhysicsAdapter.formatSeconds(secondsToNextEvent, false));
            }

            if (rendezvousTimeCount >= 1) {
                match.getHud().updateTimeLabel(PhysicsAdapter.formatSeconds(secondsToNextEvent - 1, false));
                secondsToNextEvent--;
                rendezvousTimeCount = 0;
            }
        }
        dealDamage(delta);
    }

    private void dealDamage(float delta) {
        damageTimeCount += delta;
        if (damageTimeCount >= 1) {
            if (canDamageSoldier(8, 3, 7.5f, player) ||
                    canDamageSoldier(6, 2, 15.3f, player) ||
                    canDamageSoldier(4, 1, 22.8f, player) ||
                    canDamageSoldier(2, 0, 30.5f, player)) {
                player.takeDangerZoneDamage();
            }

            for (Enemy enemy : match.getWorldBuilder().getEnemies()) {
                if (canDamageSoldier(8, 3, 7.5f, enemy) ||
                        canDamageSoldier(6, 2, 15.3f, enemy) ||
                        canDamageSoldier(4, 1, 22.8f, enemy) ||
                        canDamageSoldier(2, 0, 30.5f, enemy)) {
                    enemy.takeDangerZoneDamage();
                }
            }
            damageTimeCount = 0;
        }
    }

    private boolean canDamageSoldier(int event, int offsetIndex, float distance, Soldier soldier) {
        return (this.event == event && distance(soldier.getBody().getPosition(),
                new Vector2(safezoneOffsets[offsetIndex].x + 41, safezoneOffsets[offsetIndex].y + 41)) > distance);
    }

    public boolean soldierIsInDangerZone(Soldier soldier) {
        if (event == 1 || event == 2) {
            return (distance(soldier.getBody().getPosition(),
                    new Vector2(safezoneOffsets[0].x + 41, safezoneOffsets[0].y + 41)) > 30.5f);
        } else if (event == 3 || event == 4) {
            return (distance(soldier.getBody().getPosition(),
                    new Vector2(safezoneOffsets[1].x + 41, safezoneOffsets[1].y + 41)) > 22.8f);
        } else if (event == 5 || event == 6) {
            return (distance(soldier.getBody().getPosition(),
                    new Vector2(safezoneOffsets[2].x + 41, safezoneOffsets[2].y + 41)) > 15.3f);
        } else if (event == 7 || event == 8) {
            return (distance(soldier.getBody().getPosition(),
                    new Vector2(safezoneOffsets[3].x + 41, safezoneOffsets[3].y + 41)) > 7.5f);
        }
        return false;
    }

    public void render(float delta) {
        match.getBatch().begin();

        switch (event) {
            case 2:
                drawSafezone(2, 2, 0, 4, safezoneOffsets[0]);
                break;
            case 4:
                drawSafezone(2.5f, 1.5f, .5f, 3.5f, safezoneOffsets[1]);
                break;
            case 6:
                drawSafezone(3, 1, 1, 3, safezoneOffsets[2]);
                break;
            case 8:
                drawSafezone(3.5f, .5f, 1.5f, 2.5f, safezoneOffsets[3]);
                break;
        }
        match.getBatch().end();
    }

    private void drawSafezone(float sZXScale, float sZSScale, float dZP1Scale, float dZP2Scale, Vector2 offset) {

        float safezoneWidth = pScale(safezone.getWidth());
        float dangerZoneWidth = pScale(dangerZone.getWidth());

        int safezoneSize = safezone.getWidth();
        int dangerZoneSize = 1000;

        match.getBatch().draw(safezone,
                pScaleCenter(safezoneSize * sZXScale) - safezoneWidth + offset.x,
                pScaleCenter(safezoneSize * sZXScale) - safezoneWidth + offset.y,
                safezoneWidth * sZSScale,
                safezoneWidth * sZSScale);
        match.getBatch().draw(dangerZone,
                pScaleCenter(safezoneSize * dZP1Scale) + offset.x,
                -pScaleCenter(safezoneSize * 2) + offset.y,
                -dangerZoneWidth * dangerZoneSize,
                dangerZoneWidth * dangerZoneSize);
        match.getBatch().draw(dangerZone,
                pScaleCenter(safezoneSize * dZP2Scale) + offset.x,
                -pScaleCenter(safezoneSize * 2) + offset.y,
                dangerZoneWidth * dangerZoneSize,
                dangerZoneWidth * dangerZoneSize);
        match.getBatch().draw(dangerZone,
                pScaleCenter(safezoneSize * dZP1Scale) + offset.x,
                pScaleCenter(safezoneSize * dZP2Scale) + offset.y,
                safezoneWidth * sZSScale,
                dangerZoneWidth * dangerZoneSize);
        match.getBatch().draw(dangerZone,
                pScaleCenter(safezoneSize * dZP1Scale) + offset.x,
                pScaleCenter(safezoneSize * dZP1Scale) + offset.y,
                safezoneWidth * sZSScale,
                -dangerZoneWidth * dangerZoneSize);
    }

    public Vector2 getExpandedSafezonePosition(Image expandedSafezone) {
        Vector2 position = new Vector2();

        if (event == 1 || event == 2) {
            position.x = pCenter(GAME_WIDTH) - pCenter(expandedSafezone.getWidth()) + (safezoneOffsets[0].x * 7.3f);
            position.y = pCenter(GAME_HEIGHT) - pCenter(expandedSafezone.getHeight()) + (safezoneOffsets[0].y * 7.3f);
        } else if (event == 3 || event == 4) {
            position.x = pCenter(GAME_WIDTH) - pCenter(expandedSafezone.getWidth()) + (safezoneOffsets[1].x * 7.3f);
            position.y = pCenter(GAME_HEIGHT) - pCenter(expandedSafezone.getHeight()) + (safezoneOffsets[1].y * 7.3f);
        } else if (event == 5 || event == 6) {
            position.x = pCenter(GAME_WIDTH) - pCenter(expandedSafezone.getWidth()) + (safezoneOffsets[2].x * 7.3f);
            position.y = pCenter(GAME_HEIGHT) - pCenter(expandedSafezone.getHeight()) + (safezoneOffsets[2].y * 7.3f);
        } else if (event == 7 || event == 8) {
            position.x = pCenter(GAME_WIDTH) - pCenter(expandedSafezone.getWidth()) + (safezoneOffsets[3].x * 7.3f);
            position.y = pCenter(GAME_HEIGHT) - pCenter(expandedSafezone.getHeight()) + (safezoneOffsets[3].y * 7.3f);
        }

        return position;
    }

    public float getExpandedSafezoneSize() {

        float size = 600;

         if (event == 3 || event == 4) {
            size *= .75f;
        } else if (event == 5 || event == 6) {
            size *= .5f;
        } else if (event == 7 || event == 8) {
             size *= .25f;
        }

        return size;
    }

    public boolean isInRendezvous() {
        return event % 2 == 0;
    }

    public int getSecondsToNextEvent() {
        return secondsToNextEvent;
    }

    public Vector2[] getSafezoneOffsets() {
        return safezoneOffsets;
    }

    public int getCurrentOffset() {
        if (event == 1 || event == 2) {
            return 0;
        } else if (event == 3 || event == 4) {
            return 1;
        } else if (event == 5 || event == 6) {
            return 2;
        } else if (event == 7 || event == 8) {
            return 3;
        }
        return 0;
    }

    public int getSECONDS_NEXT() {
        return SECONDS_NEXT;
    }
}
