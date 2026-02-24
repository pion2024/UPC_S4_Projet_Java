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
        this.isMatrix(items);
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



    /* Méthodes privées (utilitaires) */
    private boolean isMatrix(T[][] items) {
        if (items == null) {
            throw new IllegalArgumentException("L'argument entré pour créer la matrice est null.\n");
        }
        if (items.length == 0) {
            throw new IllegalArgumentException(
                "Le nombre de ligne(s) du tableau (de tableau(x)) pour créer la matrice est 0." + 
                " Ce qui n'est pas attendu.\n"
            );
        }

        int nbLines = items.length;
        int nbColumns = items[0].length;
        for (int line = 0; line < nbLines; line++) {
            if (items[line] == null) {
                throw new IllegalArgumentException("La ligne " + (line + 1) + " est null.\n");
            }
            if (items[line].length != nbColumns) {
                throw new IllegalArgumentException(
                    "Le nombre de colonne(s) de la ligne " + (line + 1) +
                    " du tableau (de tableau(x)) pour créer la matrice est " + items[line].length +
                    ", comparé au nombre de colonne(s) de la première ligne qui est " + nbColumns + "." +
                    " Ce qui n'est pas attendu.\n"
                );
            }
        }
        return true;
    }
    
    private boolean isInside(int line, int column){
        return  (0 < line && line < nbLines) &&
                (0 < column && column < nbColumns);
    }

    /* Méthodes protégées */

    /* Méthodes publiques */

    
}
