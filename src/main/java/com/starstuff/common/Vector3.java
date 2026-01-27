package com.starstuff.common;

import java.util.Objects;

/**
 * A simple immutable record-like class for integer 3D coordinates.
 * Used for grid positioning.
 */
public class Vector3 {
    public final int x, y, z;

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(int dx, int dy, int dz) {
        return new Vector3(x + dx, y + dy, z + dz);
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return x == vector3.x && y == vector3.y && z == vector3.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    @Override
    public String toString() { return "(" + x + "," + y + "," + z + ")"; }
}