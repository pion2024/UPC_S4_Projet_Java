package model.entity;
import model.Vector2;
import model.Position;
import java.util.Stack;

public class Robot extends Agent {
    
    // History Stack for backtracking
    private Stack<RobotSnapshot> history = new Stack<>();
    
    // UI Message (Speech Bubble)
    private String speechBubble = null;
    private long messageClearTime = 0;

    public Robot(Position pos){
        super(pos);
    }
    
    //@Override 
    //public String getType() { return "ROBOT"; }

    // --- History Management ---
    
    public void pushHistory(Position blockPosIfApplicable) {
        // Capture state BEFORE a move/action
        history.push(new RobotSnapshot(
            this.pos,
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
        public final Position pos;
        public final Vector2 facing;
        public final Block heldBlock; // The block held at this moment (or null)
        public final Position blockWorldPos; // Where that block was in the world (if applicable)

        public RobotSnapshot(Position p, Vector2 f, Block h, Position bPos) {
            this.pos = p;
            this.facing = f;
            this.heldBlock = h;
            this.blockWorldPos = bPos;
        }
    }
}