package model.entity;

import model.physic.Direction;
import model.physic.Position;

public class PressureSwitch extends Switch {

    private int count = 0;

    public PressureSwitch(Position pos) {
        super(pos, true, Direction.DOWN);
    }

    @Override
    public void onSteppedOn(MovableEntity entity) {
        if (entity instanceof Robot || entity instanceof Agent) {
            count++;
            isPressed = true;
        }
    }

    @Override
    public void onExit(MovableEntity entity) {
        if (entity instanceof Robot || entity instanceof Agent) {
            count--;
            if (count <= 0) {
                count = 0;
                isPressed = false;
            }
        }
    }
}