package model.entity;
import model.physic.Direction;

public class Propulsor extends Items implements Activatable{

    private final Direction propulsDirection;
    private boolean isActivate;

    public Propulsor(Direction dir){
        super(true, CellType.PROPULSOR, dir);
        this.propulsDirection = dir;
    }

    @Override
    public void setActivated(boolean state){
        this.isActivate = state;
    }

    public boolean getIsActivate(){
        return this.isActivate;
    }

    @Override
    public void onSteppedOn(MovableEntity e){
        if (isActivate){
            int newX = e.getPos().getX() + propulsDirection.getDi();
        int newY = e.getPos().getY() + propulsDirection.getDj();
        e.getPos().setX(newX);
        e.getPos().setY(newY);
        }
    }

    public Direction getpropulsDirection(){
        return propulsDirection;
    }
}
