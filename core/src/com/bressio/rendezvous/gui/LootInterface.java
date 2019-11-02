package com.bressio.rendezvous.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bressio.rendezvous.entities.objects.*;
import com.bressio.rendezvous.entities.objects.equipment.armor.Armor;
import com.bressio.rendezvous.entities.objects.equipment.helmets.Helmet;
import com.bressio.rendezvous.graphics.FontGenerator;
import com.bressio.rendezvous.graphics.ResourceHandler;
import com.bressio.rendezvous.scenes.Match;

import java.util.ArrayList;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.pCenter;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public class LootInterface implements Disposable {

    private enum ItemType {
        LOOT, INVENTORY, EQUIPMENT
    }

    private Stage stage;
    private Viewport viewport;
    private Skin skin;

    private Window inventoryWindow;
    private Window lootWindow;
    private Window equipmentWindow;
    private Match match;

    private int width = 300;
    private int height = 500;

    private ArrayList<EntityObject> lootItems;
    private ArrayList<EntityObject> inventoryItems;
    private ArrayList<EntityObject> equipmentItems;

    private EntityObject selectedLootItem;
    private EntityObject selectedInventoryItem;
    private EntityObject selectedEquipmentItem;

    private int selectedIndex;

    private Image background;
    private Image soldierBody;

    public LootInterface(Match match, ArrayList<EntityObject> lootItems, ArrayList<EntityObject> inventoryItems,
                         ArrayList<EntityObject> equipmentItems) {
        this.match = match;
        this.lootItems = lootItems;
        this.inventoryItems = inventoryItems;
        this.equipmentItems = equipmentItems;
        init();
        setupStages();
        forgeInventoryInterface();
        forgeLootInterface();
        forgeGraphics();
        forgeEquipmentInterface();
    }

    private void init() {
        selectedInventoryItem = null;
        selectedLootItem = null;
        selectedEquipmentItem = null;
        background = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.BLACK_BACKGROUND));
        background.setScale(GAME_WIDTH, GAME_HEIGHT);
    }

    private void setupStages() {
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, match.getBatch());
        stage.addActor(background);
    }

    private void forgeInventoryInterface() {
        skin = match.getResources().getSkin(ResourceHandler.SkinPaths.WINDOW_SKIN);

        inventoryWindow = new Window("", skin, "background-none");
        inventoryWindow.setSize(width, height);
        inventoryWindow.setPosition(pCenter(GAME_WIDTH) + 130, pCenter(GAME_HEIGHT) - pCenter(height));
        inventoryWindow.padTop(-20);

        inventoryWindow.add(new Label(match.getI18n().getBundle().get("inventory"), skin)).padTop(20).row();

        for (int i = 0; i < inventoryItems.size(); i++) {
            insertItemToWindow(inventoryWindow, i, ItemType.INVENTORY);
        }

        stage.addActor(inventoryWindow);
    }

    private void forgeLootInterface() {
        lootWindow = new Window("", skin, "background-none");
        lootWindow.setSize(width, height);
        lootWindow.setPosition(pCenter(GAME_WIDTH) - (width + 130), pCenter(GAME_HEIGHT) - pCenter(height));
        lootWindow.padTop(-20);

        lootWindow.add(new Label(match.getI18n().getBundle().get("loot"), skin)).padTop(20).row();

        for (int i = 0; i < lootItems.size(); i++) {
            insertItemToWindow(lootWindow, i, ItemType.LOOT);
        }

        stage.addActor(lootWindow);
    }

    private void forgeGraphics() {
        soldierBody = new Image(match.getResources().getTexture(ResourceHandler.TexturePath.SOLDIER_BODY));
        soldierBody.setPosition(pCenter(GAME_WIDTH) - pCenter(soldierBody.getWidth()), pCenter(GAME_HEIGHT) - pCenter(soldierBody.getHeight()));
        stage.addActor(soldierBody);
    }

    private void forgeEquipmentInterface() {
        equipmentWindow = new Window("", skin, "background-none");
        equipmentWindow.setSize(width, height);
        equipmentWindow.setPosition(pCenter(GAME_WIDTH) - 55, pCenter(GAME_HEIGHT) - pCenter(height) + 65);
        equipmentWindow.padTop(-20);

        for (int i = 0; i < equipmentItems.size(); i++) {
            insertItemToWindow(equipmentWindow, i, ItemType.EQUIPMENT);
        }

        stage.addActor(equipmentWindow);
    }

    private void insertItemToWindow(Window window, int index, ItemType itemType) {
        EntityObject item = itemType == ItemType.INVENTORY ?
                inventoryItems.get(index) : itemType == ItemType.LOOT ?
                lootItems.get(index) : equipmentItems.get(index);
        TextureRegion myTextureRegion = null;
        if (item != null) {
            myTextureRegion = new TextureRegion(item.getIcon());
        }
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        ImageButton itemImage = new ImageButton(myTexRegionDrawable);

        itemImage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (itemType == ItemType.INVENTORY) {
                    if (!selectSlot(index, inventoryItems)) {
                        dropItemOnSlot(index, inventoryItems, true, true,
                                false, false, false);
                    }
                } else if(itemType == ItemType.LOOT) {
                    if (!selectSlot(index, lootItems)) {
                        dropItemOnSlot(index, lootItems, true, true,
                                true, false, true);
                    }
                } else if(itemType == ItemType.EQUIPMENT) {
                    if (!selectSlot(index, equipmentItems)) {
                        dropItemOnSlot(index, equipmentItems, false, true,
                                true, true, true);
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        window.add(itemImage).padTop(10).row();
        if (item != null) {
            window.add(new Label(item.getName(),
                    new Label.LabelStyle(FontGenerator.generate(ResourceHandler.FontPath.BOMBARD,
                            14, false), Color.WHITE))).row();
        }
    }

    private boolean selectSlot(int index, ArrayList<EntityObject> originList) {
        if (originList.get(index).getClass() != Empty.class &&
                selectedLootItem == null && selectedInventoryItem == null && selectedEquipmentItem == null) {
            Texture icon = originList.get(index).getIcon();
            if (!icon.getTextureData().isPrepared()) {
                icon.getTextureData().prepare();
            }
            Pixmap cursor = icon.getTextureData().consumePixmap();
            match.setCursor(cursor, true);
            if (originList == inventoryItems) {
                selectedInventoryItem = originList.get(index);
            } else if (originList == lootItems) {
                selectedLootItem = originList.get(index);
            } else if (originList == equipmentItems) {
                selectedEquipmentItem = originList.get(index);
            }
            selectedIndex = index;
            originList.set(index, new Empty(match));
            updateWindows();
            return true;
        }
        return false;
    }

    private void dropItemOnSlot(int index, ArrayList<EntityObject> destinyList, boolean acceptInventoryItem,
                                boolean acceptLootItem, boolean acceptEquipmentItem, boolean isEquipmentSlot
            , boolean acceptInventoryEquipmentItem) {
        if (destinyList.get(index).getClass() == Empty.class) {
            if (acceptInventoryItem && selectedInventoryItem != null) {
                destinyList.set(index, selectedInventoryItem);
                onItemDropComplete();
            } else if ((acceptLootItem && selectedLootItem != null) &&
                    (equipmentFitIndex(selectedLootItem, index) || !isEquipmentSlot) &&
                    (!itemIsEquipment(selectedLootItem) || acceptInventoryEquipmentItem)) {
                destinyList.set(index, selectedLootItem);
                onItemDropComplete();
            } else if ((acceptEquipmentItem && selectedEquipmentItem != null) &&
                    (equipmentFitIndex(selectedEquipmentItem, index) || !isEquipmentSlot)) {
                destinyList.set(index, selectedEquipmentItem);
                onItemDropComplete();
            }
        } else {
            if (acceptInventoryItem && selectedInventoryItem != null &&
                    !Armor.class.isAssignableFrom(destinyList.get(index).getClass()) &&
                    !Helmet.class.isAssignableFrom(destinyList.get(index).getClass())) {
                inventoryItems.set(selectedIndex, destinyList.get(index));
                destinyList.set(index, selectedInventoryItem);
                onItemDropComplete();
            } else if ((acceptLootItem && selectedLootItem != null) &&
                    (equipmentFitIndex(selectedLootItem, index) || !isEquipmentSlot) &&
                    (!itemIsEquipment(selectedLootItem) || acceptInventoryEquipmentItem)) {
                lootItems.set(selectedIndex, destinyList.get(index));
                destinyList.set(index, selectedLootItem);
                onItemDropComplete();
            } else if ((acceptEquipmentItem && selectedEquipmentItem != null) &&
                    (equipmentFitIndex(selectedEquipmentItem, index) || !isEquipmentSlot) &&
                    equipmentsMatch(selectedEquipmentItem, destinyList.get(index))) {
                equipmentItems.set(selectedIndex, destinyList.get(index));
                destinyList.set(index, selectedEquipmentItem);
                onItemDropComplete();
            }
        }
    }

    private boolean itemIsEquipment(EntityObject selectedItem) {
        return Helmet.class.isAssignableFrom(selectedItem.getClass())  ||
                Armor.class.isAssignableFrom(selectedItem.getClass());
    }

    private boolean equipmentFitIndex(EntityObject selectedItem, int index) {
        return ((Helmet.class.isAssignableFrom(selectedItem.getClass()) && index == 0) ||
                (Armor.class.isAssignableFrom(selectedItem.getClass()) && index == 1));
    }

    private boolean equipmentsMatch(EntityObject objA, EntityObject objB) {
        return (Helmet.class.isAssignableFrom(objA.getClass()) && Helmet.class.isAssignableFrom(objB.getClass())) ||
                Armor.class.isAssignableFrom(objA.getClass()) && Armor.class.isAssignableFrom(objB.getClass());
    }

    private void onItemDropComplete() {
        selectedInventoryItem = null;
        selectedLootItem = null;
        selectedEquipmentItem = null;
        match.setCursor(match.getResources().getPixmap(ResourceHandler.PixmapPath.MENU_CURSOR), false);
        updateWindows();
    }

    private void updateWindows() {
        lootWindow.remove();
        inventoryWindow.remove();
        equipmentWindow.remove();
        forgeInventoryInterface();
        forgeLootInterface();
        forgeEquipmentInterface();
    }

    public void update(float delta) {
        handleInput(delta);
    }

    private void handleInput(float delta) {
        if ((Gdx.app.getInput().isKeyJustPressed(Input.Keys.E) ||
                Gdx.app.getInput().isKeyJustPressed(Input.Keys.A) ||
                Gdx.app.getInput().isKeyJustPressed(Input.Keys.S) ||
                Gdx.app.getInput().isKeyJustPressed(Input.Keys.W) ||
                Gdx.app.getInput().isKeyJustPressed(Input.Keys.D) ||
                Gdx.app.getInput().isKeyJustPressed(Input.Keys.ESCAPE)) &&
                selectedInventoryItem == null && selectedLootItem == null &&
                selectedEquipmentItem == null){
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
