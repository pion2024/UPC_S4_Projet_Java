package model.entity;
import model.physic.Direction;

// sol classique 
public class Ground extends Items{

    private Direction dir;

    public Ground(){
        super(true, CellType.GROUND); // direction par défaut
        this.dir = Direction.UP;
    }

    public void onSteppedOn(MovableEntity stepper){
        // ne fais rien 
    }
}