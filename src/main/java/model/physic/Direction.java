package model.physic;

public enum Direction {
    UP(0,-1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    private int di;
    private int dj;

    Direction(int i, int j){
        this.di = i;
        this.dj = j;
    }

    /* ----------- getteur / setteur --------- */
    
    public int getDi(){
        return di;
    }
    public int getDj(){
        return dj;
    }
}
