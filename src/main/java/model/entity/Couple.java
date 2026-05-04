package model.entity;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.entity.Cable.CableSource;
import model.entity.Cable.CableTarget;
import model.physic.Direction;
import model.physic.Position;

public class Couple {
    private List<Cable> listOfCables;
    private CableSource source;     //switch, cable et intersection
    private CableTarget target;     //intersection, cable, pont et propulseur

    public Couple(CableSource source, CableTarget target) {
        this.source = source;
        this.target = target;
        this.listOfCables = new ArrayList<>();
    }


    public List<Cable> getListOfCables() {
        return this.listOfCables;
    }

    public CableSource getSource() {
        return this.source;
    }

    public CableTarget getTarget() {
        return this.target;
    }

    public Cable getLastCable() {
        return this.listOfCables.get(listOfCables.size()-1);
    }

    //on crée la connexion entre un pont et un bridge, et on a ajoute le cablage sur le board
    public void connexion(Board board) {
        int i = this.source.getI();
        int j = this.source.getJ();
        int targetI = this.target.getI();
        int targetJ = this.target.getJ();
        boolean premierCable = true;

        Direction vDir = i < targetI ? Direction.DOWN : (i > targetI ? Direction.UP : null);
        if (vDir != null) {
            this.listOfCables.add(new Cable(source, vDir));
            premierCable = false;
            i += vDir.getDi();
            while (i != targetI) {
                Position pos = new Position(i, j);
                if (board.isInside(i, j) && board.getItemAt(i, j).getType() == CellType.GROUND)
                    this.listOfCables.add(new Cable(pos, vDir));
                i += vDir.getDi();
            }
        }

        Direction hDir = j < targetJ ? Direction.RIGHT : (j > targetJ ? Direction.LEFT : null);
        if (hDir != null) {
            this.listOfCables.add(premierCable ? new Cable(source, hDir) : new Cable(new Position(i, j), hDir));
            j += hDir.getDj();
            while (j != targetJ) {
                Position pos = new Position(i, j);
                if (board.isInside(i, j) && board.getItemAt(i, j).getType() == CellType.GROUND)
                    this.listOfCables.add(new Cable(pos, hDir));
                j += hDir.getDj();
            }
        }
        Direction tmp2 = (vDir != null && hDir == null) ? vDir : hDir;
        this.listOfCables.add(new Cable(new Position(targetI, targetJ), tmp2));
        this.target.addCable(this.listOfCables.get(this.listOfCables.size()-1));
    }
}