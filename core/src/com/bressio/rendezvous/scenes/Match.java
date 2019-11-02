package com.bressio.rendezvous.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.entities.Player;
import com.bressio.rendezvous.entities.objects.EntityObject;
import com.bressio.rendezvous.entities.objects.weapons.ars.AssaultRifle;
import com.bressio.rendezvous.entities.objects.weapons.pistols.Pistol;
import com.bressio.rendezvous.entities.objects.weapons.srs.SniperRifle;
import com.bressio.rendezvous.entities.projectiles.Bullet;
import com.bressio.rendezvous.entities.tiles.Loot;
import com.bressio.rendezvous.events.InputTracker;
import com.bressio.rendezvous.events.RendezvousController;
import com.bressio.rendezvous.events.WorldContactListener;
import com.bressio.rendezvous.forge.WorldBuilder;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.gui.*;
import com.bressio.rendezvous.languages.Internationalization;
import com.bressio.rendezvous.scheme.MathUtils;
import com.bressio.rendezvous.scheme.PhysicsAdapter;

import java.util.ArrayList;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;
import static com.bressio.rendezvous.scheme.PlayerSettings.*;

public class Match implements Screen {

    public enum GameState {
        RUNNING, PAUSED, TACTICAL, LOOTING, GAME_OVER
    }

    // game
    private SpriteBatch batch;
    private Internationalization i18n;
    private ResourceHandler resources;

    // rendering
    private OrthographicCamera camera;
    private Viewport viewport;
    private HUD hud;
    private Progress progress;
    private PauseMenu pause;
    private LootInterface loot;
    private MatchMap matchMap;
    private GameOver gameOver;
    private OrthogonalTiledMapRenderer renderer;
    private OrthogonalTiledMapRenderer overRenderer;
    private Box2DDebugRenderer collisionDebugRenderer;
    private float cameraZoom = 20;

    // world
    private World world;
    private WorldBuilder worldBuilder;
    private Player player;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private TiledMap overMap;
    private ArrayList<Bullet> bullets;
    private Texture waterBackground;
    private Texture deadLoot;

    // events
    private InputTracker input;
    private GameState state;
    private RendezvousController rendezvousController;
    private boolean gameIsOver;
    private int matchDuration;
    private float matchTimeCount;

    Match(SpriteBatch batch) {
        this.batch = batch;
        loadResources();
        setupCamera();
        setupRenderer();
        setupCursor();
        forgeWorld();
        setupInputTracker();
    }

    private void loadResources() {
        resources = new ResourceHandler();
        resources.loadMatchResources();
        mapLoader = new TmxMapLoader();
        map = resources.getTiledMap(ResourceHandler.TiledMapPath.TILEMAP);
        overMap = resources.getTiledMap(ResourceHandler.TiledMapPath.OVER_TILEMAP);
        i18n = new Internationalization();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(pScale(GAME_WIDTH) * cameraZoom, pScale(GAME_HEIGHT) * cameraZoom, camera);
        viewport.apply();
        hud = new HUD(this);
        progress = new Progress(this);
        pause = new PauseMenu(this);
        gameOver = new GameOver(this);
        matchMap = new MatchMap(this);
        camera.position.set(pScale((float) Math.sqrt(MAP_AREA)), pScale((float) Math.sqrt(MAP_AREA)), 0);
        camera.update();
    }

    private void setupRenderer() {
        renderer = new OrthogonalTiledMapRenderer(map, PhysicsAdapter.getScale());
        overRenderer = new OrthogonalTiledMapRenderer(overMap, PhysicsAdapter.getScale());
        collisionDebugRenderer = new Box2DDebugRenderer();
        setState(GameState.RUNNING);
    }

    private void setupCursor() {
        setCursor(resources.getPixmap(ResourceHandler.PixmapPath.MATCH_CURSOR), true);
    }

    private void forgeWorld() {
        world = new World(GRAVITY, true);
        worldBuilder = new WorldBuilder(this);
        player = new Player(this, 35, 5, 8, worldBuilder.getPlayerSpawnPoint());
        for (Enemy enemy : worldBuilder.getEnemies()) {
            enemy.getAi().wakeUp(worldBuilder);
        }
        world.setContactListener(new WorldContactListener(this));
        rendezvousController = new RendezvousController(this);
        bullets = new ArrayList<>();
        waterBackground = resources.getTexture(ResourceHandler.TexturePath.WATER_BACKGROUND);
        deadLoot = resources.getTexture(ResourceHandler.TexturePath.DEAD_LOOT);
        matchDuration = 0;
    }

