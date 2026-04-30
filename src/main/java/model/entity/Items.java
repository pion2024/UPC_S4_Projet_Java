package model.entity;
import model.physic.Direction;

public abstract class Items{
    /* classe abstraite dont héritera tout les objets a stocker dans la matrice */

    protected boolean traversable;
    // protected int id; // numéro unique pour chaque instance -> utile pour activer un pont spécifique ...
    protected CellType type;
    protected Direction dir;

    public Items(boolean traversable, CellType type, Direction dir){
        this.traversable = traversable;
        this.type = type;
        this.dir = dir;
    }

    public Direction getDir(){
        return this.dir;
    }

    public int getDi() {
        return this.dir.getDi();
    }

    public int getDj() {
        return this.dir.getDi();
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

