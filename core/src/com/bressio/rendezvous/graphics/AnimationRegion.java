package com.bressio.rendezvous.graphics;

public enum AnimationRegion {
    SOLDIER("soldier", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_MEDKIT("soldier-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_MILITARY_VEST("soldier-military-vest", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_SOFT_VEST("soldier-soft-vest", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_COMBAT_HELMET("soldier-combat-helmet", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_HALF_HELMET("soldier-half-helmet", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_MV_COMBAT_HELMET("soldier-mv-combat-helmet", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_MV_HALF_HELMET("soldier-mv-half-helmet", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_SV_COMBAT_HELMET("soldier-sv-combat-helmet", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_SV_HALF_HELMET("soldier-sv-half-helmet", 0, 0, 81, 79, 18, .04f, 0, 0, 81, 79),
    SOLDIER_SV_MEDKIT("soldier-sv-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_MV_MEDKIT("soldier-mv-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_CT_MEDKIT("soldier-ct-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_HT_MEDKIT("soldier-ht-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_MV_CT_MEDKIT("soldier-mv-ct-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_MV_HT_MEDKIT("soldier-mv-ht-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_SV_CT_MEDKIT("soldier-sv-ct-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_SV_HT_MEDKIT("soldier-sv-ht-medkit", 0, 0, 78, 77, 18, .04f, 0, 0, 78, 77),
    SOLDIER_STAR("soldier-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_MV_STAR("soldier-mv-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_SV_STAR("soldier-sv-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_CT_STAR("soldier-ct-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_HT_STAR("soldier-ht-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_MV_CT_STAR("soldier-mv-ct-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_MV_HT_STAR("soldier-mv-ht-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_SV_CT_STAR("soldier-sv-ct-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_SV_HT_STAR("soldier-sv-ht-star", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_W16A("soldier-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_MV_W16A("soldier-mv-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_SV_W16A("soldier-sv-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_CT_W16A("soldier-ct-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_HT_W16A("soldier-ht-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_MV_CT_W16A("soldier-mv-ct-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_MV_HT_W16A("soldier-mv-ht-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_SV_CT_W16A("soldier-sv-ct-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_SV_HT_W16A("soldier-sv-ht-w16a", 0, 0, 57, 105, 18, .04f, 0, 0, 57, 105),
    SOLDIER_G21("soldier-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_MV_G21("soldier-mv-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_SV_G21("soldier-sv-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_CT_G21("soldier-ct-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_HT_G21("soldier-ht-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_MV_CT_G21("soldier-mv-ct-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_MV_HT_G21("soldier-mv-ht-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_SV_CT_G21("soldier-sv-ct-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_SV_HT_G21("soldier-sv-ht-g21", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_P26("soldier-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_MV_P26("soldier-mv-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_SV_P26("soldier-sv-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_CT_P26("soldier-ct-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_HT_P26("soldier-ht-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_MV_CT_P26("soldier-mv-ct-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_MV_HT_P26("soldier-mv-ht-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_SV_CT_P26("soldier-sv-ct-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_SV_HT_P26("soldier-sv-ht-p26", 0, 0, 54, 88, 18, .04f, 0, 0, 54, 88),
    SOLDIER_AW3("soldier-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_MV_AW3("soldier-mv-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_SV_AW3("soldier-sv-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_CT_AW3("soldier-ct-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_HT_AW3("soldier-ht-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_MV_CT_AW3("soldier-mv-ct-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_MV_HT_AW3("soldier-mv-ht-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_SV_CT_AW3("soldier-sv-ct-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_SV_HT_AW3("soldier-sv-ht-aw3", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_M20("soldier-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_MV_M20("soldier-mv-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_SV_M20("soldier-sv-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_CT_M20("soldier-ct-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_HT_M20("soldier-ht-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_MV_CT_M20("soldier-mv-ct-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_MV_HT_M20("soldier-mv-ht-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_SV_CT_M20("soldier-sv-ct-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114),
    SOLDIER_SV_HT_M20("soldier-sv-ht-m20", 0, 0, 57, 114, 18, .04f, 0, 0, 57, 114);
    AnimationRegion(String region, int startRow, int startColumn, int frameWidth, int frameHeight, int amountFrames, float frameDuration,
                    int idleTextureX, int idleTextureY, int idleTextureWidth, int idleTextureHeight) {
        this.region = region;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.amountFrames = amountFrames;
        this.frameDuration = frameDuration;
        this.idleTextureX = idleTextureX;
        this.idleTextureY = idleTextureY;
        this.idleTextureWidth = idleTextureWidth;
        this.idleTextureHeight = idleTextureHeight;
    }

    private String region;
    private int startRow;
    private int startColumn;
    private int frameWidth;
    private int frameHeight;
    private int amountFrames;
    private float frameDuration;
    private int idleTextureX;
    private int idleTextureY;
    private int idleTextureWidth;
    private int idleTextureHeight;

    public String getRegion() { return region; }
    public int getStartRow() { return startRow; }
    public int getStartColumn() { return startColumn; }
    public int getFrameWidth() { return frameWidth; }
    public int getFrameHeight() { return frameHeight; }
    public int getAmountFrames() { return amountFrames; }
    public float getFrameDuration() { return frameDuration; }
    public int getIdleTextureX() { return idleTextureX; }
    public int getIdleTextureY() { return idleTextureY; }
    public int getIdleTextureWidth() { return idleTextureWidth; }
    public int getIdleTextureHeight() { return idleTextureHeight; }
}
