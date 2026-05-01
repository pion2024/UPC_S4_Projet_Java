package model.entity;

import model.physic.Direction;
import model.physic.Position;

public class BlockSwitch extends Switch {

    public BlockSwitch(Position pos) {
        super(pos, true, Direction.DOWN);
    }

    @Override
    public void onSteppedOn(MovableEntity entity) {
        if (entity instanceof Block) {
            updateStatus(true);
        }
    }

    @Override
    public void onExit(MovableEntity entity) {
        if (entity instanceof Block) {
            updateStatus(false);
        }
    }
}