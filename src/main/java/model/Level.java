package model;

import model.board.Board;
import model.entity.Agent;
import model.entity.Block;
import model.entity.BlockSwitch;
import model.entity.Bridge;
import model.entity.Ground;
import model.entity.Robot;      
import model.entity.Terminal;   
import model.physic.Direction;
import model.physic.Position;

public enum Level {
    LEVEL_1(10, 10) {
        @Override
        public void setup(GameModel model) {
            Board board = model.getBoard();

            // Création des îles de base
            createIsland(board, 0, 0, 4, 10); // bloc de gauche
            createIsland(board, 6, 0, 4, 10); // bloc de droite

            Terminal terminal = new Terminal();
            // Le joueur commence en (0,0), le terminal est placé en (0,1)
            board.setItem(0, 1, terminal); 

            Robot robot = new Robot(new Position(3, 3));
            model.getBoard().getMovableEntities().add(robot);

            // Switch
            BlockSwitch sw1 = new BlockSwitch();
            board.setItem(5, 2, sw1);
            model.addSwitch(sw1);

            // Joueur
            Agent player = new Agent(new Position(0, 0));
            model.getBoard().getMovableEntities().add(player);
            model.setPlayer(player);

            // Bloc
            Block box = new Block(new Position(2, 2));
            model.getBoard().getMovableEntities().add(box);

            // Ponts
            Bridge b1 = new Bridge(2, new Position(5, 4), false, Direction.UP);
            Bridge b2 = new Bridge(3, new Position(5, 5), false, Direction.UP);
            b1.addSwitch(sw1);
            b2.addSwitch(sw1);
            board.setItem(5, 4, b1);
            board.setItem(5, 5, b2);
            model.addBridge(b1);
            model.addBridge(b2);

        }
    };

    private final int width;
    private final int height;

    Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public abstract void setup(GameModel model);

    protected void createIsland(Board board, int startJ, int startI, int width, int height) {
        for (int i = startI; i < startI + height; i++) {
            for (int j = startJ; j < startJ + width; j++) {
                if (board.isInside(i, j)) {
                    board.setItem(i, j, new Ground());
                }
            }
        }
    }
}