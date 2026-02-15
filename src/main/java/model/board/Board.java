package model.board;

import model.entity.Items;

public class Board {
    /* Attributs de classe */
    // Constantes

    // Variables
    
    /* Attributs d'une instance */
    // Constantes

    // Variables
    private Items[][] items;

    /* Constructeurs */
    public Board(Items[][] items) {
        this.items = items;
    }

    /* Méthodes statiques */

    /* Méthodes dynamiques */
    // Getters
    public Items[][] getItems() {
        return this.items;
    }

    // Setters
    public void setItems(Items[][] items) {
        this.items = items;
    }
}
