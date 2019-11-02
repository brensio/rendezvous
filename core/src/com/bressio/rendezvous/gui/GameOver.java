package com.bressio.rendezvous.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.Rendezvous;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.graphics.FontGenerator;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.MainMenu;
import com.bressio.rendezvous.scenes.Match;
import com.bressio.rendezvous.scheme.PhysicsAdapter;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.pCenter;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class GameOver implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Skin skin;

    private Window window;
    private Match match;

    private Image background;

    private int width = 400;
    private int height = 250;

    private boolean isWinner;

    public GameOver(Match match) {
        this.match = match;
        setupStage();
    }

    private void setupStage() {
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, match.getBatch());
    }

    public void forgeGameOverScreen() {
        background = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.BLACK_BACKGROUND));
        background.setScale(GAME_WIDTH, GAME_HEIGHT);
        stage.addActor(background);

        skin = match.getResources().getSkin(ResourceHandler.SkinPaths.WINDOW_SKIN);

        window = new Window("", skin, "no-stage-background");
        window.setSize(width, height);
        window.setPosition(pCenter(GAME_WIDTH), pCenter(GAME_HEIGHT) - pCenter(height));
        window.padTop(-20);

        Label title;

        if (isWinner) {
            title = new Label(match.getI18n().getBundle().get("victory"), skin);
        } else {
            title = new Label(match.getI18n().getBundle().get("failure"), skin);
        }
        window.add(title).row();

        int rank = 40;

        for (Enemy enemy : match.getWorldBuilder().getEnemies()) {
            if (enemy.isDead()) {
                rank--;
            }
        }

        window.add(new Label(match.getI18n().getBundle().get("rank") + " #" + rank,
                new Label.LabelStyle(FontGenerator.generate(ResourceHandler.FontPath.BOMBARD,
                        18, false), Color.WHITE))).padTop(20).row();

        window.add(new Label(match.getI18n().getBundle().get("kills") + ": " + match.getPlayer().getKills(),
                new Label.LabelStyle(FontGenerator.generate(ResourceHandler.FontPath.BOMBARD,
                        18, false), Color.WHITE))).padTop(5).row();

        window.add(new Label(match.getI18n().getBundle().get("matchDuration") + ": " +
                PhysicsAdapter.formatSeconds(match.getMatchDuration(), false),
                new Label.LabelStyle(FontGenerator.generate(ResourceHandler.FontPath.BOMBARD,
                        18, false), Color.WHITE))).padTop(5).row();

        TextButton backButton = new TextButton(match.getI18n().getBundle().get("pauseMenuBack"), skin);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Rendezvous game = (Rendezvous)Gdx.app.getApplicationListener();
                game.setScreen(new MainMenu(match.getBatch()));
            }
        });

        window.add(backButton).padTop(15).row();

        stage.addActor(window);

        Image icon;

        if (isWinner) {
            icon = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.SAILING));
        } else {
            icon = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.IS_DED));
        }

        icon.setPosition(pCenter(GAME_WIDTH) - height - 30, pCenter(GAME_HEIGHT) - pCenter(height));

        stage.addActor(icon);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }
}
