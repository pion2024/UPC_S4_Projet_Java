package model.physic;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /* ----------- getteurs / setteurs ----------- */

    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }

    // equals méthode qui permet de savoir si deux objets sont sur la même case 

    @Override
    public boolean equals(Object o){
        if(this == o) return true; // c'est le même objet
        if(o instanceof Position p) { // compare les positions des objets
            return this.x == p.getX() && this.y == p.getY();
        }
        return false;
    }
}
