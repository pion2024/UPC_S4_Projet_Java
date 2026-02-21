package model.entity;
import physic.Position;

public class Entity {

    // merci de définir ici tous les ATTRIBUTS et MÉTHODS communs à tous les types d'entités fixées sur le terrain ou pas (Block, Trigger, etc.)
    protected Position pos; // position a modifier plus tard 

    public Entity(Position pos){
        this.pos = pos;
    }

    public Position getPos(){
        return this.pos;
    }
}