    private void setupInputTracker() {
        input = new InputTracker(camera);
        Gdx.input.setInputProcessor(input);
    }

    private void updateCamera() {
        // smoothly moves the camera to the player's position with an offset generated by the mouse input
        camera.position.lerp(new Vector3(
                player.getBody().getPosition().x + (float)InputTracker.getRelativeX() / 300,
                player.getBody().getPosition().y - (float)InputTracker.getRelativeY() / 300,
                0), 0.05f);
        // the camera position is rounded to avoid artifacts on unfiltered textures
        camera.position.set((float) ((double) Math.round(camera.position.x * 100d) / 100d),
                (float) ((double) Math.round(camera.position.y * 100d) / 100d), camera.position.z);
        camera.update();
        renderer.setView(camera);
        overRenderer.setView(camera);

        if (SniperRifle.class.isAssignableFrom(player.getInventory().getItem(hud.getSelectedSlot()).getClass())) {
            cameraZoom = MathUtils.lerp(cameraZoom, 2, .05f);
        } else if (AssaultRifle.class.isAssignableFrom(player.getInventory().getItem(hud.getSelectedSlot()).getClass())) {
            cameraZoom = MathUtils.lerp(cameraZoom, 1.3f, .05f);
        } else if (Pistol.class.isAssignableFrom(player.getInventory().getItem(hud.getSelectedSlot()).getClass())) {
            cameraZoom = MathUtils.lerp(cameraZoom, 1.1f, .05f);
        } else {
            cameraZoom = MathUtils.lerp(cameraZoom, 1, .05f);
        }
        viewport.setWorldSize(pScale(GAME_WIDTH) * cameraZoom, pScale(GAME_HEIGHT) * cameraZoom);
        viewport.apply();
    }

    private void updateBullets() {
        for (Bullet bullet : bullets) {
            bullet.moveForward();
        }
    }

    private void updateEnemies(float delta) {
        for (Enemy enemy : worldBuilder.getEnemies()) {
            enemy.update(delta);
        }
    }

    private void update(float delta) {
        if (state == GameState.RUNNING || state == GameState.TACTICAL || state == GameState.LOOTING) {
            world.step(1 / 60f, 6, 2);
            rendezvousController.update(delta);
            player.update(delta);
            matchMap.update(delta, player.getBody().getPosition());
            updateCamera();
            updateBullets();
            checkGameOverConditions();
            updateMatchTimeCount(delta);
        }
    }

    private void lateUpdate(float delta) {
        input.update();
        handlePauseMenu(delta);
        handleMatchMap(delta);
        handleInventoryInput(delta);
        updateLoot(delta);
        if (state == GameState.RUNNING || state == GameState.TACTICAL || state == GameState.LOOTING) {
            updateEnemies(delta);
        }
    }

    private void handlePauseMenu(float delta) {
        if (InputTracker.isPressed(InputTracker.ESC)){
            pauseMatch();
        }
    }

    private void pauseMatch() {
        if (state == GameState.RUNNING || state == GameState.TACTICAL || state == GameState.LOOTING) {
            input.resetAllKeys();
            Gdx.input.setInputProcessor(pause.getStage());
            setCursor(resources.getPixmap(ResourceHandler.PixmapPath.MENU_CURSOR), false);
            setState(GameState.PAUSED);
        }
    }

    public void handleLootInterface(float delta, ArrayList<EntityObject> items) {
        if (InputTracker.isPressed(InputTracker.E) && !player.isActionsBlocked()){
            if (state == GameState.RUNNING || state == GameState.TACTICAL) {
                loot = new LootInterface(this, items, player.getInventory().getItems(),
                        player.getInventory().getEquipmentItems());
                input.resetAllKeys();
                Gdx.input.setInputProcessor(loot.getStage());
                setCursor(resources.getPixmap(ResourceHandler.PixmapPath.MENU_CURSOR), false);
                setState(GameState.LOOTING);
            }
        }
    }

