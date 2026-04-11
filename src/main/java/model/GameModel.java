package model;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.entity.Bridge;
import model.entity.Items;
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
        // init du monde avec du vide partout (bridges bloqués)
        Items[][] items = new Items[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                items[i][j] = new Bridge(0, new Position(i, j), false, Direction.UP);
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

    public void setPlayer(Robot player) {
        this.player = player;
        // ajouter le player aux entités mobiles du board
        this.board.getMovableEntities().add(player);
    }

    public Board getBoard() {
        return this.board;
    }

    public Robot getPlayer() {
        return this.player;
    }

    // ======= Boucle de mise à jour =======

    public void update() {
        // Les switches sont gérés par onEnter / onExit / onInteract => pas besoin de update ici

        // On update l'état des ponts
        for (Bridge bridge : bridges) {
            bridge.updateStatus();
        }
    }
}