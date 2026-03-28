package model.entity;

import model.physic.Direction;

public abstract class Switch extends Items {

    protected boolean isPressed;

    public Switch(boolean traversable, Direction dir) {
        super(traversable, CellType.SWITCH, dir);
        this.isPressed = false;
    }

    // Méthode centrale pour mettre à jour l'état
    public void updateStatus(boolean active) {
        this.isPressed = active;
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