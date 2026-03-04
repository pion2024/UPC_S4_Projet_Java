package model.entity;
import model.physic.*;

// sol classique 
public class Ground extends Items{

    public Ground(int id, Position pos){
        super(id, pos, true, CellType.GROUND, Direction.UP); // direction par défaut
    }

    public void onSteppedOn(MovableEntity stepper){
        // ne fais rien 
    }
}