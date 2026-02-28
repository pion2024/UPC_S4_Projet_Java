package model.entity;
import model.physic.Direction;
import model.physic.Position;

public abstract class Items extends Entity{
    /* classe abstraite dont héritera tout les objets a stocker dans la matrice */

    protected boolean traversable;
    protected int id; // numéro unique pour chaque instance -> utile pour activer un pont spécifique ...
    protected CellType type;
    protected Direction dir;

    public Items(int id, Position pos, boolean traversable, CellType type, Direction dir){
        super(pos);
        this.traversable = traversable;
        this.id = id;
        this.type = type;
    }

    public Direction getDir(){
        return this.dir;
    }

    public void setDir(Direction dir){
        this.dir=dir;
    }
    
    public boolean isTraversable(){
        return traversable;
    }

    public int getId(){
        return this.id;
    }

    public CellType getType(){
        return type;
    }

    public abstract void onSteppedOn(MovableEntity stepper);
}
