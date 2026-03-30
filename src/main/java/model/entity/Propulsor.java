package model.entity;
import java.util.List;

import model.physic.Direction;
import model.physic.Position;

public class Propulsor extends Items implements Activatable{

    private final Direction propulsDirection;
    private boolean isActivate;
    private List<Switch> hostSwitches;

    public Propulsor(int id, Position pos, Direction dir){
        super(true, CellType.PROPULSOR, dir);
        this.propulsDirection = dir;
    }

    public List<Switch> getHostSwitches() {
        return this.hostSwitches;
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
