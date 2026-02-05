package com.starstuff.model;

import com.starstuff.common.Vector2;

// import com.starstuff.common.Vector3;

// /**
//  * Represents a carryable object (cube).
//  */
// public class Block extends Entity {
//     public Block(Vector3 startPos) {
//         super(startPos);
//     }

//     @Override
//     public String getType() {
//         return "BLOCK";
//     }
// }

public class Block extends Entity {
    public Block(Vector2 pos) { super(pos); }
    @Override public String getType() { return "BLOCK"; }
}

