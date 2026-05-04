package model;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.entity.Agent;
import model.entity.Bridge;
import model.entity.Cable;
import model.entity.Couple;
import model.entity.Items;
import model.entity.Propulsor;
import model.entity.Switch;
import model.entity.Wall;
import model.physic.Direction;
import model.physic.Position;

public class GameModel {

    private Board board;
    private List<Bridge> bridges;
    private List<Switch> switches;
    private List<Propulsor> propulsors;
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
        this.bridges = new ArrayList<>();
        this.switches = new ArrayList<>();
        this.couples = new ArrayList<>();
    }

    public void addBridge(Bridge b) {
        this.bridges.add(b);
    }

    public void addSwitch(Switch s) {
        this.switches.add(s);
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

    // ======= Boucle de mise à jour =======

    //le coeur de la boucle de jeu
    // public void update() {
    //     // Les switches sont gérés par onEnter / onExit / onInteract => pas besoin de update ici
        

    //     // On update l'état des ponts
    //     for (Bridge bridge : bridges) {
    //         // if (checkIfConnected(bridge, switches)) {
    //             bridge.updateStatus();
    //         // }
    //     }

    //     for (Propulsor propulsor : propulsors) {
    //         // if (checkIfConnected(propulsor, switches)) {
    //             propulsor.updateStatus();
    //         // }
    //     }
    // }


    public void update() {
        for (Couple couple : couples) {
            List<Cable> listOfCables = couple.getListOfCables();

            listOfCables.get(0).setInput(couple.getSwitch().getIsPressed());

            for (int i = 1; i < listOfCables.size(); i++) {
                Cable tmp  = listOfCables.get(i);
                Cable tmp2 = listOfCables.get(i - 1);

                if (tmp.getNbConnection() == 1) tmp.setInput(tmp2.getOutput());
                else if (tmp.getNbConnection() == 2) {
                    for (Direction dir : Direction.values()) {
                        int line = tmp.getI() + dir.getDi();
                        int column = tmp.getJ() + dir.getDj();
                        if (board.isInside(line, column) && board.getItemAt(line, column) instanceof Cable) {
                            Cable neighbor = (Cable) board.getItemAt(line, column);
                            if (neighbor != tmp2) {
                                tmp.setInput2(tmp2.getOutput(), neighbor.getOutput());
                                break;
                            }
                        }
                    }
                }
            }
            couple.getBridge().updateStatus();
        }
    }
}