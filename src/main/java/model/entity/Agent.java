package model.entity;
import model.Vector2;
import model.Position;

public class Agent extends Entity {
    private Block heldBlock = null;
    // la direction auquelle on fait face; par défaut le bas (0, 1)
    private Vector2 facing = new Vector2(0, 1); 

    public Agent(Position pos) {
        super(pos);
    }
    
    public boolean isCarrying() { return heldBlock != null; }
    public Block getHeldBlock() { return heldBlock; }
    public void hold(Block b) { this.heldBlock = b; }
    public Block drop() { Block b = heldBlock; heldBlock = null; return b; }
    
    public Vector2 getFacing() { return facing; }
    public void setFacing(Vector2 facing) { this.facing = facing; }

    // @Override public String getType() { return "AGENT"; }
}