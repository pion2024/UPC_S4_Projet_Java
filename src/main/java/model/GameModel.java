//src/main/java/model/GameModel.java
package model;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.board.Matrix;
import model.entity.Bridge;
import model.entity.Items;
import model.entity.MovableEntity;
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
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix.setElement(i, j, new Bridge(0, new Position(i, j), false, Direction.UP));
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
}