package model.entity;

import model.physic.Position;

public abstract class MovableEntity {

    protected Position pos; 

    public MovableEntity(Position pos) {
        this.pos = pos;
    }


    public Position getPos(){
        return this.pos;
    }

    // modifie la position 
    public void setPosition(int x, int y) {
        this.getPos().setI(x);
        this.getPos().setJ(y);
    }
}

