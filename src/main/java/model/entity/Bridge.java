package model.entity;
import model.physic.*;

// Le Pont (ou dalle vide) qui apparaît/disparaît 
public class Bridge extends Items implements Activatable {
    private boolean active; // état du pont

    public Bridge(int id, Position pos, boolean traversable, Direction dir) {
        super(id, pos, traversable, CellType.BRIDGE, dir);
        this.active = traversable;
    }

    @Override
    public void setActivated(boolean state) {
        this.active = state;
        this.traversable = this.active;
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
         // ne fais rien 
    }
}
