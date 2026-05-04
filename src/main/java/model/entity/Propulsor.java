package model.entity;
import java.util.List;

import model.entity.Cable.CableTarget;
import model.physic.Direction;
import model.physic.Position;

public class Propulsor extends Items implements Activatable, CableTarget{

    private final Direction propulsDirection;
    private boolean isActivate;
    private List<Switch> hostSwitches;
    private Position pos;
    private Cable cable;

    public Propulsor(int id, Position pos, Direction dir){
        super(true, CellType.PROPULSOR, dir);
        this.propulsDirection = dir;
        this.pos = pos;
        this.cable = null;
    }

    public int getI() {
        return this.pos.getI();
    }

    public int getJ() {
        return this.pos.getJ();
    }

    public List<Switch> getHostSwitches() {
        return this.hostSwitches;
    }

    public void addCable(Cable cable) {
        this.cable = cable;
    }

    @Override
    public void setActivated(boolean state){
        this.isActivate = state;
    }

    @Override
    public void onSteppedOn(MovableEntity e){
        if (isActivate){
            int newX = e.getPos().getI() + propulsDirection.getDi();
            int newY = e.getPos().getJ() + propulsDirection.getDj();
            e.getPos().setI(newX);
            e.getPos().setJ(newY);
        }
    }

    public Direction getpropulsDirection(){
        return propulsDirection;
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
