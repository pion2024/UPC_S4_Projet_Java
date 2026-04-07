package model.entity;
import model.physic.Direction;

// sol classique 
public class Ground extends Items{


    public Ground(){
        super(true, CellType.GROUND, Direction.UP); // direction par défaut
    }

    public void onSteppedOn(MovableEntity stepper){
        // ne fais rien 
    }
}