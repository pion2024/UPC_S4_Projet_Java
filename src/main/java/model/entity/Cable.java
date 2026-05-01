package model.entity;

import model.physic.Direction;
import model.physic.Position;

public class Cable extends Items{
    private Position pos;
    private int nbOfConnection;
    private Switch sw;
    private Bridge br;
    private Direction dir;
    private Cable input2;
    private boolean in;
    private boolean in2;
    private boolean in3;
    private boolean out;
    
    public Cable(Switch sw, Direction dir) {
        super(true, CellType.CABLE, dir);
        this.pos = new Position(sw.getDi(), sw.getDj());
        this.sw = sw;
        this.in = sw.getIsPressed();
        this.out = this.in;
        this.nbOfConnection = 1;
    }

    public Cable(Position pos, Direction dir, Bridge br) {
        super(true, CellType.CABLE, dir);
        this.pos = new Position(pos.getI(), pos.getJ());
        this.br = br;
        this.in = false;
        this.out = this.in;
        this.nbOfConnection = 1;
    }

    public Cable(Position pos, Direction dir) {
        super(true, CellType.CABLE, dir);
        this.pos = new Position(pos.getI(), pos.getJ());
        this.in = false;
        this.out = this.in;
        this.nbOfConnection = 1;
    }

    public Cable(Position pos, Cable input, Direction dir) { //cellule de cable avec connexion -|- (2 entrées et 1 sortie)
        super(true, CellType.CABLE, dir);
        this.dir = dir;
        this.pos = new Position(pos.getI(), pos.getJ());
        this.in = false;
        this.in2 = false;
        this.out = this.in && this.in2;
        this.nbOfConnection = 2;
    }

    public Cable(Position pos, Cable input, Cable input2, Direction dir) {//cellule de cable avec connexion -|- (3 entrées et 1 sortie)
        super(true, CellType.CABLE, dir);
        this.input2 = input2;
        this.dir = dir;
        this.pos = new Position(pos.getI(), pos.getJ());
        this.in = false;
        this.in2 = false;
        this.in3 = false;
        this.out = this.in && this.in2 && this.in3;
        this.nbOfConnection = 3;
    }

    //Getters
    public boolean getOutput() {
        return this.out;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public CellType getType() {
        return type;
    }

    public int getNbConnection() {
        return this.nbOfConnection;
    }

    public int getI() {
        return this.pos.getI();
    }

    public int getJ() {
        return this.pos.getJ();
    }

    //Setters

    public void setInput(boolean newInput) {
        this.in = newInput;
        this.out = newInput;
    }

    public void setInput2(boolean newInput1, boolean newInput2) {
        this.in = newInput1;
        this.in2 = newInput2;
        this.out = newInput1 && newInput2;
    }

    public void setInput3(boolean newInput1, boolean newInput2, boolean newInput3) {
        this.in = newInput1;
        this.in2 = newInput2;
        this.in3 = newInput3;
        this.out = newInput1 && newInput2 && newInput3;
    }


    @Override
    public void onSteppedOn(MovableEntity stepper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onSteppedOn'");
    }
}
