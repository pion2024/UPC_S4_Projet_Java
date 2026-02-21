package model.entity;
import model.physic.Position;

// Le Pont (ou dalle vide) qui apparaît/disparaît 
public class Bridge extends Items implements Activatable {
    private boolean active; // état du pont

    public Bridge(int id, Position pos, boolean traversable) {
        super(id, pos, traversable);
        this.active = traversable;
    }

    @Override
    public void toggle() {
        this.active = !this.active;
        this.traversable = this.active;
    }

    @Override
    public void onSteppedOn(Entity stepper) {
         // ne fais rien 
    }
}
