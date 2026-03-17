package model.entity;
import java.util.ArrayList;
import java.util.List;

import model.physic.Direction;

// Le Pont (ou dalle vide) qui apparaît/disparaît 
public class Bridge extends Items implements Activatable {
    private boolean active; // état du pont
    private List<Switch> hostSwitches;


    public Bridge(boolean traversable, Direction dir) {
        super(traversable, CellType.BRIDGE, dir);
        this.active = traversable;
        this.hostSwitches = new ArrayList<>();
    }

    @Override
    public void setActivated(boolean state) {
        this.active = state;
        this.traversable = this.active;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
         // ne fais rien 
    }

    public void addSwitch(Switch sw) {
        this.hostSwitches.add(sw);
    }

    public void updateStatus() {
        boolean allPressed = true;
        for (Switch sw : hostSwitches) {
            if (!sw.getIsPressed()) {
                allPressed = false;
                break;
            }
        }
        setActivated(allPressed);
    }    
}





