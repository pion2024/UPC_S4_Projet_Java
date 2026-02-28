package model.entity;
import model.physic.*;

public class Propulsor extends Items {

    private final Direction propulsDirection;

    public Propulsor(int id, Position pos, Direction dir){
        super(id, pos, true, CellType.PROPULSOR, dir);
        this.propulsDirection = dir;
    }

    @Override
    public void onSteppedOn(MovableEntity e){
        int newX = e.getPos().getX() + propulsDirection.getDi();
        int newY = e.getPos().getY() + propulsDirection.getDj();
        e.getPos().setX(newX);
        e.getPos().setY(newY);
    }

    public Direction getpropulsDirection(){
        return propulsDirection;
    }
}
