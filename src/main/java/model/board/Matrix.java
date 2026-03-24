package model.board;

/* Classe générique qui représente une matrice.
 * Alors on utilisera la notation matricielle.
 */
public class Matrix<T> {
    /* Attributs de classe */
    // Constantes

    // Variables
    
    /* Attributs d'une instance */
    // Constantes

    // Variables
    protected int nbLines;      // nbLines      doit être > 0
    protected int nbColumns;    // nbLColumns   doit être > 0
    protected T[][] elements;

    /* Constructeurs */
    public Matrix(int nbLines, int nbColumns) {
        this.nbLines = nbLines;
        this.nbColumns = nbColumns;
        this.elements = (T[][]) new Object[nbLines][nbColumns]; //corriger l'ordre de nbLines et nbColumns
        //la premiere dimension correspond au nombre de lignes, et la seconde dimension correspond au nombre de colonnes.
    }

    public Matrix(T[][] elements) {
        this.nbLines = elements.length;
        this.nbColumns = elements[0].length;
        this.elements = elements;
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

    public T[][] getElements() {
        return this.elements;
    }

    public T getElement(int lineIndex, int columnIndex) {
        if ( !this.isInside(lineIndex, columnIndex) ) {
            throw new IllegalArgumentException("Les indices sont hors bornes.");
        }
        return this.elements[lineIndex][columnIndex];
    }

    // Setters
    public void setElement(int lineIndex, int columnIndex, T element) {
        if ( !this.isInside(lineIndex, columnIndex) ) {
            throw new IllegalArgumentException("Les indices sont hors bornes.");
        }
        this.elements[lineIndex][columnIndex] = element;
    }

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


    
}
