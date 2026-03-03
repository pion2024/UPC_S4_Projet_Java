package model.board;

import java.util.ArrayList;
import java.util.List;
import model.entity.Items;
import model.entity.MovableEntity;

public class Board {
    /* Attributs de classe */
    // Constantes

    // Variables
    
    /* Attributs d'une instance */
    // Constantes

    // Variables
    private Matrix<Items> elements;
    private List<MovableEntity> movableEntities; // (Blocs, Robots, Joueur)

    /* Constructeurs */
    public Board(Matrix<Items> elements) {
        this.elements = elements;
        this.movableEntities = new ArrayList<>();
    }

    
    // Utilitaire pour trouver s'il y a un objet mobile à une position donnée
    
    public MovableEntity getEntityAt(int x, int y) {
        for (MovableEntity e : movableEntities) {
            if (e.getPos().getX() == x && e.getPos().getY() == y) {
                return e;
            }
        }
        return null;
    }

    /* Méthodes statiques */

    /* Méthodes dynamiques */
    // Getters
    public Matrix<Items> getItems() {
        return this.elements;
    }

    public List<MovableEntity> getMovableEntities() { 
        return movableEntities; 
    }

    // Setters
    public void setItems(Matrix<Items> elements) {
        this.elements = elements;
    }

}
