package model.entity;
import java.util.Stack;
import model.physic.Direction;
import model.physic.Position;

public class Robot extends Agent {
    
    // Pile qui représente la mémoire d'un roboy, utile pour du backtracking
    private Stack<RobotSnapshot> history = new Stack<>();
    
    // UI Message (Speech Bubble)
    private String speechBubble = null;
    private long messageClearTime = 0;

    public Robot(Position pos){
        super(pos);
    }
    
    //@Override 
    //public String getType() { return "ROBOT"; }

    // --- Gestion de la mémoire du robot ---
    
    public void pushHistory(Position blockPosIfApplicable) {
        // On prend une snapshot de l'état du robot avant une action ou un déplacement
        history.push(new RobotSnapshot(
            this.getPos(),
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

    // --- Gestion des messages ---
    
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

    // --- Classe Snapshot ---
    
    public static class RobotSnapshot {
        public final Position pos;
        public final Direction facing;
        public final Block heldBlock; // Bloc tenu (ou null si rien n'est tenu)
        public final Position blockWorldPos; // S'il existe, position du bloc

        public RobotSnapshot(Position p, Direction f, Block h, Position bPos) {
            this.pos = p;
            this.facing = f;
            this.heldBlock = h;
            this.blockWorldPos = bPos;
        }
    }
}