package model;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.entity.Agent;
import model.entity.Cable;
import model.entity.Cable.Intersection;
import model.entity.Couple;
import model.entity.Items;
import model.entity.Switch;
import model.entity.Wall;
import model.physic.Position;

public class GameModel {

    private Board board;
    private List<Couple> couples;
    private Agent player;

    public GameModel(int width, int height) {
        // init du monde avec du vide partout (bridges bloqués)
        Items[][] items = new Items[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                items[i][j] = new Wall(new Position(i, j));
            }
        }

        this.board = new Board(items);
        this.couples = new ArrayList<>();
    }


    public void setPlayer(Agent player) {
        this.player = player;
        // ajouter le player aux entités mobiles du board
        this.board.getMovableEntities().add(player);
    }

    public Board getBoard() {
        return this.board;
    }

    public Agent getPlayer() {
        return this.player;
    }

    public void addCouple(Couple c) {
        this.couples.add(c);
    }

    // ======= Boucle de mise à jour =======

    public void update() {
        for (Couple couple : couples) {
            List<Cable> listOfCables = couple.getListOfCables();

            if (couple.getSource() instanceof Switch)
                listOfCables.get(0).setInput(couple.getSource().getIsPressed());
            else if (couple.getSource() instanceof Intersection){
                Intersection inter = (Intersection) couple.getSource();
                inter.updateStatus();
                listOfCables.get(0).setInput(inter.getIsPressed());;
            }
                

            for (int i = 1; i < listOfCables.size(); i++) {
                Cable tmp  = listOfCables.get(i);
                Cable tmp2 = listOfCables.get(i - 1);

                tmp.setInput(tmp2.getOutput());
            }
            couple.getTarget().updateStatus();
        }
    }
}