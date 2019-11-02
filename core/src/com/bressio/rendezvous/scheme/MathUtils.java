package com.bressio.rendezvous.scheme;

import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

public final class MathUtils {

    private MathUtils() { }

    public static int randomRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double distance(Vector2 objectA, Vector2 objectB){
        return Math.sqrt(Math.pow((objectB.x - objectA.x), 2) + Math.pow((objectB.y - objectA.y), 2));
    }

    public static float lerp(float start, float end, float alpha)
    {
        return start + alpha * (end - start);
    }
}
