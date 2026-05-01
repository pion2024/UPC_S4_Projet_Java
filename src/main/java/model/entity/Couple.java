package model.entity;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.physic.Direction;
import model.physic.Position;

public class Couple {
    private Board b1;
    private List<Cable> listOfCables;
    private Switch sw;
    private Switch sw2;
    private Switch sw3;
    private Bridge br;

    public Couple(Switch sw, Bridge br) {
        this.sw = sw;
        this.br = br;
        this.listOfCables = new ArrayList<>();
    }

    public Couple(Switch sw1, Switch sw2, Bridge br) {
        this.sw = sw1;
        this.sw2 = sw2;
        this.br = br;
        this.listOfCables = new ArrayList<>();
    }

    public Couple(Switch sw1, Switch sw2, Switch sw3, Bridge br) {
        this.sw = sw1;
        this.sw2 = sw2;
        this.sw3 = sw3;
        this.br = br;
        this.listOfCables = new ArrayList<>();
    }

    public List<Cable> getListOfCables() {
        return this.listOfCables;
    }

    public void connexion(Switch sw, Bridge br, Board board) {
        int i = sw.getI();
        int j = sw.getJ();
        int brI = br.getI();
        int brJ = br.getJ();
        boolean premierCable = true;

        Direction vDir = i < brI ? Direction.DOWN : (i > brI ? Direction.UP : null);
        if (vDir != null) {
            this.listOfCables.add(new Cable(sw, vDir));
            premierCable = false;
            i += vDir.getDi();
            while (i != brI) {
                Position pos = new Position(i, j);
                if (board.isInside(i, j) && board.getItemAt(i, j).getType() == CellType.GROUND)
                    this.listOfCables.add(new Cable(pos, vDir));
                Items tmp = board.getItemAt(i, j);
                if (tmp instanceof Cable) {
                    Cable tmp2 = (Cable) tmp;
                    if (tmp2.getNbConnection() == 1)
                        this.listOfCables.add(new Cable(pos, tmp2, vDir));
                }
                i += vDir.getDi();
            }
        }

        Direction hDir = j < brJ ? Direction.RIGHT : (j > brJ ? Direction.LEFT : null);
        if (hDir != null) {
            this.listOfCables.add(premierCable ? new Cable(sw, hDir) : new Cable(new Position(i, j), hDir));
            j += hDir.getDj();
            while (j != brJ) {
                Position pos = new Position(i, j);
                if (board.isInside(i, j) && board.getItemAt(i, j).getType() == CellType.GROUND)
                    this.listOfCables.add(new Cable(pos, hDir));
                Items tmp = board.getItemAt(i, j);
                if (tmp instanceof Cable) {
                    Cable tmp2 = (Cable) tmp;
                    if (tmp2.getNbConnection() == 1)
                        this.listOfCables.add(new Cable(pos, tmp2, hDir));
                }
                j += hDir.getDj();
            }
        }
        this.br.setCable(this.listOfCables.get(this.listOfCables.size()-1));
    }

}