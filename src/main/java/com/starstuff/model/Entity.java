package com.starstuff.model;

import com.starstuff.common.Vector3;

/**
 * Base class for all objects in the game world.
 */
public abstract class Entity {
    protected Vector3 position;
    protected final int id;
    private static int ID_COUNTER = 0;

    public Entity(Vector3 startPos) {
        this.position = startPos;
        this.id = ID_COUNTER++;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }
    
    // Using simple types for the MWE. In a real engine, use Enums.
    public abstract String getType(); 
} 
