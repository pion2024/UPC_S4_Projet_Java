package model;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.entity.Agent;
import model.entity.Bridge;
import model.entity.Items;
import model.entity.Switch;
import model.entity.Wall;
import model.physic.Position;

public class GameModel {

    private Board board;
    private List<Bridge> bridges;
    private List<Switch> switches;
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

    public void update() {
        // Les switches sont gérés par onEnter / onExit / onInteract, donc pas besoin de update ici
        // On update l'état des ponts
        for (Bridge bridge : bridges) {
            bridge.updateStatus();
        }
    }
}