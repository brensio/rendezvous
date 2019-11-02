package com.bressio.rendezvous.entities.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.forge.WorldBuilder;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.BUILDING_TAG;
import static com.bressio.rendezvous.scheme.PhysicsAdapter.pUnscale;

public class Building extends InteractiveTile {

    private enum TraverseMode {
        STORE, HIDE, SHOW
    }

    private int[][] ceilingTileIds;
    private final int CEILING_LAYER = WorldBuilder.Layer.CEILING.index;
    private final int TRANSPARENT_TILE = 40;
    private final int CEILING_AREA = 31;

    public Building(Rectangle bounds, Match match) {
        super(bounds, match, true, BUILDING_TAG);
        setUserData();
        storeCeilingTiles();
    }

    private void setUserData() {
        getFixture().setUserData(this);
    }

    private void storeCeilingTiles() {
        ceilingTileIds = new int[CEILING_AREA][CEILING_AREA];
        traverseCeilingTiles(TraverseMode.STORE);
    }

    private void traverseCeilingTiles(TraverseMode mode) {
        TiledMapTileLayer layer = (TiledMapTileLayer) getMap().getLayers().get(CEILING_LAYER);
        for (int i = -((CEILING_AREA / 2) - 1); i <= ((CEILING_AREA / 2) - 1); i++) {
            for (int j = -((CEILING_AREA / 2) - 1); j <= ((CEILING_AREA / 2) - 1); j++) {
                if (layer.getCell((int)((pUnscale(getBody().getPosition().x) / (CEILING_AREA + 1)) - i),
                        (int)((pUnscale(getBody().getPosition().y) / (CEILING_AREA + 1)) - j)) != null) {
                    switch (mode) {
                        case STORE:
                            ceilingTileIds[i + ((CEILING_AREA / 2) - 1)][j + ((CEILING_AREA / 2) - 1)] =
                                    layer.getCell(
                                            (int)((pUnscale(getBody().getPosition().x) / (CEILING_AREA + 1)) - i),
                                            (int)((pUnscale(getBody().getPosition().y) / (CEILING_AREA + 1)) - j))
                                            .getTile().getId();
                            break;
                        case HIDE:
                            layer.getCell(
                                    (int)((pUnscale(getBody().getPosition().x) / (CEILING_AREA + 1)) - i),
                                    (int)((pUnscale(getBody().getPosition().y) / (CEILING_AREA + 1)) - j))
                                    .setTile(getMap().getTileSets().getTile(TRANSPARENT_TILE));
                            break;
                        case SHOW:
                            layer.getCell(
                                    (int)((pUnscale(getBody().getPosition().x) / (CEILING_AREA + 1)) - i),
                                    (int)((pUnscale(getBody().getPosition().y) / (CEILING_AREA + 1)) - j))
                                    .setTile(getMap().getTileSets().getTile(
                                            ceilingTileIds[i + ((CEILING_AREA / 2) - 1)]
                                                    [j + ((CEILING_AREA / 2) - 1)]));
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerEnter() {
        traverseCeilingTiles(TraverseMode.HIDE);
    }

    @Override
    public void onPlayerLeave() {
        traverseCeilingTiles(TraverseMode.SHOW);
    }

    @Override
    public void onEnemyEnter(Enemy enemy) {

    }

    @Override
    public void onEnemyLeave(Enemy enemy) {

    }
}
