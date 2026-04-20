package model.command;

import model.entity.Items;
import model.entity.MovableEntity;

public class Command {      
    protected Items infraTarget; 
    protected MovableEntity entityTarget; 

    // Constructeur vide, utilisé par le symbole de fin (StopCommand)
    public Command() {}

    public Command(Object target) {
        if (target instanceof Items) {
            this.infraTarget = (Items) target;
        } else if (target instanceof MovableEntity) {
            this.entityTarget = (MovableEntity) target;
        } else {
            throw new IllegalArgumentException("Invalid target for Command: " + target);
        }
    }

    // Getters nécessaires pour que le Robot puisse lire la cible
    public Items getInfraTarget() { return infraTarget; }
    public MovableEntity getEntityTarget() { return entityTarget; }
}