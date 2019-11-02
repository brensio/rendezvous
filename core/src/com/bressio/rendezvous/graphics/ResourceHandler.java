package com.bressio.rendezvous.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public final class ResourceHandler implements Disposable {

    public enum TexturePath {
        MENU_P0("textures/gui/backgrounds/menu-plane-zero.png"),
        MENU_P1("textures/gui/backgrounds/menu-plane-one.png"),
        MENU_P2("textures/gui/backgrounds/menu-plane-two.png"),
        MENU_P3("textures/gui/backgrounds/menu-plane-three.png"),
        MENU_LOGO("textures/gui/logos/menu-logo.png"),
        MATCH_MAP("textures/gui/maps/map-expanded.png"),
        MATCH_MINIMAP("textures/gui/maps/minimap.png"),
        MATCH_MINIMAP_FRAME("textures/gui/maps/minimap-frame.png"),
        PLAYER_MARK("textures/gui/maps/player-mark.png"),
        BLACK_BACKGROUND("textures/gui/backgrounds/black-background.png"),
        EVENT_BACKGROUND("textures/gui/backgrounds/event-background.png"),
        SAFEZONE("textures/world/safezone.png"),
        DANGER_ZONE("textures/world/danger-zone.png"),
        EMPTY_BAR("textures/gui/health/empty-bar.png"),
        HEALTH_BAR("textures/gui/health/health-bar.png"),
        ARMOR_BAR("textures/gui/health/armor-bar.png"),
        HEALTH_ICON("textures/gui/health/health-icon.png"),
        ARMOR_ICON("textures/gui/health/armor-icon.png"),
        VIGNETTE("textures/overlay/vignette.png"),
        INTERACTION_BUTTON("textures/world/interaction-button.png"),
        EXCHANGE_ICON("textures/gui/icons/exchange.png"),
        LOADING_SCREEN("textures/gui/backgrounds/loading-screen.png"),
        GAME_ICON("textures/gui/icons/game-icon.png"),
        EMPTY_SLOT("textures/gui/items/empty-slot.png"),
        MEDKIT_ICON("textures/gui/items/medkit.png"),
        INVENTORY("textures/gui/items/inventory.png"),
        INVISIBLE_SLOT("textures/gui/items/invisible-slot.png"),
        SELECTED_SLOT("textures/gui/items/selected-slot.png"),
        GENERIC_PROGRESS_BAR("textures/gui/bars/generic-progress-bar.png"),
        SOLDIER_BODY("textures/gui/icons/soldier-body.png"),
        COMBAT_HELMET("textures/gui/items/equipment/combat-helmet.png"),
        HALF_HELMET("textures/gui/items/equipment/half-helmet.png"),
        MILITARY_VEST("textures/gui/items/equipment/military-vest.png"),
        SOFT_VEST("textures/gui/items/equipment/soft-vest.png"),
        AW3("textures/gui/items/weapons/aw3.png"),
        G21("textures/gui/items/weapons/g21.png"),
        M20("textures/gui/items/weapons/m20.png"),
        P26("textures/gui/items/weapons/p26.png"),
        STAR("textures/gui/items/weapons/star.png"),
        W16A("textures/gui/items/weapons/w16a.png"),
        FIVE_FIVE_SIX("textures/gui/items/ammo/five-five-six.png"),
        NINE("textures/gui/items/ammo/nine.png"),
        SEVEN_SIX_TWO("textures/gui/items/ammo/seven-six-two.png"),
        BULLET("textures/projectiles/bullet.png"),
        POINTLIGHT("textures/world/pointlight.png"),
        SAFEZONE_EXPANDED("textures/world/safezone-expanded.png"),
        NEXT_SAFEZONE_EXPANDED("textures/world/next-safezone-expanded.png"),
        WATER_BACKGROUND("textures/world/water-background.png"),
        DEAD_LOOT("textures/world/dead-loot.png"),
        ALIVE_INDICATOR_BG("textures/gui/backgrounds/alive-indicator-bg.png"),
        THIN_ARROW("textures/gui/icons/thin-arrow.png"),
        SAILING("textures/gui/icons/sailing.png"),
        IS_DED("textures/gui/icons/is-ded.png");

        private String path;
        TexturePath(String path) { this.path = path; }
    }

    public enum SkinPaths {
        BUTTON_SKIN("skins/button.json", "bombard", FontPath.BOMBARD, 36, TextureAtlasPath.BUTTON_ATLAS),
        WINDOW_SKIN("skins/vis/skin/x2/uiskin.json", "bombard", FontPath.BOMBARD, 26, TextureAtlasPath.WINDOW_ATLAS);
        private String path;
        private String fontName;
        private FontPath fontPath;
        private int fontSize;
        private TextureAtlasPath atlasPath;
        SkinPaths(String path, String fontName, FontPath fontPath, int fontSize, TextureAtlasPath atlasPath) {
            this.path = path;
            this.fontName = fontName;
            this.fontPath = fontPath;
            this.fontSize = fontSize;
            this.atlasPath = atlasPath;
        }
    }

    public enum TextureAtlasPath {
        BUTTON_ATLAS("textures/gui/buttons/buttons.pack"),
        SOLDIER_ATLAS("textures/animations/unarmed/soldier-animation.pack"),
        SOLDIER_MILITARY_VEST_ATLAS("textures/animations/unarmed/soldier-military-vest-animation.pack"),
        SOLDIER_SOFT_VEST_ATLAS("textures/animations/unarmed/soldier-soft-vest-animation.pack"),
        SOLDIER_COMBAT_HELMET_ATLAS("textures/animations/unarmed/soldier-combat-helmet-animation.pack"),
        SOLDIER_HALF_HELMET_ATLAS("textures/animations/unarmed/soldier-half-helmet-animation.pack"),
        SOLDIER_MV_COMBAT_HELMET_ATLAS("textures/animations/unarmed/soldier-mv-combat-helmet-animation.pack"),
        SOLDIER_MV_HALF_HELMET_ATLAS("textures/animations/unarmed/soldier-mv-half-helmet-animation.pack"),
        SOLDIER_SV_COMBAT_HELMET_ATLAS("textures/animations/unarmed/soldier-sv-combat-helmet-animation.pack"),
        SOLDIER_SV_HALF_HELMET_ATLAS("textures/animations/unarmed/soldier-sv-half-helmet-animation.pack"),
        SOLDIER_MEDKIT_ATLAS("textures/animations/medkit/soldier-medkit-animation.pack"),
        SOLDIER_SV_MEDKIT_ATLAS("textures/animations/medkit/soldier-sv-medkit-animation.pack"),
        SOLDIER_MV_MEDKIT_ATLAS("textures/animations/medkit/soldier-mv-medkit-animation.pack"),
        SOLDIER_CT_MEDKIT_ATLAS("textures/animations/medkit/soldier-ct-medkit-animation.pack"),
        SOLDIER_HT_MEDKIT_ATLAS("textures/animations/medkit/soldier-ht-medkit-animation.pack"),
        SOLDIER_MV_CT_MEDKIT_ATLAS("textures/animations/medkit/soldier-mv-ct-medkit-animation.pack"),
        SOLDIER_MV_HT_MEDKIT_ATLAS("textures/animations/medkit/soldier-mv-ht-medkit-animation.pack"),
        SOLDIER_SV_CT_MEDKIT_ATLAS("textures/animations/medkit/soldier-sv-ct-medkit-animation.pack"),
        SOLDIER_SV_HT_MEDKIT_ATLAS("textures/animations/medkit/soldier-sv-ht-medkit-animation.pack"),
        SOLDIER_STAR_ATLAS("textures/animations/star/soldier-star-animation.pack"),
        SOLDIER_MV_STAR_ATLAS("textures/animations/star/soldier-mv-star-animation.pack"),
        SOLDIER_SV_STAR_ATLAS("textures/animations/star/soldier-sv-star-animation.pack"),
        SOLDIER_CT_STAR_ATLAS("textures/animations/star/soldier-ct-star-animation.pack"),
        SOLDIER_HT_STAR_ATLAS("textures/animations/star/soldier-ht-star-animation.pack"),
        SOLDIER_MV_CT_STAR_ATLAS("textures/animations/star/soldier-mv-ct-star-animation.pack"),
        SOLDIER_MV_HT_STAR_ATLAS("textures/animations/star/soldier-mv-ht-star-animation.pack"),
        SOLDIER_SV_CT_STAR_ATLAS("textures/animations/star/soldier-sv-ct-star-animation.pack"),
        SOLDIER_SV_HT_STAR_ATLAS("textures/animations/star/soldier-sv-ht-star-animation.pack"),
        SOLDIER_W16A_ATLAS("textures/animations/w16a/soldier-w16a-animation.pack"),
        SOLDIER_MV_W16A_ATLAS("textures/animations/w16a/soldier-mv-w16a-animation.pack"),
        SOLDIER_SV_W16A_ATLAS("textures/animations/w16a/soldier-sv-w16a-animation.pack"),
        SOLDIER_CT_W16A_ATLAS("textures/animations/w16a/soldier-ct-w16a-animation.pack"),
        SOLDIER_HT_W16A_ATLAS("textures/animations/w16a/soldier-ht-w16a-animation.pack"),
        SOLDIER_MV_CT_W16A_ATLAS("textures/animations/w16a/soldier-mv-ct-w16a-animation.pack"),
        SOLDIER_MV_HT_W16A_ATLAS("textures/animations/w16a/soldier-mv-ht-w16a-animation.pack"),
        SOLDIER_SV_CT_W16A_ATLAS("textures/animations/w16a/soldier-sv-ct-w16a-animation.pack"),
        SOLDIER_SV_HT_W16A_ATLAS("textures/animations/w16a/soldier-sv-ht-w16a-animation.pack"),
        SOLDIER_G21_ATLAS("textures/animations/g21/soldier-g21-animation.pack"),
        SOLDIER_MV_G21_ATLAS("textures/animations/g21/soldier-mv-g21-animation.pack"),
        SOLDIER_SV_G21_ATLAS("textures/animations/g21/soldier-sv-g21-animation.pack"),
        SOLDIER_CT_G21_ATLAS("textures/animations/g21/soldier-ct-g21-animation.pack"),
        SOLDIER_HT_G21_ATLAS("textures/animations/g21/soldier-ht-g21-animation.pack"),
        SOLDIER_MV_CT_G21_ATLAS("textures/animations/g21/soldier-mv-ct-g21-animation.pack"),
        SOLDIER_MV_HT_G21_ATLAS("textures/animations/g21/soldier-mv-ht-g21-animation.pack"),
        SOLDIER_SV_CT_G21_ATLAS("textures/animations/g21/soldier-sv-ct-g21-animation.pack"),
        SOLDIER_SV_HT_G21_ATLAS("textures/animations/g21/soldier-sv-ht-g21-animation.pack"),
        SOLDIER_P26_ATLAS("textures/animations/p26/soldier-p26-animation.pack"),
        SOLDIER_MV_P26_ATLAS("textures/animations/p26/soldier-mv-p26-animation.pack"),
        SOLDIER_SV_P26_ATLAS("textures/animations/p26/soldier-sv-p26-animation.pack"),
        SOLDIER_CT_P26_ATLAS("textures/animations/p26/soldier-ct-p26-animation.pack"),
        SOLDIER_HT_P26_ATLAS("textures/animations/p26/soldier-ht-p26-animation.pack"),
        SOLDIER_MV_CT_P26_ATLAS("textures/animations/p26/soldier-mv-ct-p26-animation.pack"),
        SOLDIER_MV_HT_P26_ATLAS("textures/animations/p26/soldier-mv-ht-p26-animation.pack"),
        SOLDIER_SV_CT_P26_ATLAS("textures/animations/p26/soldier-sv-ct-p26-animation.pack"),
        SOLDIER_SV_HT_P26_ATLAS("textures/animations/p26/soldier-sv-ht-p26-animation.pack"),
        SOLDIER_AW3_ATLAS("textures/animations/aw3/soldier-aw3-animation.pack"),
        SOLDIER_MV_AW3_ATLAS("textures/animations/aw3/soldier-mv-aw3-animation.pack"),
        SOLDIER_SV_AW3_ATLAS("textures/animations/aw3/soldier-sv-aw3-animation.pack"),
        SOLDIER_CT_AW3_ATLAS("textures/animations/aw3/soldier-ct-aw3-animation.pack"),
        SOLDIER_HT_AW3_ATLAS("textures/animations/aw3/soldier-ht-aw3-animation.pack"),
        SOLDIER_MV_CT_AW3_ATLAS("textures/animations/aw3/soldier-mv-ct-aw3-animation.pack"),
        SOLDIER_MV_HT_AW3_ATLAS("textures/animations/aw3/soldier-mv-ht-aw3-animation.pack"),
        SOLDIER_SV_CT_AW3_ATLAS("textures/animations/aw3/soldier-sv-ct-aw3-animation.pack"),
        SOLDIER_SV_HT_AW3_ATLAS("textures/animations/aw3/soldier-sv-ht-aw3-animation.pack"),
        SOLDIER_M20_ATLAS("textures/animations/m20/soldier-m20-animation.pack"),
        SOLDIER_MV_M20_ATLAS("textures/animations/m20/soldier-mv-m20-animation.pack"),
        SOLDIER_SV_M20_ATLAS("textures/animations/m20/soldier-sv-m20-animation.pack"),
        SOLDIER_CT_M20_ATLAS("textures/animations/m20/soldier-ct-m20-animation.pack"),
        SOLDIER_HT_M20_ATLAS("textures/animations/m20/soldier-ht-m20-animation.pack"),
        SOLDIER_MV_CT_M20_ATLAS("textures/animations/m20/soldier-mv-ct-m20-animation.pack"),
        SOLDIER_MV_HT_M20_ATLAS("textures/animations/m20/soldier-mv-ht-m20-animation.pack"),
        SOLDIER_SV_CT_M20_ATLAS("textures/animations/m20/soldier-sv-ct-m20-animation.pack"),
        SOLDIER_SV_HT_M20_ATLAS("textures/animations/m20/soldier-sv-ht-m20-animation.pack"),

        WINDOW_ATLAS("skins/vis/skin/x2/uiskin.atlas");
        private String path;
        TextureAtlasPath(String path) { this.path = path; }
    }

    public enum PixmapPath {
        MENU_CURSOR("textures/cursors/menu-cursor.png"),
        MATCH_CURSOR("textures/cursors/match-cursor.png");
        private String path;
        PixmapPath(String path) { this.path = path; }
    }

    public enum TiledMapPath {
        TILEMAP("tiles/map.tmx"),
        OVER_TILEMAP("tiles/overmap.tmx");
        private String path;
        TiledMapPath(String path) { this.path = path; }
    }

    public enum FontPath {
        BOMBARD("fonts/BOMBARD.ttf");
        public String path;
        FontPath(String path) { this.path = path; }
    }

    private AssetManager assetManager;

    public ResourceHandler() {
        assetManager = new AssetManager();
    }

    public void loadMainMenuResources() {
        assetManager.load(TexturePath.MENU_P0.path, Texture.class);
        assetManager.load(TexturePath.MENU_P1.path, Texture.class);
        assetManager.load(TexturePath.MENU_P2.path, Texture.class);
        assetManager.load(TexturePath.MENU_P3.path, Texture.class);
        assetManager.load(TexturePath.MENU_LOGO.path, Texture.class);
        assetManager.load(PixmapPath.MENU_CURSOR.path, Pixmap.class);
        assetManager.load(TexturePath.LOADING_SCREEN.path, Texture.class);
        assetManager.load(TexturePath.GAME_ICON.path, Texture.class);
        assetManager.load(TexturePath.VIGNETTE.path, Texture.class);
        assetManager.finishLoading();
    }

    public void loadMatchResources() {
        assetManager.load(PixmapPath.MATCH_CURSOR.path, Pixmap.class);
        assetManager.load(PixmapPath.MENU_CURSOR.path, Pixmap.class);
        assetManager.load(TextureAtlasPath.SOLDIER_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MILITARY_VEST_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SOFT_VEST_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_COMBAT_HELMET_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HALF_HELMET_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_COMBAT_HELMET_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HALF_HELMET_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_COMBAT_HELMET_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HALF_HELMET_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_CT_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HT_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_CT_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HT_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_CT_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HT_MEDKIT_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_CT_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HT_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_CT_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HT_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_CT_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HT_STAR_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_CT_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HT_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_CT_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HT_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_CT_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HT_W16A_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_CT_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HT_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_CT_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HT_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_CT_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HT_G21_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_CT_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HT_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_CT_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HT_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_CT_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HT_P26_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_CT_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HT_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_CT_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HT_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_CT_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HT_AW3_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_CT_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_HT_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_CT_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_MV_HT_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_CT_M20_ATLAS.path, TextureAtlas.class);
        assetManager.load(TextureAtlasPath.SOLDIER_SV_HT_M20_ATLAS.path, TextureAtlas.class);
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load(TiledMapPath.TILEMAP.path, TiledMap.class);
        assetManager.load(TiledMapPath.OVER_TILEMAP.path, TiledMap.class);
        assetManager.load(TexturePath.BLACK_BACKGROUND.path, Texture.class);
        assetManager.load(TexturePath.MATCH_MAP.path, Texture.class);
        assetManager.load(TexturePath.MATCH_MINIMAP.path, Texture.class);
        assetManager.load(TexturePath.MATCH_MINIMAP_FRAME.path, Texture.class);
        assetManager.load(TexturePath.PLAYER_MARK.path, Texture.class);
        assetManager.load(TexturePath.EVENT_BACKGROUND.path, Texture.class);
        assetManager.load(TexturePath.SAFEZONE.path, Texture.class);
        assetManager.load(TexturePath.DANGER_ZONE.path, Texture.class);
        assetManager.load(TexturePath.EMPTY_BAR.path, Texture.class);
        assetManager.load(TexturePath.HEALTH_BAR.path, Texture.class);
        assetManager.load(TexturePath.ARMOR_BAR.path, Texture.class);
        assetManager.load(TexturePath.HEALTH_ICON.path, Texture.class);
        assetManager.load(TexturePath.ARMOR_ICON.path, Texture.class);
        assetManager.load(TexturePath.VIGNETTE.path, Texture.class);
        assetManager.load(TexturePath.INTERACTION_BUTTON.path, Texture.class);
        assetManager.load(TexturePath.EXCHANGE_ICON.path, Texture.class);
        assetManager.load(TexturePath.EMPTY_SLOT.path, Texture.class);
        assetManager.load(TexturePath.MEDKIT_ICON.path, Texture.class);
        assetManager.load(TexturePath.INVENTORY.path, Texture.class);
        assetManager.load(TexturePath.INVISIBLE_SLOT.path, Texture.class);
        assetManager.load(TexturePath.SELECTED_SLOT.path, Texture.class);
        assetManager.load(TexturePath.GENERIC_PROGRESS_BAR.path, Texture.class);
        assetManager.load(TexturePath.SOLDIER_BODY.path, Texture.class);
        assetManager.load(TexturePath.COMBAT_HELMET.path, Texture.class);
        assetManager.load(TexturePath.HALF_HELMET.path, Texture.class);
        assetManager.load(TexturePath.MILITARY_VEST.path, Texture.class);
        assetManager.load(TexturePath.SOFT_VEST.path, Texture.class);
        assetManager.load(TexturePath.AW3.path, Texture.class);
        assetManager.load(TexturePath.G21.path, Texture.class);
        assetManager.load(TexturePath.M20.path, Texture.class);
        assetManager.load(TexturePath.P26.path, Texture.class);
        assetManager.load(TexturePath.STAR.path, Texture.class);
        assetManager.load(TexturePath.W16A.path, Texture.class);
        assetManager.load(TexturePath.FIVE_FIVE_SIX.path, Texture.class);
        assetManager.load(TexturePath.NINE.path, Texture.class);
        assetManager.load(TexturePath.SEVEN_SIX_TWO.path, Texture.class);
        assetManager.load(TexturePath.BULLET.path, Texture.class);
        assetManager.load(TexturePath.POINTLIGHT.path, Texture.class);
        assetManager.load(TexturePath.SAFEZONE_EXPANDED.path, Texture.class);
        assetManager.load(TexturePath.NEXT_SAFEZONE_EXPANDED.path, Texture.class);
        assetManager.load(TexturePath.WATER_BACKGROUND.path, Texture.class);
        assetManager.load(TexturePath.DEAD_LOOT.path, Texture.class);
        assetManager.load(TexturePath.ALIVE_INDICATOR_BG.path, Texture.class);
        assetManager.load(TexturePath.THIN_ARROW.path, Texture.class);
        assetManager.load(TexturePath.SAILING.path, Texture.class);
        assetManager.load(TexturePath.IS_DED.path, Texture.class);
        assetManager.finishLoading();
    }

    public Texture getTexture(TexturePath texture) {
        return assetManager.get(texture.path, Texture.class);
    }

    public TextureAtlas getTextureAtlas(TextureAtlasPath textureAtlas) {
        return assetManager.get(textureAtlas.path, TextureAtlas.class);
    }

    public Skin getSkin(SkinPaths skinPaths) {
        Skin skin = new Skin();
        skin.add(skinPaths.fontName, FontGenerator.generate(skinPaths.fontPath, skinPaths.fontSize, false));
        skin.addRegions(new TextureAtlas(Gdx.files.internal(skinPaths.atlasPath.path)));
        skin.load(Gdx.files.internal(skinPaths.path));
        return skin;
    }

    public Pixmap getPixmap(PixmapPath pixmap) {
        return assetManager.get(pixmap.path, Pixmap.class);
    }

    public TiledMap getTiledMap(TiledMapPath tiledmap) {
        return assetManager.get(tiledmap.path, TiledMap.class);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
