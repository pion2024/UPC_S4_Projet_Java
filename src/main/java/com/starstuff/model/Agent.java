package com.starstuff.model;

import com.starstuff.common.Vector3;

/**
 * Represents characters (Player or Bots).
 * Can move and carry blocks.
 */
public class Agent extends Entity {
    private Block heldBlock = null;
    private Vector3 facingDirection = new Vector3(0, 0, 1); // Default facing Z+

    public Agent(Vector3 startPos) {
        super(startPos);
    }

    public void setFacing(int dx, int dz) {
        this.facingDirection = new Vector3(dx, 0, dz);
    }

    public Vector3 getFacingDirection() {
        return facingDirection;
    }

    public boolean isCarrying() {
        return heldBlock != null;
    }

    public Block getHeldBlock() {
        return heldBlock;
    }

    public void hold(Block block) {
        this.heldBlock = block;
    }

    public Block drop() {
        Block b = this.heldBlock;
        this.heldBlock = null;
        return b;
    }

    @Override
    public String getType() {
        return "AGENT";
    }
}