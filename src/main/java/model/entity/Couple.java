package model.entity;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.physic.Direction;

public class Couple {
    private Board b1;
    private List<Cable> listeOfCable;
    private Switch sw;
    private Switch sw2;
    private Switch sw3;
    private Bridge br;

    public Couple(Switch sw, Bridge br) {
        this.sw = sw;
        this.br = br;
        this.listeOfCable = new ArrayList<>();
    }

    public Couple(Switch sw1, Switch sw2, Bridge br) {
        this.sw = sw1;
        this.sw2 = sw2;
        this.br = br;
        this.listeOfCable = new ArrayList<>();
    }

    public List<Cable> getListOfCable() {
        return this.listeOfCable;
    }


    public void connexion2(Switch sw, Bridge br) {
    int i = sw.getDi();
    int j = sw.getDj();
    int brI = br.getDi();
    int brJ = br.getDj();
    boolean premierCable = true;

    Direction vDir = i < brI ? Direction.DOWN : (i > brI ? Direction.UP : null);
    if (vDir != null) {
        this.listeOfCable.add(new Cable(sw, vDir));
        premierCable = false;
        i += vDir.getDi();
        while (i != brI) {
            this.listeOfCable.add(new Cable(vDir));
            i += vDir.getDi();
        }
    }

    Direction hDir = j < brJ ? Direction.RIGHT : (j > brJ ? Direction.LEFT : null);
    if (hDir != null) {
        this.listeOfCable.add(premierCable ? new Cable(sw, hDir) : new Cable(hDir));
        j += hDir.getDj();
        while (j != brJ) {
            this.listeOfCable.add(new Cable(hDir));
            j += hDir.getDj();
        }
    }
}

public void updateCable() {
    this.listeOfCable.get(0).setInput(sw.isPressed);
    for (int i = 1 ; i < this.listeOfCable.size() ; i ++) {
        this.listeOfCable.get(i).setInput(this.listeOfCable.get(i-1).getOutput());
    }
}


    // public class Cable {
    //     private Switch sw;
    //     private Bridge br;

    //     private Direction input;
    //     private Direction input2;
    //     private Direction input3;
    //     private Direction output;

    //     private boolean in;
    //     private boolean in2;
    //     private boolean in3;
    //     private boolean out;
        
    //     public Cable(Switch sw, Direction input) {
    //         this.sw = sw;
    //         this.input = input;
    //         this.output = input;
    //         this.in = sw.getIsPressed();
    //         this.out = this.in;
    //     }

    //     public Cable(Direction input, Direction output) {
    //         this.input = input;
    //         this.output = output;
    //         this.in = false;
    //         this.out = this.in;
    //     }

    //     public Cable(Direction input, Direction input2, Direction output) { //cellule de cable avec connexion -|- (2 entrées et 1 sortie)
    //         this.input = input;
    //         this.input2 = input2;
    //         this.output = output;
    //         this.in = false;
    //         this.in2 = false;
    //         this.out = this.in && this.in2;
    //     }

    //     public Cable(Direction input, Direction input2, Direction input3, Direction output) {//cellule de cable avec connexion -|- (3 entrées et 1 sortie)
    //         this.input = input;
    //         this.input2 = input2;
    //         this.input3 = input3;
    //         this.output = output;
    //         this.in = false;
    //         this.in2 = false;
    //         this.in3 = false;
    //         this.out = this.in && this.in2 && this.in3;
    //     }

    //     public void setInput(boolean newInput) {
    //         this.in = newInput;
    //         this.out = newInput;
    //     }

    //     public void setInput2(boolean newInput1, boolean newInput2) {
    //         this.in = newInput1;
    //         this.in2 = newInput2;
    //         this.out = newInput1 && newInput2;
    //     }

    //     public void setInput3(boolean newInput1, boolean newInput2, boolean newInput3) {
    //         this.in = newInput1;
    //         this.in2 = newInput2;
    //         this.in3 = newInput3;
    //         this.out = newInput1 && newInput2 && newInput3;
    //     }
    // }
}