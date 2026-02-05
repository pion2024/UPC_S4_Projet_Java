package com.starstuff.model;

import com.starstuff.common.Vector2;
import java.util.Stack;

public class Robot extends Agent {
    
    // History Stack for backtracking
    private Stack<RobotSnapshot> history = new Stack<>();
    
    // UI Message (Speech Bubble)
    private String speechBubble = null;
    private long messageClearTime = 0;

    public Robot(Vector2 pos) { 
        super(pos); 
    }
    
    @Override 
    public String getType() { return "ROBOT"; }

    // --- History Management ---
    
    public void pushHistory(Vector2 blockPosIfApplicable) {
        // Capture state BEFORE a move/action
        history.push(new RobotSnapshot(
            this.position,
            this.getFacing(),
            this.getHeldBlock(),
            blockPosIfApplicable
        ));
    }

    public RobotSnapshot popHistory() {
        if (history.isEmpty()) return null;
        return history.pop();
    }
    
    public void clearHistory() {
        history.clear();
    }
    
    public boolean hasHistory() {
        return !history.isEmpty();
    }

    // --- Message Management ---
    
    public void say(String msg, int durationMs) {
        this.speechBubble = msg;
        this.messageClearTime = System.currentTimeMillis() + durationMs;
    }
    
    public String getSpeechBubble() {
        if (System.currentTimeMillis() > messageClearTime) {
            speechBubble = null;
        }
        return speechBubble;
    }

    // --- Snapshot Class ---
    
    public static class RobotSnapshot {
        public final Vector2 pos;
        public final Vector2 facing;
        public final Block heldBlock; // The block held at this moment (or null)
        public final Vector2 blockWorldPos; // Where that block was in the world (if applicable)

        public RobotSnapshot(Vector2 p, Vector2 f, Block h, Vector2 bPos) {
            this.pos = p;
            this.facing = f;
            this.heldBlock = h;
            this.blockWorldPos = bPos;
        }
    }
}