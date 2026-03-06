package model.board;

/* Classe qui représente une matrice.
 * Alors on utilisera la notation matricicelle.
 */
public class Matrix<T> implements Cloneable {
    /* Attributs de classe */
    // Constantes

    // Variables
    
    /* Attributs d'une instance */
    // Constantes

    // Variables
    protected int nbLines;
    protected int nbColumns;
    protected T[][] items;

    /* Constructeurs */
    public Matrix(int nbLines, int nbColumns) {
        this.nbLines = nbLines;
        this.nbColumns = nbColumns;
        this.items = (T[][]) new Object[nbLines][nbColumns]; //corriger l'ordre de nbLines et nbColumns
        //la premiere dimension correspond au nombre de lignes, et la seconde dimension correspond au nombre de colonnes.
    }

    public Matrix(T[][] items) {
        this.nbLines = items.length;
        this.nbColumns = items[0].length;
        this.items = items;
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

    public T[][] getItems() {
        return this.items;
    }

    public T getItem(int lineIndex, int columnIndex) {
        return this.items[lineIndex][columnIndex];
    }

    // Setters
    public void setItem(int lineIndex, int columnIndex, T item) {
        this.items[lineIndex][columnIndex] = item;
    }

    // permet de verifier si on est dans la matrice 

    public boolean isInside(int line, int column){
        return line >= 0 && line < nbLines && column >= 0 && column < nbColumns;
    }

    /* Méthodes protégées */

    /* Méthodes publiques */
    
}
