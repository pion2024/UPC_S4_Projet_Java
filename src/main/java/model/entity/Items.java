package model.entity;
import model.physic.Position;

public abstract class Items extends Entity{
    /* classe abstraite dont héritera tout les objets a stocker dans la matrice */

    protected boolean traversable;
    protected int id = 0; // numéro unique pour chaque instance -> utile pour activer un pont spécifique ...
    protected CellType type;
    private static int counter = 1; //compteur qui permet d'attribuer l'id à chaque item pour le rendre unique

    public Items(Position pos, boolean traversable, CellType type){
        super(pos);
        this.traversable = traversable;
        this.id = counter++;
        this.type = type;
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
