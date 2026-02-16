package model;

public class Vector2 {
    private double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //accesseurs et mutateurs
    
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;        
    }

    public double getLength(){ // Donne la longueur du vecteur ||A||
        return Math.sqrt(this.x*this.x+this.y*this.y);
    }

    public double getAngle(){ // Angle du vecteur
        return Math.atan2(this.y, this.x);
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    //methodes

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 sub(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 mul(Vector2 other) {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    public Vector2 div(Vector2 other) {
        this.x /= other.x;
        this.y /= other.y;
        return this;
    }

    public Vector2 scalar(double other) {
        this.x *= other;
        this.y *= other;
        return this;
    }

    public double getDistance(Vector2 other){
        return sub(other).getLength();
    }
}
