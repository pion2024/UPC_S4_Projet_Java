package com.starstuff.model;
import com.starstuff.common.Vector2;


// import com.starstuff.common.Vector3;

// /**
//  * A dynamic platform that can be toggled on/off.
//  * When active, it acts as a walkable floor.
//  */
// public class Bridge extends Entity {
//     private boolean isActive = false;

//     public Bridge(Vector3 startPos) {
//         super(startPos);
//     }

//     public boolean isActive() {
//         return isActive;
//     }

//     public void setActive(boolean active) {
//         isActive = active;
//     }

//     @Override
//     public String getType() {
//         return "BRIDGE";
//     }
// }


public class Bridge extends Entity {
    private boolean active = false;
    public Bridge(Vector2 pos) { super(pos); }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
    @Override public String getType() { return "BRIDGE"; }
}

