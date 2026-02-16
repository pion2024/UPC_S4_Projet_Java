package model.entity;
import model.Vector2;
import model.Position;

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
    
    public Block(Position pos) {
        super(pos);
    }
}

