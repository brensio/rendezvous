package com.bressio.rendezvous.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.pCenter;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class MatchMap implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Image background;
    private Image map;
    private Image playerMark;
    private Match match;
    private Image safezone;

    public MatchMap(Match match) {
        this.match = match;
        setupStage();
        forgeMap();
    }

    private void setupStage() {
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, match.getBatch());
    }

    private void forgeMap() {
        background = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.BLACK_BACKGROUND));
        background.setScale(GAME_WIDTH, GAME_HEIGHT);
        map = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.MATCH_MAP));
        map.setPosition(pCenter(GAME_WIDTH) - pCenter( map.getWidth()),
                pCenter(GAME_HEIGHT) - pCenter(map.getHeight()));
        playerMark = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.PLAYER_MARK));
        safezone = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.NEXT_SAFEZONE_EXPANDED));
        safezone.setSize(safezone.getWidth(), safezone.getHeight());

        stage.addActor(background);
        stage.addActor(map);
        stage.addActor(safezone);
        stage.addActor(playerMark);
    }

    public void update(float delta, Vector2 playerPosition) {
        playerMark.setPosition(
                pCenter(GAME_WIDTH) -
                        pCenter(map.getWidth()) -
                        pCenter(playerMark.getWidth()) +
                        playerPosition.x * 7.3f,
                pCenter(GAME_HEIGHT) -
                        pCenter(map.getHeight()) -
                        pCenter(playerMark.getHeight()) +
                        playerPosition.y * 7.3f);

        safezone.setPosition(
                match.getRendezvousController().getExpandedSafezonePosition(safezone).x,
                match.getRendezvousController().getExpandedSafezonePosition(safezone).y
        );
        safezone.setSize(
                match.getRendezvousController().getExpandedSafezoneSize(),
                match.getRendezvousController().getExpandedSafezoneSize()
        );

        Texture texture;
        if (match.getRendezvousController().isInRendezvous()) {
            texture = match.getResources().getTexture(ResourceHandler.TexturePath.SAFEZONE_EXPANDED);
        } else {
            texture = match.getResources().getTexture(ResourceHandler.TexturePath.NEXT_SAFEZONE_EXPANDED);
        }
        safezone.setDrawable(new SpriteDrawable(new Sprite(texture)));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
