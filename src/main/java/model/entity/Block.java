package model.entity;
import model.physic.Position;

// /**
//  * Représente un bloc que l'on peut porter (cube).
//  */

//     @Override
//     public String getType() {
//         return "BLOCK";
//     }
// }

public class Block extends Entity {

    private CellType type;
    
    public Block(Position pos) {
        super(pos);
        this.type = CellType.BLOCK;
    }

    public CellType getType(){
        return this.type;
    }
}

