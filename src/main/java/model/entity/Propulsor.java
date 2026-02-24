package model.entity;
import model.physic.Direction;
import model.physic.Position;

public class Propulsor extends Items {

    private final Direction propulsDirection;

    public Propulsor(Position pos, Direction dir){
        super(pos, true, CellType.PROPULSOR);
        this.propulsDirection = dir;
    }

    @Override
    public void onSteppedOn(MovableEntity e){
        int newX = e.getPos().getX() + propulsDirection.getDx();
        int newY = e.getPos().getY() + propulsDirection.getDy();
        e.getPos().setX(newX);
        e.getPos().setY(newY);
    }

    public Direction getpropulsDirection(){
        return propulsDirection;
    }
}
