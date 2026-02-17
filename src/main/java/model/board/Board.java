package model.board;

import model.entity.Items;

public class Board {
    /* Attributs de classe */
    // Constantes

    // Variables
    
    /* Attributs d'une instance */
    // Constantes

    // Variables
    private Matrix<Items> elements;

    /* Constructeurs */
    public Board(Matrix<Items> elements) {
        this.elements = elements;
    }

    /* Méthodes statiques */

    /* Méthodes dynamiques */
    // Getters
    public Matrix<Items> getItems() {
        return this.elements;
    }

    // Setters
    public void setItems(Matrix<Items> elements) {
        this.elements = elements;
    }
}
