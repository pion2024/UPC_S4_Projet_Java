package model.entity;
import model.physic.Position;

// La dalle qui se casse après le passage
public class SingleUseTile extends Items {
    private boolean broken = false;

    public SingleUseTile(int id, Position pos) {
        super(id, pos, true, CellType.TILE);
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
        if (!broken) {
            this.broken = true;
            this.traversable = false; // La dalle s'est effondrée 
        }
    }
}