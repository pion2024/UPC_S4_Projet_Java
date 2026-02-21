package model.physic;

public enum Direction {
    UP(0,-1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private int dx;
    private int dy;

    Direction(int x, int y){
        this.dx = x;
        this.dy = y;
    }

    public int getDx(){
        return dx;
    }
    public int Dy(){
        return dy;
    }
}
