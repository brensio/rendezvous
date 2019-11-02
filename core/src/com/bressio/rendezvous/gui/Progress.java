package com.bressio.rendezvous.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.graphics.FontGenerator;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.pCenter;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class Progress implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Match match;

    private Texture genericProgressBar;

    private float progressTimeCount;
    private Image progressBar;

    private Label activity;

    private float progressSpeed;

    public Progress(Match match) {
        this.match = match;
        setupStage();
        forgeProgressDisplay();
    }

    private void setupStage() {
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, match.getBatch());
    }

    private void forgeProgressDisplay() {
        genericProgressBar = match.getResources().getTexture(ResourceHandler.TexturePath.GENERIC_PROGRESS_BAR);

        Image emptyBar = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.EMPTY_BAR));
        emptyBar.setPosition(pCenter(GAME_WIDTH) - 50, 120);
        emptyBar.setSize(100, 10);
        stage.addActor(emptyBar);

        progressBar = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.GENERIC_PROGRESS_BAR));
        progressBar.setPosition(pCenter(GAME_WIDTH) - 50, 120);
        progressBar.setSize(0, 10);
        stage.addActor(progressBar);

        activity = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 18, false),
                        Color.WHITE
                )
        );
        Table table = new Table();
        table.bottom();
        table.setFillParent(true);
        table.add(activity).fillX().padBottom(100);
        stage.addActor(table);
    }

    public void fillProgressBar(float delta) {
        progressTimeCount += delta;
        if (progressTimeCount >= progressSpeed) {
            if (progressBar.getWidth() <= 100) {
                progressBar.setSize(progressBar.getWidth() + 1, 10);
            } else {
                progressBar.setSize(0, 10);
                match.getPlayer().unblockActions();
                match.getPlayer().getInventory().setSelectedBeingUsed(false);
                match.getPlayer().getInventory().applyAction();
            }
            progressTimeCount = 0;
        }
    }

    public void setActivity(String bundleId) {
        activity.setText(match.getI18n().getBundle().get(bundleId));
    }

    public Stage getStage() {
        return stage;
    }

    public void setProgressSpeed(float progressSpeed) {
        this.progressSpeed = progressSpeed;
    }

    @Override
    public void dispose() {

    }
}
