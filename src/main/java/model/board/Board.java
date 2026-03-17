package model.board;

import java.util.ArrayList;
import java.util.List;
import model.entity.Items;
import model.entity.MovableEntity;

public class Board extends Matrix<Items>{
    /* Attributs de classe */
    // Constantes

    // Variables
    
    /* Attributs d'une instance */
    // Constantes

    // Variables
    private List<MovableEntity> movableEntities; // (Blocs, Robots, Joueur)

    /* Constructeurs */
    public Board(Matrix<Items> items) {
        super(items.elements);
        this.movableEntities = new ArrayList<>();
    }

    /* Méthodes statiques */

    /* Méthodes dynamiques */
    // Getters
    public List<MovableEntity> getMovableEntities() { 
        return this.movableEntities; 
    }

    // Setters
    public void setMovableEntities(List<MovableEntity> movableEntities) {
        this.movableEntities = movableEntities;
    }
    
    /* Méthodes privées (utilitaires) */

    /* Méthodes protégées */

    /* Méthodes publiques */
    /**
     * Utilitaire pour trouver s'il y a un objet mobile à une position donnée.
     * @param x
     * @param y
     * @return
     */
    public MovableEntity getEntityAt(int x, int y) {
        for (MovableEntity e : this.movableEntities) {
            if ((e.getPos().getX() == x) &&
                (e.getPos().getY() == y)) {
                return e;
            }
        }
        return null;
    }
}