    private void handleMatchMap(float delta) {
        if (InputTracker.isPressed(InputTracker.M)){
            if (state == GameState.RUNNING) {
                input.resetSecondaryKeys();
                setCursor(resources.getPixmap(ResourceHandler.PixmapPath.MENU_CURSOR), false);
                setState(GameState.TACTICAL);
            } else if (state == GameState.TACTICAL) {
                input.resetSecondaryKeys();
                setCursor(resources.getPixmap(ResourceHandler.PixmapPath.MATCH_CURSOR), true);
                setState(GameState.RUNNING);
            }
        }
    }

    private void handleInventoryInput(float delta) {
        if (!player.isActionsBlocked()) {
            if (InputTracker.isPressed(InputTracker.NUM_1)) {
                hud.switchSelectedSlot(0);
            } else if (InputTracker.isPressed(InputTracker.NUM_2)) {
                hud.switchSelectedSlot(1);
            } else if (InputTracker.isPressed(InputTracker.NUM_3)) {
                hud.switchSelectedSlot(2);
            } else if (InputTracker.isPressed(InputTracker.NUM_4)) {
                hud.switchSelectedSlot(3);
            } else if (InputTracker.isPressed(InputTracker.NUM_5)) {
                hud.switchSelectedSlot(4);
            } else if (InputTracker.isPressed(InputTracker.NUM_6)) {
                hud.switchSelectedSlot(5);
            }
            if (input.isScrolling() != 0) {
                hud.switchSelectedSlot(input.isScrolling() == 1);
                input.resetScrollAmount();
            }
        }
    }

    private void checkGameOverConditions() {
        if (player.getHealth() <= 0 && !gameIsOver) {
            gameIsOver = true;
            input.resetAllKeys();
            Gdx.input.setInputProcessor(gameOver.getStage());
            setCursor(resources.getPixmap(ResourceHandler.PixmapPath.MENU_CURSOR), false);
            gameOver.setWinner(false);
            gameOver.forgeGameOverScreen();
            setState(GameState.GAME_OVER);
        } else {
            int enemiesAlive = 39;
            for (Enemy enemy: worldBuilder.getEnemies()) {
                if (enemy.isDead()) {
                    enemiesAlive--;
                }
            }
            if (enemiesAlive == 0) {
                gameIsOver = true;
                input.resetAllKeys();
                Gdx.input.setInputProcessor(gameOver.getStage());
                setCursor(resources.getPixmap(ResourceHandler.PixmapPath.MENU_CURSOR), false);
                gameOver.setWinner(true);
                gameOver.forgeGameOverScreen();
                setState(GameState.GAME_OVER);
            }
        }
    }

    private void updateMatchTimeCount(float delta) {
        matchTimeCount += delta;
        if (matchTimeCount >= 1) {
            matchDuration++;
            matchTimeCount = 0;
        }
    }

