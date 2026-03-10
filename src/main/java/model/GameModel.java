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
import model.physic.Position;


public class GameModel {
    private Board board;
    private List<Bridge> bridges;
    private List<Switch> switches;
    private Robot player;

    public GameModel(int width, int height) {
        //init du monde avec du vide partout (bridge bloqué)
        Matrix<Items> matrix = new Matrix<>(height, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                matrix.setItem(y, x, new Bridge(0, new Position(x, y), false, Direction.UP));
            }
        }
        this.board = new Board(matrix);
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

        Bridge bridge;
        Propulsor propulsor;
        ArrayList<Switch> switches;
        public Connection(Bridge bridge, ArrayList<Switch> switches){
            this.bridge = bridge;
            this.switches = switches;
        }
        public Connection(Propulsor propulsor, ArrayList<Switch> switches){
            this.propulsor = propulsor;
            this.switches = switches;
        }

        //vérifie que la liste en paramètre corresponde à la liste du pont/propulseur
        public boolean checkIfConnected(ArrayList<Switch> switchesToCheck) {
            if (this.switches.isEmpty()) return false;
            if (this.switches.size() != switchesToCheck.size()) return false;
            else {
                for (int i = 0 ; i < this.switches.size() ; i++) {
                    if (this.switches.get(i) == switchesToCheck.get(i)) return false;
                }
                return true;
            }
        }

        public boolean areSwitchesPressed() {
            for (int i = 0; i < this.switches.size(); i++) {
                    if (!this.switches.get(i).getIsPressed()) return false;
                }
            return true;
        }

        /**
         * @param switchesToCheck switches vérifiant si la liste appartient au pont/prolseur et si ils sont activé
         */
        public void activate(ArrayList<Switch> switchesToCheck) {
            if (checkIfConnected(switchesToCheck)) {
                if (areSwitchesPressed()) {
                    if (this.bridge != null) this.bridge.isActive();
                    else if (this.propulsor != null) this.propulsor.getIsActivate();
                }
            }
        }
    }
}
