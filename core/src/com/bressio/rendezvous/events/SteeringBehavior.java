package com.bressio.rendezvous.events;

import com.badlogic.gdx.math.Vector2;

public interface SteeringBehavior {

    boolean seek(Vector2 target);

    void setVisible(boolean isVisible);
}
