package model.entity;
import model.Vector2;
import model.Position;

public class Entity {

    // merci de définir ici tous les ATTRIBUTS et MÉTHODS communs à tous les types d'entités fixées sur le terrain ou pas (Block, Trigger, etc.)
    Position pos; // position a modifier plus tard 

    public Entity(Position pos){
        this.pos = pos;
    }
}
