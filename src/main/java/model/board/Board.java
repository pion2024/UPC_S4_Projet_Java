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
    // private Matrix<Items> map;
    Items[][] map;
    // private List<MovableEntity> movableEntities; // (Blocs, Robots, Joueur)

    /* Constructeurs */
    public Board(int length, int width) {
        // this.elements = elements;
        //this.movableEntities = new ArrayList<>();
        initMap(length,width);
    }

    /* Méthodes statiques */

    /* Méthodes dynamiques */
    // Getters

    public initMap(); //init avec du vide,ground partout 

    public Matrix<Items> getMap() {
        return this.map;
    }
 

    public Item getItemAt(int x, int y) {
        // return this.map.getItem(lineIndex, columnIndex);
        return map[x][y];
    }
    
    // public List<MovableEntity> getMovableEntities() { 
    //     return this.movableEntities; 
    // }

    // // Setters
    // public void setItems(Matrix<Items> map) {
    //     this.map = map;
    // }
    
    /* Méthodes privées (utilitaires) */

    /* Méthodes protégées */

    /* Méthodes publiques */
    /**
     * Utilitaire pour trouver s'il y a un objet mobile à une position donnée.
     * @param x
     * @param y
     * @return
     */
    // public MovableEntity getEntityAt(int x, int y) {
    //     for (MovableEntity e : movableEntities) {
    //         if ((e.getPos().getX() == x) &&
    //             (e.getPos().getY() == y)) {
    //             return e;
    //         }
    //     }
    //     return null;
    // }
}
