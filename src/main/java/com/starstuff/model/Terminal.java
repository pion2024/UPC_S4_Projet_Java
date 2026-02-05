package com.starstuff.model;
import com.starstuff.common.Vector2;

public class Terminal extends Entity {
    // 0: Up, 1: Right, 2: Down, 3: Left
    private int facingDirection; 
    public Terminal(Vector2 pos, int facing) { super(pos); this.facingDirection = facing; }
    public int getFacing() { return facingDirection; }
    
    // Helper to get the tile "in front" of the terminal
    public Vector2 getAccessPosition() {
        if (facingDirection == 0) return position.add(0, -1);
        if (facingDirection == 1) return position.add(1, 0);
        if (facingDirection == 2) return position.add(0, 1);
        if (facingDirection == 3) return position.add(-1, 0);
        return position;
    }
    @Override public String getType() { return "TERMINAL"; }
}