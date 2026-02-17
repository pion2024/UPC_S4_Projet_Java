package model.entity;
import model.Vector2;
import model.Position;

public abstract class Items extends Entity{
    /* classe abstraite dont héritera tout les objets a stocker dans la matrice */

    protected boolean traversable;
    protected int id; // numéro unique pour chaque instance -> utile pour activer un pont spécifique ...


    public Items(int id, Position pos, boolean traversable){
        super(pos);
        this.traversable = traversable;
        this.id = id;
    }

    public boolean isTraversable(){
        return traversable;
    }

    public int getId(){
        return this.id;
    }

    public abstract void onSteppedOn(Entity stepper);
}
