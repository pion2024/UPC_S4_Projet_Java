package model.physic;

public enum Direction {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

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
