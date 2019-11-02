package com.bressio.rendezvous.scheme;

import com.badlogic.gdx.math.Vector2;

public final class PhysicsAdapter {

    private PhysicsAdapter() { }

    public static final Vector2 GRAVITY = new Vector2(0, 0);
    private static final float SCALE = 100;
    public static final int MAP_AREA = 10240000;

    public static final short DEFAULT_TAG = 1;
    public static final short PLAYER_TAG = 2;
    public static final short BUILDING_TAG = 4;
    public static final short LOOT_TAG = 8;
    public static final short BULLET_TAG = 16;
    public static final short WATER_TAG = 32;
    public static final short ENEMY_TAG = 64;

    public static float pScale(float number) {
        return number / SCALE;
    }

    public static Vector2 pScale(float x, float y) {
        return new Vector2(x / SCALE, y / SCALE);
    }

    public static float[] pScale(float[] numbers) {
        float[] scaledNumbers = new float[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            scaledNumbers[i] = numbers[i] / SCALE;
        }
        return scaledNumbers;
    }

    public static float pScaleCenter(float number) {
        return number / 2 / SCALE;
    }

    public static Vector2 pScaleCenter(float x, float y) {
        return new Vector2(x / 2 / SCALE, y / 2 / SCALE);
    }

    public static float pCenter(float number) {
        return number / 2;
    }

    public static int pCenter(int number) {
        return number / 2;
    }

    public static float getScale() {
        return 1 / SCALE;
    }

    public static float pUnscale(float number) {
        return number * SCALE;
    }

    public static String formatSeconds(int timeInSeconds, boolean hasHours){
        int seconds = timeInSeconds % 3600 % 60;
        int minutes = timeInSeconds % 3600 / 60;
        int hours = timeInSeconds / 3600;

        String HH = hours < 10 ? "0" + hours : String.valueOf(hours);
        String MM = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        String SS = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

        return (hasHours ? HH + ":" : "" ) + MM + ":" + SS;
    }
}
