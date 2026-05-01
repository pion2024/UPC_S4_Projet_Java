//src/main/java/model/Level.java
package model;

import model.board.Board;
import model.entity.Agent;
import model.entity.Block;
import model.entity.BlockSwitch;
import model.entity.Bridge;
import model.entity.Cable;
import model.entity.Couple;
import model.entity.Ground;
import model.physic.Direction;
import model.physic.Position;

public enum Level {
    // LEVEL_1(10, 10) {
    //     @Override
    //     public void setup(GameModel model) {
    //         Board board = model.getBoard();

    //         //on dessine les zones de terre
    //         createIsland(board, 0, 0, 4, 10); //bloc de gauche
    //         createIsland(board, 6, 0, 4, 10); //bloc de droite

    //         //on pose les mecanismes
    //         //un interrupteur rouge au depart
    //         BlockSwitch sw1 = new BlockSwitch();            
    //         board.setItem(5, 2, sw1);
    //         board.setItem(5, 2, sw1);
    //         model.addSwitch(sw1);

    //         //on cree le passage entre les deux iles
    //         Bridge b1 = new Bridge(2, new Position(5, 4), false, Direction.UP);
    //         Bridge b2 = new Bridge(3, new Position(5, 5), false, Direction.UP);
            
    //         //on lie les ponts au switch
    //         b1.addSwitch(sw1);
    //         b2.addSwitch(sw1);
            
    //         board.setItem(5, 4, b1);
    //         board.setItem(5, 5, b2);
    //         model.addBridge(b1);
    //         model.addBridge(b2);

    //         //le joueur qui pop
    //         Agent player = new Agent(new Position(0, 0));
    //         model.getBoard().getMovableEntities().add(player);
    //         model.setPlayer(player);

    //         //on peut ajouter un bloc pour tester la physique plus tard
    //         Block box = new Block(new Position(2, 2));
    //         model.getBoard().getMovableEntities().add(box);
    //     }
    // };

    LEVEL_1(10, 10) {
        @Override
        public void setup(GameModel model) {
            Board board = model.getBoard();

            //on dessine les zones de terre
            createIsland(board, 0, 0, 4, 10); //bloc de gauche
            createIsland(board, 6, 0, 4, 10); //bloc de droite

            //on pose les mecanismes
            //un interrupteur rouge au depart
            BlockSwitch sw1 = new BlockSwitch(new Position(5, 2));            
            board.setItem(5, 2, sw1);
            board.setItem(5, 2, sw1);
            model.addSwitch(sw1);

            //on cree le passage entre les deux iles
            Bridge b1 = new Bridge(2, new Position(5, 4), false, Direction.UP);
            Bridge b2 = new Bridge(3, new Position(5, 5), false, Direction.UP);
            
            //on lie les ponts au switch
            
            Couple c1 = new Couple(sw1, b1);
            Couple c2 = new Couple(sw1, b2);

            c1.connexion(sw1, b1, board);
            c2.connexion(sw1, b2, board);

            board.setItem(5, 4, b1);
            board.setItem(5, 5, b2);
            model.addBridge(b1);
            model.addBridge(b2);

            cabler(board, c1);
            cabler(board, c2);


            //le joueur qui pop
            Agent player = new Agent(new Position(0, 0));
            model.getBoard().getMovableEntities().add(player);
            model.setPlayer(player);

            //on peut ajouter un bloc pour tester la physique plus tard
            Block box = new Block(new Position(2, 2));
            model.getBoard().getMovableEntities().add(box);
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

    //remplit une zone avec du ground
    protected void createIsland(Board board, int startJ, int startI, int width, int height) {
    for (int i = startI; i < startI + height; i++) {
        for (int j = startJ; j < startJ + width; j++) {
            if (board.isInside(i, j)) {
                board.setItem(i, j, new Ground());
            }
        }
    }
}

    public void cabler(Board board, Couple couple) {
        for (Cable cable : couple.getListOfCables()) {
           board.setItem(cable.getI(), cable.getJ(), cable);
        }
    }
}