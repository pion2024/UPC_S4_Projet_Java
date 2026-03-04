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
    protected T[][] items;

    /* Constructeurs */
    public Matrix(int nbLines, int nbColumns) {
        this.nbLines = nbLines;
        this.nbColumns = nbColumns;
        this.items = (T[][]) new Object[nbColumns][nbLines];
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
        if ( !this.isInside(lineIndex, columnIndex) ) {
            throw new IllegalArgumentException("Les indices sont hors bornes.");
        }
        return this.items[lineIndex][columnIndex];
    }

    // Setters
    public void setItem(int lineIndex, int columnIndex, T item) {
        if ( !this.isInside(lineIndex, columnIndex) ) {
            throw new IllegalArgumentException("Les indices sont hors bornes.");
        }
        this.items[lineIndex][columnIndex] = item;
    }

    /* Méthodes protégées */

    /* Méthodes publiques */
    /**
     * Permet de vérifier si une position donnée est dans la matrice.
     * @param lineIndex
     * @param columnIndex
     * @return
     */
    public boolean isInside(int lineIndex, int columnIndex){
        return  (0 < lineIndex && lineIndex < this.nbLines) &&
                (0 < columnIndex && columnIndex < this.nbColumns);
    }


    
}
