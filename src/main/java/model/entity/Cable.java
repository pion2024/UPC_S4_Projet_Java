package model.entity;

import model.physic.Direction;

public class Cable extends Items{
    private Switch sw;
    private Bridge br;


    private Direction input2;
    private Direction input3;
    private Direction output;
    private boolean in;
    private boolean in2;
    private boolean in3;
    private boolean out;
    
    public Cable(Switch sw, Direction input) {
        super(true, CellType.CABLE, input);
        this.sw = sw;
        this.output = input;
        this.in = sw.getIsPressed();
        this.out = this.in;
    }

    public Cable(Direction input) {
        super(true, CellType.CABLE, input);
        // this.output = output;
        this.in = false;
        this.out = this.in;
    }

    public Cable(Direction input, Direction input2) { //cellule de cable avec connexion -|- (2 entrées et 1 sortie)
        super(true, CellType.CABLE, input);
        this.input2 = input2;
        // this.output = output;
        this.in = false;
        this.in2 = false;
        this.out = this.in && this.in2;
    }

    public Cable(Direction input, Direction input2, Direction input3) {//cellule de cable avec connexion -|- (3 entrées et 1 sortie)
        super(true, CellType.CABLE, input);
        this.input2 = input2;
        this.input3 = input3;
        // this.output = output;
        this.in = false;
        this.in2 = false;
        this.in3 = false;
        this.out = this.in && this.in2 && this.in3;
    }



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

    public boolean getOutput() {
        return this.out;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public CellType getType() {
        return type;
    }


    @Override
    public void onSteppedOn(MovableEntity stepper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onSteppedOn'");
    }
}
