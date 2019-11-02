package com.bressio.rendezvous.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_HEIGHT;
import static com.bressio.rendezvous.scheme.PlayerSettings.GAME_WIDTH;

public final class InputTracker extends InputAdapter {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int ESC = 4;
    public static final int M = 5;
    public static final int E = 6;
    public static final int NUM_1 = 7;
    public static final int NUM_2 = 8;
    public static final int NUM_3 = 9;
    public static final int NUM_4 = 10;
    public static final int NUM_5 = 11;
    public static final int NUM_6 = 12;
    public static final int R = 13;
    private static final int MAX = 14;

    private static boolean[] keyPresses;

    private static Vector2 mousePosition;
    private Vector3 mousePosition3D;
    private final OrthographicCamera camera;

    private int scrollAmount = 0;

    public InputTracker(OrthographicCamera camera) {
        this.camera = camera;
        init();
    }

    private void init() {
        keyPresses = new boolean[InputTracker.MAX];
        mousePosition = new Vector2();
        mousePosition3D = new Vector3();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                keyPresses[LEFT] = true;
                break;
            case Input.Keys.D:
                keyPresses[RIGHT] = true;
                break;
            case Input.Keys.W:
                keyPresses[UP] = true;
                break;
            case Input.Keys.S:
                keyPresses[DOWN] = true;
                break;
            case Input.Keys.ESCAPE:
                keyPresses[ESC] = true;
                break;
            case Input.Keys.M:
                keyPresses[M] = true;
                break;
            case Input.Keys.E:
                keyPresses[E] = true;
                break;
            case Input.Keys.NUM_1:
                keyPresses[NUM_1] = true;
                break;
            case Input.Keys.NUM_2:
                keyPresses[NUM_2] = true;
                break;
            case Input.Keys.NUM_3:
                keyPresses[NUM_3] = true;
                break;
            case Input.Keys.NUM_4:
                keyPresses[NUM_4] = true;
                break;
            case Input.Keys.NUM_5:
                keyPresses[NUM_5] = true;
                break;
            case Input.Keys.NUM_6:
                keyPresses[NUM_6] = true;
                break;
            case Input.Keys.R:
                keyPresses[R] = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                keyPresses[LEFT] = false;
                break;
            case Input.Keys.D:
                keyPresses[RIGHT] = false;
                break;
            case Input.Keys.W:
                keyPresses[UP] = false;
                break;
            case Input.Keys.S:
                keyPresses[DOWN] = false;
                break;
            case Input.Keys.ESCAPE:
                keyPresses[ESC] = false;
                break;
            case Input.Keys.M:
                keyPresses[M] = false;
                break;
            case Input.Keys.E:
                keyPresses[E] = false;
                break;
            case Input.Keys.NUM_1:
                keyPresses[NUM_1] = false;
                break;
            case Input.Keys.NUM_2:
                keyPresses[NUM_2] = false;
                break;
            case Input.Keys.NUM_3:
                keyPresses[NUM_3] = false;
                break;
            case Input.Keys.NUM_4:
                keyPresses[NUM_4] = false;
                break;
            case Input.Keys.NUM_5:
                keyPresses[NUM_5] = false;
                break;
            case Input.Keys.NUM_6:
                keyPresses[NUM_6] = false;
                break;
            case Input.Keys.R:
                keyPresses[R] = false;
                break;
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        if(amount == 1 || amount == -1){
            scrollAmount = amount;
            return true;
        }
        scrollAmount = 0;
        return false;
    }

    public int isScrolling() {
        return scrollAmount;
    }

    public static boolean isPressed(int key) {
        return keyPresses[key];
    }

    public static int getRelativeX() {
        return (Gdx.input.getX() < GAME_WIDTH / 2 ?
                -((GAME_WIDTH / 2) - Gdx.input.getX()) : Gdx.input.getX() - (GAME_WIDTH / 2));
    }

    public static int getRelativeY() {
        return (Gdx.input.getY() < GAME_HEIGHT / 2 ?
                -((GAME_HEIGHT / 2) - Gdx.input.getY()) : Gdx.input.getY() - (GAME_HEIGHT / 2));
    }

    public static Vector2 getMousePos() {
        return mousePosition;
    }

    public void update() {
        mousePosition3D.x = Gdx.input.getX();
        mousePosition3D.y = Gdx.input.getY();
        mousePosition3D.z = 0;
        camera.unproject(mousePosition3D);
        mousePosition.x = mousePosition3D.x;
        mousePosition.y = mousePosition3D.y;
    }

    public void resetSecondaryKeys() {
        for (int i = 4; i < MAX; i++) {
            keyPresses[i] = false;
        }
    }

    public void resetAllKeys() {
        for (int i = 0; i < MAX; i++) {
            keyPresses[i] = false;
        }
    }

    public void resetScrollAmount() {
        scrollAmount = 0;
    }
}
