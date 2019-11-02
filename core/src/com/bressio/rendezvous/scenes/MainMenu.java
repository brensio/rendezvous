package com.bressio.rendezvous.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.Rendezvous;
import com.bressio.rendezvous.events.InputTracker;
import com.bressio.rendezvous.graphics.FontGenerator;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.languages.Internationalization;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.pCenter;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class MainMenu implements Screen {

    // game
    private SpriteBatch batch;
    private ResourceHandler resources;
    private Internationalization i18n;
    private boolean isLoading;

    // GUI
    private Skin skin;
    private Stage stage;
    private Stage loadingStage;
    private Texture backgroundP0;
    private Texture backgroundP1;
    private Texture backgroundP2;
    private Texture backgroundP3;
    private Texture vignette;
    private Texture logo;
    private Texture loadingScreen;
    private Label loadingLabel;
    private Image gameIcon;

    // rendering
    private OrthographicCamera camera;
    private Viewport viewport;

    public MainMenu(SpriteBatch batch) {
        this.batch = batch;
        loadResources();
        setupCamera();
        setupCursor();
        forgeMenu();
        forgeLoadingScreen();
    }

    private void loadResources() {
        resources = new ResourceHandler();
        resources.loadMainMenuResources();
        backgroundP0 = resources.getTexture(ResourceHandler.TexturePath.MENU_P0);
        backgroundP1 = resources.getTexture(ResourceHandler.TexturePath.MENU_P1);
        backgroundP2 = resources.getTexture(ResourceHandler.TexturePath.MENU_P2);
        backgroundP3 = resources.getTexture(ResourceHandler.TexturePath.MENU_P3);
        vignette = resources.getTexture(ResourceHandler.TexturePath.VIGNETTE);
        logo = resources.getTexture(ResourceHandler.TexturePath.MENU_LOGO);
        skin = resources.getSkin(ResourceHandler.SkinPaths.BUTTON_SKIN);
        loadingScreen = resources.getTexture(ResourceHandler.TexturePath.LOADING_SCREEN);
        gameIcon = new Image(resources.getTexture(ResourceHandler.TexturePath.GAME_ICON));
        i18n = new Internationalization();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    private void setupCursor() {
        Pixmap pixmap = resources.getPixmap(ResourceHandler.PixmapPath.MENU_CURSOR);
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 0, 0));
        pixmap.dispose();
    }

    private void forgeMenu() {
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left();
        table.setFillParent(true);

        TextButton playButton = new TextButton(i18n.getBundle().get("play"), skin, "white-button");
        TextButton optionsButton = new TextButton(i18n.getBundle().get("settings"), skin, "white-button");
        TextButton helpButton = new TextButton(i18n.getBundle().get("help"), skin, "white-button");
        TextButton exitButton = new TextButton(i18n.getBundle().get("quit"), skin, "white-button");

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isLoading = true;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        Rendezvous game = (Rendezvous)Gdx.app.getApplicationListener();
                        game.setScreen(new Match(batch));
                    }
                }, 1);
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(playButton).padLeft(pCenter(GAME_WIDTH) - pCenter(logo.getWidth()) + 20).align(Align.left).padTop(100);
        table.row();
        table.add(optionsButton).padLeft(pCenter(GAME_WIDTH) - pCenter(logo.getWidth()) + 20).align(Align.left).padTop(-20);
        table.row();
        table.add(helpButton).padLeft(pCenter(GAME_WIDTH) - pCenter(logo.getWidth()) + 20).align(Align.left).padTop(-20);
        table.row();
        table.add(exitButton).padLeft(pCenter(GAME_WIDTH) - pCenter(logo.getWidth()) + 20).align(Align.left).padTop(-20);

        stage.addActor(table);
    }

    private void forgeLoadingScreen() {
        loadingStage = new Stage(viewport, batch);

        Table table = new Table();
        table.right().bottom();
        table.setFillParent(true);

        table.add(gameIcon).padBottom(70).padRight(5);

        loadingLabel = new Label(i18n.getBundle().get("loading"),
                new Label.LabelStyle(FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 42, false), Color.WHITE));
        table.add(loadingLabel).padBottom(70).padRight(100).row();

        loadingStage.addActor(table);
    }

    private void update(float delta) {
        camera.update();
    }

    @Override
    public void show() {

    }

    private void renderBackgroundColor() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void renderBackground() {
        batch.begin();
        batch.draw(backgroundP0, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        batch.draw(backgroundP1,
                pCenter(GAME_WIDTH) - pCenter(backgroundP1.getWidth()) - InputTracker.getRelativeX() / 10,
                pCenter(GAME_HEIGHT) - pCenter(backgroundP1.getHeight()) + InputTracker.getRelativeY() / 10);
        batch.draw(logo,
                pCenter(GAME_WIDTH) - pCenter(logo.getWidth())  - InputTracker.getRelativeX() / 15,
                GAME_HEIGHT - (GAME_HEIGHT / 3) + InputTracker.getRelativeY() / 15
        );
        batch.draw(backgroundP2,
                pCenter(GAME_WIDTH) - pCenter(backgroundP2.getWidth() / 2) - InputTracker.getRelativeX() / 30,
                pCenter(GAME_HEIGHT) - pCenter(backgroundP2.getHeight() / 2) + 450 + InputTracker.getRelativeY() / 30,
                backgroundP2.getWidth() / 2, backgroundP2.getHeight() / 2
        );
        batch.draw(backgroundP3,
                pCenter(GAME_WIDTH) - pCenter(backgroundP3.getWidth() / 2) + 50 - InputTracker.getRelativeX() / 15,
                pCenter(GAME_HEIGHT) - pCenter(backgroundP3.getHeight() / 2) + 450 + InputTracker.getRelativeY() / 15,
                backgroundP3.getWidth() / 2, backgroundP3.getHeight() / 2
        );
        batch.draw(vignette, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        batch.end();
    }

    private void renderMenu() {
        stage.act();
        stage.draw();
    }

    private void renderLoadingScreen() {
        if (isLoading) {
            batch.begin();
            batch.draw(loadingScreen, 0, 0, GAME_WIDTH, GAME_HEIGHT);
            batch.end();
            loadingStage.draw();
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        renderBackgroundColor();
        renderBackground();
        renderMenu();
        renderLoadingScreen();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        resources.dispose();
    }
}
