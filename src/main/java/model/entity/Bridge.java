package model.entity;

// Le Pont (ou dalle vide) qui apparaît/disparaît 
public class Bridge extends Items implements Activatable {
    private boolean active; // état du pont

    public Bridge(int id, int x, int y, boolean traversable) {
        super(id, x, y, traversable);
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
