package model.entity;
import model.physic.Direction;

// La dalle qui se casse après le passage
public class SingleUseTile extends Items {
    private boolean broken = false;

    public SingleUseTile(Direction dir) {
        super(true, CellType.TILE, dir);
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
        if (!broken) {
            this.broken = true;
            this.traversable = false; // La dalle s'est effondrée 
        }
    }
}