package com.starstuff.model;
import com.starstuff.common.Vector2;

public class Trigger extends Entity {
    private boolean pressed = false;
    private int linkedBridgeId; // ID of the bridge this trigger controls

    public Trigger(Vector2 pos, int bridgeId) { 
        super(pos); 
        this.linkedBridgeId = bridgeId; 
    }

    public boolean isPressed() { return pressed; }
    public void setPressed(boolean p) { this.pressed = p; }
    public int getLinkedBridgeId() { return linkedBridgeId; }
    @Override public String getType() { return "TRIGGER"; }
}