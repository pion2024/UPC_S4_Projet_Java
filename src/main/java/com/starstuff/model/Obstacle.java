package com.starstuff.model;

import com.starstuff.common.Vector2;

/**
 * Represents a static obstacle that blocks movement.
 */
public class Obstacle extends Entity {
    public Obstacle(Vector2 pos) {
        super(pos);
    }

    @Override
    public String getType() {
        return "OBSTACLE";
    }
}