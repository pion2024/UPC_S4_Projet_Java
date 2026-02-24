package model.entity;

import model.physic.Position;

public abstract class Interactions extends Items{
    protected boolean traversable;
    protected CellType type;
    protected CellType targetType;
    protected int targetId;
    
    public Interactions(Position pos, boolean traversable, CellType type, CellType targetType, int targetId){
        super(pos, traversable, type);
        this.targetType = targetType;
        this.targetId = targetId;
    }

    // public Items getTargetItem(CellType targetType, int targetId){
    //     return 
    // }

    public boolean isConnected(CellType targetType, int targetId){
        return (this.targetType == targetType && this.targetId == targetId);
    }
}
