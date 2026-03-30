package model.entity;

import model.physic.Direction;

public class InteractionSwitch extends Switch {

    public InteractionSwitch() {
        super(true, Direction.DOWN);
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
