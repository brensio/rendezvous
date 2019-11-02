package com.bressio.rendezvous.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.entities.objects.Empty;
import com.bressio.rendezvous.entities.objects.Inventory;
import com.bressio.rendezvous.forge.WorldBuilder;
import com.bressio.rendezvous.graphics.FontGenerator;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

import java.util.ArrayList;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.pCenter;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class HUD implements Disposable {

    private Stage stage;
    private Viewport viewport;

    private Label event;
    private Label timeToNextEvent;

    private Texture minimap;
    private Texture minimapFrame;
    private Texture minimapPlayerMark;
    private Rectangle minimapMask;
    private int minimapOffset = 10;

    private Image eventBackground;

    private Texture emptyBar;
    private Texture healthbar;
    private Texture armorBar;
    private Texture healthIcon;
    private Texture armorIcon;
    private Label healthPoints;
    private Label armorPoints;
    private Match match;

    private Image inventoryBackground;
    private Image selectionMarker;
    private ArrayList<Image> items;
    private int selectedSlot;

    private Texture vignette;

    private Label ammoIndicator;

    private Label aliveIndicator;
    private Label killsIndicator;

    private Label killFeedback;

    private int kills;

    public HUD(Match match) {
        this.match = match;
        init();
        setupStage();
        forgeEventDisplay();
        forgeMinimap();
        forgeHealthDisplay();
        forgeInventory();
        forgeAmmoIndicator();
        forgeEnemyStatus();
        forgeKillfeedback();
    }

    private void init() {
        kills = 0;
    }

    private void setupStage() {
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, match.getBatch());
        vignette = match.getResources().getTexture(ResourceHandler.TexturePath.VIGNETTE);
    }

    private void forgeEventDisplay() {
        eventBackground = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.EVENT_BACKGROUND));
        eventBackground.setPosition(pCenter(GAME_WIDTH) - pCenter( eventBackground.getWidth()),
                GAME_HEIGHT - eventBackground.getHeight());
        stage.addActor(eventBackground);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        event = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 12, false),
                        Color.WHITE
                )
        );
        table.add(event).fillX().padTop(3).row();

        timeToNextEvent = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 22, false),
                        Color.WHITE
                )
        );
        table.add(timeToNextEvent).padTop(1).row();

        stage.addActor(table);
    }

    private void forgeMinimap() {
        minimap = match.getResources().getTexture(ResourceHandler.TexturePath.MATCH_MINIMAP);
        minimapFrame = match.getResources().getTexture(ResourceHandler.TexturePath.MATCH_MINIMAP_FRAME);
        minimapPlayerMark = match.getResources().getTexture(ResourceHandler.TexturePath.PLAYER_MARK);
        minimapMask = new Rectangle();
        Rectangle clipBounds = new Rectangle(minimapOffset, minimapOffset, 200, 200);
        ScissorStack.calculateScissors(stage.getCamera(), stage.getBatch().getTransformMatrix(), clipBounds, minimapMask);
    }

    private void forgeHealthDisplay() {
        emptyBar = match.getResources().getTexture(ResourceHandler.TexturePath.EMPTY_BAR);
        healthbar = match.getResources().getTexture(ResourceHandler.TexturePath.HEALTH_BAR);
        armorBar = match.getResources().getTexture(ResourceHandler.TexturePath.ARMOR_BAR);
        healthIcon = match.getResources().getTexture(ResourceHandler.TexturePath.HEALTH_ICON);
        armorIcon = match.getResources().getTexture(ResourceHandler.TexturePath.ARMOR_ICON);

        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        armorPoints = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 14, false), Color.WHITE
                )
        );
        table.add(armorPoints).padBottom(3).padLeft(-350).row();
        healthPoints = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 14, false), Color.WHITE
                )
        );
        table.add(healthPoints).padBottom(15).padLeft(-350);

        stage.addActor(table);
    }

    private void forgeInventory() {
        items = new ArrayList<>();
        inventoryBackground = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.INVENTORY));
        inventoryBackground.setPosition(GAME_WIDTH - 366, 10);
        stage.addActor(inventoryBackground);

        selectedSlot = 0;

        selectionMarker = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.SELECTED_SLOT));
        Vector2 markerPos = getSelectionMarkerPosition(selectedSlot);
        selectionMarker.setPosition(markerPos.x, markerPos.y);
        stage.addActor(selectionMarker);

        float itemPos = GAME_WIDTH - 370;
        for (int i = 0; i < 6; i++) {
            items.add(new Image(match.getResources().getTexture(ResourceHandler.TexturePath.INVISIBLE_SLOT)));
            items.get(i).setPosition(itemPos, 15);
            itemPos += 55.5f;
            stage.addActor(items.get(i));
        }
    }

    private void forgeAmmoIndicator() {
        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        ammoIndicator = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 22, false), Color.WHITE
                )
        );
        table.add(ammoIndicator).padBottom(60).row();

        stage.addActor(table);
    }

    private void forgeEnemyStatus() {
        Image aliveIndicatorBackground = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.ALIVE_INDICATOR_BG));
        aliveIndicatorBackground.setPosition(0, GAME_HEIGHT - aliveIndicatorBackground.getHeight());
        stage.addActor(aliveIndicatorBackground);

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        aliveIndicator = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 26, false), Color.valueOf("afafaf")
                )
        );
        table.add(aliveIndicator).padTop(6).padLeft(6).row();
        stage.addActor(table);

        Image aliveArrow = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.THIN_ARROW));
        aliveArrow.setPosition(30, GAME_HEIGHT - aliveArrow.getHeight() - 10);
        stage.addActor(aliveArrow);

        Image killsIndicatorBackground = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.ALIVE_INDICATOR_BG));
        killsIndicatorBackground.setPosition(GAME_WIDTH, GAME_HEIGHT - killsIndicatorBackground.getHeight());
        killsIndicatorBackground.setSize(-killsIndicatorBackground.getWidth(), killsIndicatorBackground.getHeight());
        stage.addActor(killsIndicatorBackground);

        table = new Table();
        table.top().right();
        table.setFillParent(true);

        killsIndicator = new Label("",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 26, false), Color.valueOf("afafaf")
                )
        );
        table.add(killsIndicator).padTop(6).padRight(6).row();
        stage.addActor(table);

        Image killsArrow = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.THIN_ARROW));
        killsArrow.setPosition(GAME_WIDTH - 65, GAME_HEIGHT - killsArrow.getHeight() + 8);
        killsArrow.setSize(killsArrow.getWidth(), -killsArrow.getHeight());
        stage.addActor(killsArrow);
    }

    private void forgeKillfeedback() {
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        killFeedback = new Label( "",
                new Label.LabelStyle(
                        FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 26, false), Color.valueOf("ce3535")
                )
        );
        table.add(killFeedback).padTop(250);
        stage.addActor(table);
        killFeedback.setVisible(false);
    }

    private Vector2 getSelectionMarkerPosition(int index) {
        switch (index) {
            case 0: return new Vector2(GAME_WIDTH - 370, 6);
            case 1: return new Vector2(GAME_WIDTH - 314.5f, 6);
            case 2: return new Vector2(GAME_WIDTH - 259, 6);
            case 3: return new Vector2(GAME_WIDTH - 203.5f, 6);
            case 4: return new Vector2(GAME_WIDTH - 148, 6);
            case 5: return new Vector2(GAME_WIDTH - 92.5f, 6);
            default: return new Vector2(GAME_WIDTH - 37, 6);
        }
    }

    public void drawVignette(float delta) {
        match.getBatch().begin();
        match.getBatch().draw(vignette, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        match.getBatch().end();
    }

    public void drawMinimap(float delta, Vector2 playerPosition) {
        match.getBatch().begin();
        match.getBatch().flush();
        ScissorStack.pushScissors(minimapMask);
        match.getBatch().draw(minimap, - playerPosition.x * 7.2f - 82 + minimapOffset,
                - playerPosition.y * 7.2f - 82 + minimapOffset);
        match.getBatch().draw(minimapFrame, minimapOffset, minimapOffset);
        match.getBatch().draw(minimapPlayerMark,
                pCenter(minimapFrame.getWidth()) - pCenter(minimapPlayerMark.getWidth()) + minimapOffset,
                pCenter(minimapFrame.getHeight()) - pCenter(minimapPlayerMark.getHeight()) + minimapOffset);
        match.getBatch().flush();
        ScissorStack.popScissors();
        match.getBatch().end();
    }

    public void drawHealthBars(float delta, int health, int armor) {
        match.getBatch().begin();
        match.getBatch().draw(emptyBar, pCenter(GAME_WIDTH) - pCenter(emptyBar.getWidth()), 33);
        match.getBatch().draw(emptyBar, pCenter(GAME_WIDTH) - pCenter(emptyBar.getWidth()), 15);

        match.getBatch().draw(armorBar,
                pCenter(GAME_WIDTH) - pCenter(emptyBar.getWidth()),
                33,
                armor * 4,
                15);
        match.getBatch().draw(healthbar,
                pCenter(GAME_WIDTH) - pCenter(emptyBar.getWidth()),
                15,
                health * 4,
                15);

        match.getBatch().draw(armorIcon,
                pCenter(GAME_WIDTH) - pCenter(emptyBar.getWidth()) + 2, 35);
        match.getBatch().draw(healthIcon,
                pCenter(GAME_WIDTH) - pCenter(emptyBar.getWidth()) + 2, 17);

        match.getBatch().end();
    }

    public void updateHealthBars(float delta, int health, int armor) {
        armorPoints.setText(String.valueOf(armor));
        healthPoints.setText(String.valueOf(health));
    }

    public void updateAmmoIndicator(float delta, Inventory inventory) {
        int bulletsInMagazine = inventory.getBulletsInMagazine();
        int bulletsInAmmoBoxes = inventory.getBulletsInAmmoBoxes();
        if (bulletsInMagazine != -1 && bulletsInAmmoBoxes != -1) {
            ammoIndicator.setText(bulletsInMagazine + " | " + bulletsInAmmoBoxes);
        } else {
            ammoIndicator.setText("");
        }
    }

    public void updateEnemyStatus(float delta, WorldBuilder worldBuilder) {
        int alive = 40;
        for (Enemy enemy : worldBuilder.getEnemies()) {
            if (enemy.isDead()) {
                alive--;
            }
        }
        aliveIndicator.setText(String.valueOf(alive));
        killsIndicator.setText(String.valueOf(match.getPlayer().getKills()));
    }

    public void updateInventory(float delta) {
        if (match.getPlayer() != null) {
            for (int i = 0; i < 6; i++) {
                items.get(i).setDrawable(
                        new SpriteDrawable(new Sprite(
                                match.getPlayer().getInventory().getItems().get(i).getClass() == Empty.class ?
                                        match.getResources().getTexture(ResourceHandler.TexturePath.INVISIBLE_SLOT) :
                                        match.getPlayer().getInventory().getItems().get(i).getIcon()
                        ))
                );
            }
        }
    }

    public void switchSelectedSlot(int slotIndex) {
        Vector2 newPos = getSelectionMarkerPosition(slotIndex);
        selectedSlot = slotIndex;
        selectionMarker.setPosition(newPos.x, newPos.y);
    }

    public void switchSelectedSlot(boolean isGoingForward) {
        if (!isGoingForward) {
            Vector2 newPos = getSelectionMarkerPosition(selectedSlot - 1 < 0 ? 5 : selectedSlot - 1);
            selectedSlot = selectedSlot - 1 < 0 ? 5 : selectedSlot - 1;
            selectionMarker.setPosition(newPos.x, newPos.y);
        } else {
            Vector2 newPos = getSelectionMarkerPosition(selectedSlot + 1 > 5 ? 0 : selectedSlot + 1);
            selectedSlot = selectedSlot + 1 > 5 ? 0 : selectedSlot + 1;
            selectionMarker.setPosition(newPos.x, newPos.y);
        }
    }

    public void updateTimeLabel(String time) {
        timeToNextEvent.setText(time);
    }

    public void updateEventLabel(String eventLabel, boolean isInRendezvous) {
        event.setText(eventLabel);
        if (isInRendezvous) {
            timeToNextEvent.setVisible(false);
            event.setStyle(new Label.LabelStyle(
                    FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 26, false), Color.WHITE
            ));
        } else {
            timeToNextEvent.setVisible(true);
            event.setStyle(new Label.LabelStyle(
                    FontGenerator.generate(ResourceHandler.FontPath.BOMBARD, 12, false), Color.WHITE
            ));
        }
    }

    public void notifyKill(int kills) {
        killFeedback.setVisible(true);
        killFeedback.setText(kills + " " + match.getI18n().getBundle().get(kills > 1 ? "kills" : "kill"));
        this.kills = kills;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                killFeedback.setText("");
                killFeedback.setVisible(false);
            }
        }, 2);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public int getKills() {
        return kills;
    }
}
