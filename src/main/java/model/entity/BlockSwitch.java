package model.entity;

import model.physic.Direction;

public class BlockSwitch extends Switch {

    public BlockSwitch() {
        super(true, Direction.DOWN);
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