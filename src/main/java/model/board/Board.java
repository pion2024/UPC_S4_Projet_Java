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
    protected int nbLines;      // nbLines      doit être > 0
    protected int nbColumns;    // nbLColumns   doit être > 0
    protected Items[][] items;
    private List<MovableEntity> movableEntities; // (Blocs, Robots, Joueur)

    /* Constructeurs */
    public Board(int nbLines, int nbColumns) {
        this.nbLines = nbLines;
        this.nbColumns = nbColumns;
        this.items = new Items[nbLines][nbColumns];
    }

    public Board(Items[][] items) {
        this.nbLines = items.length;
        this.nbColumns = items[0].length;
        this.items = items;
        this.movableEntities = new ArrayList<>();
    }

    /* Méthodes statiques */

    /* Méthodes dynamiques */
    // Getters
    public int getNbColumns() {
        return this.nbColumns;
    }

    public int getNbLines() {
        return this.nbLines;
    }

    public Items[][] getItems() {
        return this.items;
    }

    public Items getItemAt(int lineIndex, int columnIndex) {
        if ( !this.isInside(lineIndex, columnIndex) ) {
            throw new IllegalArgumentException("Les indices sont hors bornes.");
        }
        return this.items[lineIndex][columnIndex];
    }

    public List<MovableEntity> getMovableEntities() { 
        return this.movableEntities; 
    }

    // Setters
    public void setItem(int lineIndex, int columnIndex, Items item) {
        if ( !this.isInside(lineIndex, columnIndex) ) {
            throw new IllegalArgumentException("Les indices sont hors bornes.");
        }
        this.items[lineIndex][columnIndex] = item;
    }

    public void setMovableEntities(List<MovableEntity> movableEntities) {
        this.movableEntities = movableEntities;
    }
    
    /* Méthodes privées (utilitaires) */

    /* Méthodes protégées */

    /* Méthodes publiques */
    /**
     * Permet de vérifier si une position donnée est dans la matrice.
     * @param lineIndex
     * @param columnIndex
     * @return
     */
    public boolean isInside(int lineIndex, int columnIndex) {
        return  (0 <= lineIndex && lineIndex < this.nbLines) &&
                (0 <= columnIndex && columnIndex < this.nbColumns);
    }

    /**
     * Utilitaire pour trouver s'il y a un objet mobile à une position donnée.
     * @param i
     * @param j
     * @return
     */
    public MovableEntity getEntityAt(int i, int j) {
        for (MovableEntity e : this.movableEntities) {
            if ((e.getPos().getI() == i) &&
                (e.getPos().getJ() == j)) {
                return e;
            }
        }
        return null;
    }
}
