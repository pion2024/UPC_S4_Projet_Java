//src/main/java/model/GameModel.java
package model;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.board.Matrix;
import model.entity.Bridge;
import model.entity.Items;
import model.entity.MovableEntity;
import model.entity.Propulsor;
import model.entity.Robot;
import model.entity.Switch;
import model.physic.Direction;


public class GameModel {
    private Board board;
    private List<Bridge> bridges;
    private List<Propulsor> propulsors;
    private List<Switch> switches;
    private Robot player;

    public GameModel(int width, int height) {
        //init du monde avec du vide partout (bridge bloqué)
        // Matrix<Items> matrix = new Matrix<>(height, width);
        // for (int y = 0; y < height; y++) {
        //     for (int x = 0; x < width; x++) {
        //         matrix.setItem(y, x, new Bridge(false, Direction.UP));
        //     }
        // }
        //this.board = new Board(matrix);
        this.board.init();
        this.bridges = new ArrayList<>();
        this.switches = new ArrayList<>();
    }

    public void addBridge(Bridge b) { this.bridges.add(b); }
    public void addSwitch(Switch s) { this.switches.add(s); }
    public void setPlayer(Robot player) { this.player = player; }
    public Board getBoard() { return this.board; }
    public Robot getPlayer() { return this.player; }

    //le coeur de la boucle de jeu
    public void update() {
        //on check qui marche sur quoi
        for (Switch sw : switches) {
            boolean isPressed = false;
            for (MovableEntity entity : board.getMovableEntities()) {
                if (entity.getPos().equals(sw.getPos())) {
                    isPressed = true;
                    break;
                }
            }
            sw.updateStatus(isPressed);
        }
        //on update l'etat des ponts
        for (Bridge bridge : bridges) {
            bridge.updateStatus();
        }
    }


    public class Connection {

        Bridge bridge2;
        Propulsor propulsor2;
        ArrayList<Switch> switches2;
        public Connection(Bridge bridge, ArrayList<Switch> switches){
            this.bridge2 = bridge;
            this.switches2 = switches;
        }
        public Connection(Propulsor propulsor, ArrayList<Switch> switches){
            this.propulsor2 = propulsor;
            this.switches2 = switches;
        }

        //vérifie que la liste en paramètre corresponde à la liste du pont/propulseur
        public boolean checkIfConnected(ArrayList<Switch> switchesToCheck) {
            if (this.switches2.isEmpty()) return false;
            if (this.switches2.size() != switchesToCheck.size()) return false;
            else {
                for (int i = 0 ; i < this.switches2.size() ; i++) {
                    if (this.switches2.get(i) == switchesToCheck.get(i)) return false;
                }
                return true;
            }
        }

        public boolean areSwitchesPressed() {
            for (int i = 0; i < this.switches2.size(); i++) {
                    if (!this.switches2.get(i).getIsPressed()) return false;
                }
            return true;
        }

        /**
         * @param switchesToCheck switches vérifiant si la liste appartient au pont/prolseur et si ils sont activé
         */
        public void activate(ArrayList<Switch> switchesToCheck) {
            if (checkIfConnected(switchesToCheck)) {
                if (areSwitchesPressed()) {
                    if (this.bridge2 != null) this.bridge2.isActive();
                    else if (this.propulsor2 != null) this.propulsor2.getIsActivate();
                }
            }
        }
    }
}
