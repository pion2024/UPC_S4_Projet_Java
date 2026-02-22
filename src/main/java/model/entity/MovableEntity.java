package model.entity;

import model.physic.Position;

public abstract class MovableEntity extends Entity {
    
    public MovableEntity(Position pos) {
        super(pos);
    }

    // modifie la position 
    public void setPosition(int x, int y) {
        this.getPos().setX(x);
        this.getPos().setY(y);
    }
}