    public ResourceHandler getResources() {
        return resources;
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public TiledMap getOverMap() {
        return overMap;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Internationalization getI18n() {
        return i18n;
    }

    public HUD getHud() {
        return hud;
    }

    public Progress getProgress() {
        return progress;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public RendezvousController getRendezvousController() {
        return rendezvousController;
    }

    public WorldBuilder getWorldBuilder() {
        return worldBuilder;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public int getMatchDuration() {
        return matchDuration;
    }

    private void renderPauseMenu(float delta) {
        batch.setProjectionMatrix(pause.getStage().getCamera().combined);
        pause.getStage().draw();
    }

    private void renderLootInterface(float delta) {
        batch.setProjectionMatrix(loot.getStage().getCamera().combined);
        loot.getStage().draw();
    }

    private void renderMap(float delta) {
        batch.setProjectionMatrix(matchMap.getStage().getCamera().combined);
        matchMap.getStage().draw();
    }

    private void renderGameOver(float delta) {
        batch.setProjectionMatrix(gameOver.getStage().getCamera().combined);
        gameOver.getStage().draw();
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void delegateInputProcessor() {
        Gdx.input.setInputProcessor(input);
    }

    public void setCursor(Pixmap pixmap, boolean isCentered) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap,
                isCentered ? pCenter(pixmap.getWidth()) : 0,
                isCentered ? pCenter(pixmap.getHeight()) : 0));
    }

    private void updateLoot(float delta) {
        for (Loot loot : worldBuilder.getLoot()) {
            loot.update(delta);
        }
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    @Override
    public void show() {

    }

    private void renderBackgroundColor() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void renderVoid() {
        batch.begin();
        batch.draw(
                waterBackground,
                pCenter(GAME_WIDTH) - 5000,
                pCenter(GAME_HEIGHT) - 5000,
                10000, 10000
        );
        batch.end();
    }

    private void renderCollisionDebug() {
        if (DEBUG_MODE) {
            collisionDebugRenderer.render(world, camera.combined);
        }
    }

    private void renderPlayer() {
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    private void renderEnemies() {
        batch.begin();
        for (Enemy enemy : worldBuilder.getEnemies()) {
            enemy.draw(batch);
        }
        batch.end();
    }

    private void renderLootInteractionButton() {
        for (Loot loot : worldBuilder.getLoot()) {
            loot.drawInteractionButton();
        }
    }

    private void renderDeadLootInteractionButton() {
        for (Enemy enemy : worldBuilder.getEnemies()) {
            enemy.drawInteractionButton();
        }
    }

    private void renderHud(float delta) {
        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.drawVignette(delta);
        hud.drawHealthBars(delta, player.getHealth(), player.getArmor());
        hud.getStage().draw();
        hud.updateHealthBars(delta, player.getHealth(), player.getArmor());
        hud.updateAmmoIndicator(delta, player.getInventory());
        hud.updateEnemyStatus(delta, worldBuilder);
        hud.updateInventory(delta);
        if (state != GameState.TACTICAL) {
            hud.drawMinimap(delta, player.getBody().getPosition());
        }
    }

    private void renderProgressDisplay(float delta) {
        if (player.getInventory().isSelectedBeingUsed()) {
            batch.setProjectionMatrix(progress.getStage().getCamera().combined);
            progress.getStage().draw();
            if (state != GameState.PAUSED && state != GameState.GAME_OVER) {
                progress.fillProgressBar(delta);
            }
        }
    }

    private void renderInterface(float delta) {
        if (state == GameState.PAUSED) {
            renderPauseMenu(delta);
            pause.update(delta);
        } else if (state == GameState.TACTICAL) {
            renderMap(delta);
        } else if (state == GameState.LOOTING) {
            renderLootInterface(delta);
            loot.update(delta);
        } else if (state == GameState.GAME_OVER) {
            renderGameOver(delta);
        }
    }

    private void renderDeadLoots(float delta) {
        batch.begin();
        for (Enemy enemy : worldBuilder.getEnemies()) {
            if (enemy.isDead()) {
                batch.draw(deadLoot, enemy.getBody().getPosition().x - pScaleCenter(deadLoot.getWidth()),
                        enemy.getBody().getPosition().y - pScaleCenter(deadLoot.getHeight()),
                        pScale(deadLoot.getWidth()), pScale(deadLoot.getHeight()));
            }
        }
        if (player.isDead()) {
            batch.draw(deadLoot, player.getBody().getPosition().x - pScaleCenter(deadLoot.getWidth()),
                    player.getBody().getPosition().y - pScaleCenter(deadLoot.getHeight()),
                    pScale(deadLoot.getWidth()), pScale(deadLoot.getHeight()));
        }
        batch.end();
    }

    private void renderBullets(float delta) {
        batch.begin();
        for (Bullet bullet : bullets) {
            bullet.draw(getBatch());
        }
        batch.end();
    }

    @Override
    public void render(float delta) {
        update(delta);
        renderBackgroundColor();
        renderVoid();
        renderer.render();
        renderCollisionDebug();
        batch.setProjectionMatrix(camera.combined);
        renderDeadLoots(delta);
        renderBullets(delta);
        renderPlayer();
        renderEnemies();
        overRenderer.render();
        renderLootInteractionButton();
        renderDeadLootInteractionButton();
        rendezvousController.render(delta);
        renderHud(delta);
        renderProgressDisplay(delta);
        renderInterface(delta);
        lateUpdate(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        pauseMatch();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
        world.dispose();
        collisionDebugRenderer.dispose();
        resources.dispose();
    }
}
