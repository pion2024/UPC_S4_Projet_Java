package model.entity;

import model.physic.Direction;
import model.physic.Position;


public abstract class Switch extends Items {

    private Position pos;
    protected boolean isPressed;

    public Switch(Position pos, boolean traversable, Direction dir) {
        super(traversable, CellType.SWITCH, dir);
        this.isPressed = false;
        this.pos = pos;
    }

    // Méthode centrale pour mettre à jour l'état
    public void updateStatus(boolean active) {
        this.isPressed = active;
    }

    public int getI() {
        return this.pos.getI();
    }

    public int getJ(){
        return this.pos.getJ();
    }
    
    public int getDi() {
        return this.dir.getDi();
    }

    public int getDj() {
        return this.dir.getDi();
    }
    
    public boolean getIsPressed() {
        return isPressed;
    }

    // Une entité ENTRE sur la case
    public abstract void onSteppedOn(MovableEntity entity);

    // Une entité SORT de la case
    public abstract void onExit(MovableEntity entity);

    // Interaction (touche espace)
    public void onInteract(Agent agent) {
        // par défaut : rien
    }
}