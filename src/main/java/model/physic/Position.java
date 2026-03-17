package model.physic;

public class Position {
    private int i;
    private int j;

    public Position(int j, int i){
        this.i = i;
        this.j = j;
    }

    /* ----------- getteurs / setteurs ----------- */

    public int getJ(){ return this.j; }
    public int getI(){ return this.i; }
    public void setJ(int j){ this.j = j; }
    public void setI(int i){ this.i = i; }

    // equals méthode qui permet de savoir si deux objets sont sur la même case 

    @Override
    public boolean equals(Object o){
        if(this == o) return true; // c'est le même objet
        if(o instanceof Position p) { // compare les positions des objets
            return this.j == p.getJ() && this.i == p.getI();
        }
        return false;
    }
}
