package model.entity;
import java.util.ArrayList;
import java.util.List;

import model.physic.Direction;
import model.physic.Position;

// Le Pont (ou dalle vide) qui apparaît/disparaît 
public class Bridge extends Items implements Activatable {
    private boolean active; // état du pont
    private List<Switch> hostSwitches;
    private Cable cable;
    private Position pos;

    public Bridge(int id, Position pos, boolean traversable, Direction dir) {
        super(traversable, CellType.BRIDGE, dir);
        this.active = traversable;
        this.pos = pos;
        this.hostSwitches = new ArrayList<>();
        this.hostSwitches = new ArrayList<>();
        this.cable = null;
    }

    public List<Switch> getHostSwitches() {
        return this.hostSwitches;
    }

    public int getI() {
        return this.pos.getI();
    }

    public int getJ(){
        return this.pos.getJ();
    }

    public Direction getDir() {
        return this.dir;
    }

    public int getDi() {
        return this.dir.getDi();
    }

    public int getDj() {
        return this.dir.getDi();
    }

    @Override
    public void setActivated(boolean state) {
        this.active = state;
        this.traversable = this.active;
    }

    public void setCable(Cable cable) {
        this.cable = cable;
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
         // ne fais rien 
    }

    public void addSwitch(Switch sw) {
        this.hostSwitches.add(sw);
    }

    // public void updateStatus() {
    //     boolean allPressed = true;
    //     for (Switch sw : hostSwitches) {
    //         if (!sw.getIsPressed()) {
    //             allPressed = false;
    //             break;
    //         }
    //     }
    //     setActivated(allPressed);
    // }    

    public void updateStatus() {
        setActivated(this.cable.getOutput());
    }
}





