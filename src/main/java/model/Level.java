//src/main/java/model/Level.java
package model;

import model.board.Matrix;
import model.entity.Block;
import model.entity.Bridge;
import model.entity.Ground;
import model.entity.Items;
import model.entity.Robot;
import model.entity.Switch;
import model.physic.Direction;
import model.physic.Position;

public enum Level {
    LEVEL_1(10, 10) {
        @Override
        public void setup(GameModel model) {
            Matrix<Items> matrix = model.getBoard().getItems();

            //on dessine les zones de terre
            createIsland(matrix, 0, 0, 4, 10); //bloc de gauche
            createIsland(matrix, 6, 0, 4, 10); //bloc de droite

            //on pose les mecanismes
            //un interrupteur rouge au depart
            Switch sw1 = new Switch(new Position(5,2),true, null, Direction.UP);
            matrix.setItem(5, 2, sw1);
            model.addSwitch(sw1);

            //on cree le passage entre les deux iles
            Bridge b1 = new Bridge(false, Direction.UP);
            Bridge b2 = new Bridge(false, Direction.UP);
            
            //on lie les ponts au switch
            b1.addSwitch(sw1);
            b2.addSwitch(sw1);
            
            matrix.setItem(5, 4, b1);
            matrix.setItem(5, 5, b2);
            model.addBridge(b1);
            model.addBridge(b2);

            //le robot qui pop
            Robot player = new Robot(new Position(0, 0));
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
    protected void createIsland(Matrix<Items> matrix, int startX, int startY, int width, int height) {
        for (int y = startY; y < startY + height; y++) {
            for (int x = startX; x < startX + width; x++) {
                if (matrix.isInside(y, x)) {
                    matrix.setItem(y, x, new Ground());
                }
            }
        }
    }
}