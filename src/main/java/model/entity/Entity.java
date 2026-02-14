package model.entity;

public class Entity {

    // merci de définir ici tous les ATTRIBUTS et MÉTHODS communs à tous les types d'entités fixées sur le terrain ou pas (Block, Trigger, etc.)
    int x, y; // position a modifier plus tard 

    public Entity(int x, int y){
        this.x = x;
        this.y = y;
    }
}
