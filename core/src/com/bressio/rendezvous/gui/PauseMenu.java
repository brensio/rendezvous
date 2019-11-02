package com.bressio.rendezvous.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.Rendezvous;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.MainMenu;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.pCenter;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class PauseMenu implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Skin skin;

    private Window window;
    private Match match;

    private Image background;

    private int width = 400;
    private int height = 250;

    public PauseMenu(Match match) {
        this.match = match;
        setupStage();
        forgePauseMenu();
    }

    private void setupStage() {
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, match.getBatch());
    }

    private void forgePauseMenu() {
        background = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.BLACK_BACKGROUND));
        background.setScale(GAME_WIDTH, GAME_HEIGHT);
        stage.addActor(background);

        skin = match.getResources().getSkin(ResourceHandler.SkinPaths.WINDOW_SKIN);

        window = new Window("", skin, "no-stage-background");
        window.setSize(width, height);
        window.setPosition(pCenter(GAME_WIDTH) - pCenter(width), pCenter(GAME_HEIGHT) - pCenter(height));
        window.padTop(-20);

        Label title = new Label(match.getI18n().getBundle().get("paused"), skin);
        window.add(title).row();

        TextButton resumeButton = new TextButton(match.getI18n().getBundle().get("pauseMenuResume"), skin);
        TextButton backButton = new TextButton(match.getI18n().getBundle().get("pauseMenuBack"), skin);
        TextButton exitButton = new TextButton(match.getI18n().getBundle().get("pauseMenuQuit"), skin);

        resumeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                match.delegateInputProcessor();
                match.setCursor(match.getResources().getPixmap(ResourceHandler.PixmapPath.MATCH_CURSOR), true);
                match.setState(Match.GameState.RUNNING);
            }
        });

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Rendezvous game = (Rendezvous)Gdx.app.getApplicationListener();
                game.setScreen(new MainMenu(match.getBatch()));
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        window.add(resumeButton).padTop(20).row();
        window.add(backButton).padTop(10).row();
        window.add(exitButton).padTop(10).row();

        stage.addActor(window);
    }

    public void update(float delta) {
        handleInput(delta);
    }

    private void handleInput(float delta) {
        if (Gdx.app.getInput().isKeyJustPressed(Input.Keys.ESCAPE)){
            match.delegateInputProcessor();
            match.setCursor(match.getResources().getPixmap(ResourceHandler.PixmapPath.MATCH_CURSOR), true);
            match.setState(Match.GameState.RUNNING);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
