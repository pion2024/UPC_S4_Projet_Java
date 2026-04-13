package model.entity;

import model.physic.Direction;
import model.physic.Position;

public class Wall extends Items {

    public Wall(Position pos) {
        super(false, CellType.WALL, Direction.UP);
    }

    @Override
    public boolean isTraversable() {
        return false;
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
        // rien
    }
}
