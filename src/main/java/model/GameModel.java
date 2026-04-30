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
    }

    // ======= Méthodes de gestion =======

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
        for (Couple couple : couples){
            List<Cable> listOfCables = couple.getListOfCables();
            for (Switch sw : switches) {
                listOfCables.get(0).setInput(sw.getIsPressed());
                for (int i = 1 ; i < listOfCables.size() ; i ++) {
                    Cable tmp = listOfCables.get(i);
                    Cable tmp2 = listOfCables.get(i-1);
                    if (tmp.getNbConnection() == 1) tmp.setInput(tmp2.getOutput());
                    if (tmp.getNbConnection() == 2){
                        if (board.getItemAt(tmp.getDi()+1, tmp.getDj()+1) instanceof Cable) {
                            Cable tmp3 = (Cable) board.getItemAt(tmp.getDi()+1, tmp.getDj()+1);
                            tmp.setInput2(tmp2.getOutput(), tmp3.getOutput());
                        }
                        if (board.getItemAt(tmp.getDi()-1, tmp.getDj()+1) instanceof Cable) {
                            Cable tmp3 = (Cable) board.getItemAt(tmp.getDi()+1, tmp.getDj()+1);
                            tmp.setInput2(tmp2.getOutput(), tmp3.getOutput());
                        }
                        if (board.getItemAt(tmp.getDi()+1, tmp.getDj()-1) instanceof Cable) {
                            Cable tmp3 = (Cable) board.getItemAt(tmp.getDi()+1, tmp.getDj()+1);
                            tmp.setInput2(tmp2.getOutput(), tmp3.getOutput());
                        }
                        if (board.getItemAt(tmp.getDi()-1, tmp.getDj()-1) instanceof Cable) {
                            Cable tmp3 = (Cable) board.getItemAt(tmp.getDi()+1, tmp.getDj()+1);
                            tmp.setInput2(tmp2.getOutput(), tmp3.getOutput());
                        }
                    } 
                }
            }
       }
    }
}