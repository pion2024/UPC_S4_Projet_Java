//src/main/java/view/GameView.java
package view;

import model.GameModel;
import model.board.Board;
import model.entity.*;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class GameView extends JPanel {
    private GameModel model;
    private final int CELL_SIZE = 50;

    public GameView(GameModel model) {
        this.model = model;
        int cols = model.getBoard().getItems().getNbColumns();
        int rows = model.getBoard().getItems().getNbLines();
        setPreferredSize(new Dimension(cols * CELL_SIZE, rows * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Board board = model.getBoard();
        int cols = board.getItems().getNbColumns();
        int rows = board.getItems().getNbLines();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Items item = board.getItems().getItem(y, x);
                drawFixedItem(g, item, x, y);

                MovableEntity mobile = board.getEntityAt(x, y);
                if (mobile != null) {
                    drawMovableEntity(g, mobile, x, y);
                }
                
                g.setColor(Color.BLACK);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawFixedItem(Graphics g, Items item, int x, int y) {
        switch (item.getType()) {
            case GROUND:
                g.setColor(new Color(220, 220, 220));
                break;
            case SWITCH:
                Switch sw = (Switch) item;
                g.setColor(sw.getIsPressed() ? Color.GREEN : Color.RED);
                break;
            case BRIDGE:
                g.setColor(item.isTraversable() ? new Color(139, 69, 19) : Color.DARK_GRAY);
                break;
            default:
                g.setColor(Color.WHITE);
                break;
        }
        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawMovableEntity(Graphics g, MovableEntity mobile, int x, int y) {
        if (mobile instanceof Robot) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.MAGENTA);
        }
        int padding = 10; 
        g.fillRect(x * CELL_SIZE + padding, y * CELL_SIZE + padding, 
                   CELL_SIZE - 2 * padding, CELL_SIZE - 2 * padding);
    }
}