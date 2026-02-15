package model.board;

import model.entity.Items;

public class Board {
    /* Attributs de classe */
    // Constantes

    // Variables
    
    /* Attributs d'une instance */
    // Constantes

    // Variables
    private Items[][] elements;

    /* Constructeurs */
    public Board(Items[][] elements) {
        this.elements = elements;
    }

    /* Méthodes statiques */

    /* Méthodes dynamiques */
    // Getters
    public Items[][] getItems() {
        return this.elements;
    }

    // Setters
    public void setItems(Items[][] elements) {
        this.elements = elements;
    }
}
