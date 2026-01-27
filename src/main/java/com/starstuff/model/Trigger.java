package com.starstuff.model;

import com.starstuff.common.Vector3;

public class Trigger extends Entity {
    private boolean isPressed = false;
    private final int linkedBridgeId;

    public Trigger(Vector3 startPos, int linkedBridgeId) {
        super(startPos);
        this.linkedBridgeId = linkedBridgeId;
    }

    public boolean isPressed() { return isPressed; }
    public void setPressed(boolean pressed) { isPressed = pressed; }
    public int getLinkedBridgeId() { return linkedBridgeId; }
    @Override public String getType() { return "TRIGGER"; }
}