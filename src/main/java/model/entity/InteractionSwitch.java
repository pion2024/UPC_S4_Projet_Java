package model.entity;

import model.physic.Direction;
import model.physic.Position;

public class InteractionSwitch extends Switch {

    public InteractionSwitch(Position pos) {
        super(pos, true, Direction.DOWN);
    }

    @Override
    public void onSteppedOn(MovableEntity entity) {
        // rien
    }

    @Override
    public void onExit(MovableEntity entity) {
        // rien
    }

    @Override
    public void onInteract(Agent agent) {
        isPressed = !isPressed; // toggle
    }
}
