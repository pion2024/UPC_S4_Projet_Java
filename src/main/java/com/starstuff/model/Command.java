package com.starstuff.model;

import com.starstuff.common.Vector2;

/**
 * Represents a command for the Robot.
 */
public class Command {
    public enum Type { GO_TO, PICK_UP, DROP_AT }

    private Type type;
    private Entity targetEntity; // The block or trigger selected as parameter

    public Command(Type type) {
        this.type = type;
    }

    public void setTarget(Entity e) {
        this.targetEntity = e;
    }

    public Type getType() { return type; }
    public Entity getTarget() { return targetEntity; }
    
    @Override
    public String toString() {
        String targetName = (targetEntity == null) ? "[None]" : targetEntity.getType() + "#" + targetEntity.getId();
        return type.name() + " -> " + targetName;
    }
}