package model.entity;
import model.physic.Direction;
import model.physic.Position;

public class Agent extends Entity {
    private Block heldBlock = null;
    // la direction auquelle on fait face; par défaut le bas (0, 1)
    private Direction facing = Direction.DOWN;

    public Agent(Position pos) {
        super(pos);
    }
    
    public boolean isCarrying() { return heldBlock != null; }
    public Block getHeldBlock() { return heldBlock; }
    public void hold(Block b) { this.heldBlock = b; }
    public Block drop() { Block b = heldBlock; heldBlock = null; return b; }
    
    public Direction getFacing() { return facing; }
    public void setFacing(Direction facing) { this.facing = facing; }

    // @Override public String getType() { return "AGENT"; }
}