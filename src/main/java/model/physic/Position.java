package model.physic;

public class Position {
    private int i;
    private int j;

    public Position(int i, int j){
        this.i = i;
        this.j = j;
    }

    /* ----------- getteurs / setteurs ----------- */

    public int getI(){ return this.i; }
    public int getJ(){ return this.j; }
    public void setI(int i){ this.i = i; }
    public void setJ(int j){ this.j = j; }

    // equals méthode qui permet de savoir si deux objets sont sur la même case 

    @Override
    public boolean equals(Object o){
        if(this == o) return true; // c'est le même objet
        if(o instanceof Position p) { // compare les positions des objets
            return this.i == p.getI() && this.j == p.getJ();
        }
        return false;
    }
}
