package com.starstuff.model;

import com.starstuff.common.Vector2;

/**
 * Represents the victory zone.
 */
public class TargetZone extends Entity {
    public TargetZone(Vector2 pos) {
        super(pos);
    }

    @Override
    public String getType() {
        return "TARGET_ZONE";
    }
}