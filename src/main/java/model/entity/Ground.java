package model.entity;
import model.physic.Position;

// sol classique 
public class Ground extends Items{

    public Ground(Position pos){
        super(pos, true, CellType.GROUND);
    }

    public void onSteppedOn(MovableEntity stepper){
        // ne fais rien 
    }
}