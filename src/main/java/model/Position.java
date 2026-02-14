package model;

public class Position {
    private Vector2 pos;    //pos = position

    public Position(Vector2 pos) {
        this.pos = pos;
    }

    public void move(Vector2 newPos) {       //deplacement avec un vecteur
        this.pos.setX(newPos.getX());
        this.pos.setY(newPos.getY());
    }

    public void move(double dX, double dY) {       //deplacement avec les coordonnées x et y
        this.pos.setX(dX);
        this.pos.setY(dY);
    }

    public double getDistance(Vector2 pos2) {
        return this.pos.getDistance(pos2);
    }
}
