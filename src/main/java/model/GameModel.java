//src/main/java/model/GameModel.java
package model;

import java.util.ArrayList;
import java.util.List;
import model.board.Board;
import model.entity.Bridge;
import model.entity.Robot;

public class GameModel {
    private Board board;
    private List<Bridge> bridges;
    private Robot player; 

    public GameModel(Board board) {
        this.board = board;
        this.bridges = new ArrayList<>();
    }

    public void addBridge(Bridge b) { this.bridges.add(b); }
    public void setPlayer(Robot player) { this.player = player; }

    public Board getBoard() { return this.board; }
    public Robot getPlayer() { return this.player; }

    public void update() {
        for (Bridge bridge : bridges) {
            bridge.updateStatus(); 
        }
    }
}