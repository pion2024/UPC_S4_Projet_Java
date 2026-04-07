package model.entity;
import model.physic.Direction;

public abstract class Items{
    /* classe abstraite dont héritera tout les objets a stocker dans la matrice */

    protected boolean traversable;
    // protected int id; // numéro unique pour chaque instance -> utile pour activer un pont spécifique ...
    protected CellType type;
    protected Direction dir;

    public Items(boolean traversable, CellType type){
        this.traversable = traversable;
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


    public CellType getType(){
        return type;
    }

    public abstract void onSteppedOn(MovableEntity stepper);
}

