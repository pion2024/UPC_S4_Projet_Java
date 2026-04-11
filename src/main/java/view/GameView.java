//src/main/java/view/GameView.java
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import model.GameModel;
import model.board.Board;
import model.entity.Items;
import model.entity.MovableEntity;
import model.entity.Robot;
import model.entity.Switch;

public class GameView extends JPanel {
    private GameModel model;
    private final int CELL_SIZE = 50;

    public GameView(GameModel model) {
        this.model = model;
        this.setBackground(new Color(30, 50, 90));
    
        // Protection indispensable au lancement du menu
        if (model != null && model.getBoard() != null) {
            updateViewSize();
        } else {
            // Taille par défaut pour que la fenêtre ne soit pas minuscule au début
            setPreferredSize(new Dimension(800, 600)); 
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // le fond est colorier au cas ou
        g.setColor(new Color(30, 50, 90)); 
        g.fillRect(0, 0, getWidth(), getHeight());
        //Si le modèle est null, on s'arrête là
        if (model == null || model.getBoard() == null) {
            return; 
        }
        Board board = model.getBoard();
        int cols = board.getNbColumns();
        int rows = board.getNbLines();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Items item = board.getItemAt(i, j);
                drawFixedItem(g, item, j, i);

                MovableEntity mobile = board.getEntityAt(i, j);
                if (mobile != null) {
                    drawMovableEntity(g, mobile, j, i);
                }
                
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    // permet de changer le model selon le niveau choisi 
    public void setModel(GameModel model) {
        this.model = model;
        if (model != null) {
            updateViewSize();
            this.revalidate(); // Informe Swing que la taille a changé
        }
    }

    private void updateViewSize() {
    // Sécurité supplémentaire
    if (model == null || model.getBoard() == null) return;

    int cols = model.getBoard().getNbColumns();
    int rows = model.getBoard().getNbLines();
    
    // On définit la taille physique du composant
    Dimension size = new Dimension(cols * CELL_SIZE, rows * CELL_SIZE);
    this.setPreferredSize(size);
    this.setMinimumSize(size);
}

    // private void drawFixedItem(Graphics g, Items item, int j, int i) {
    //     switch (item.getType()) {
    //         case GROUND:
    //             g.setColor(new Color(220, 220, 220));
    //             break;
    //         case SWITCH:
    //             Switch sw = (Switch) item;
    //             g.setColor(sw.getIsPressed() ? Color.GREEN : Color.RED);
    //             break;
    //         case BRIDGE:
    //             g.setColor(item.isTraversable() ? new Color(139, 69, 19) : Color.DARK_GRAY);
    //             break;
    //         default:
    //             g.setColor(Color.WHITE);
    //             break;
    //     }
    //     g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    // }

     private void drawFixedItem(Graphics g, Items item, int col, int row) {
        BufferedImage img = null;
        Color fallback;

        switch (item.getType()) {
            case GROUND:
                img = AssetManager.getInstance().getGround();
                fallback = new Color(220, 220, 220);
                break;
            case SWITCH:
                Switch sw = (Switch) item;
                img = AssetManager.getInstance().getSwitch();
                fallback = sw.getIsPressed() ? Color.GREEN : Color.RED;
                break;
            case BRIDGE:
                img = item.isTraversable() ? AssetManager.getInstance().getOpenBridge() : AssetManager.getInstance().getClosedBridge();
                fallback = item.isTraversable() ? new Color(139, 69, 19) : Color.DARK_GRAY;
                break;
            default:
                fallback = Color.WHITE;
                break;
        }

        drawCell(g, img, col, row, fallback);
    }

    private void drawCell(Graphics g, BufferedImage img, int col, int row, Color fallback) {
    if (img != null) {
        g.drawImage(img, col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
    } else {
        g.setColor(fallback);
        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}

    // private void drawMovableEntity(Graphics g, MovableEntity mobile, int j, int i) {
    //     if (mobile instanceof Robot) {
    //         g.setColor(Color.BLUE);
    //     } else {
    //         g.setColor(Color.MAGENTA);
    //     }
    //     int padding = 10; 
    //     g.fillRect(j * CELL_SIZE + padding, i * CELL_SIZE + padding, 
    //                CELL_SIZE - 2 * padding, CELL_SIZE - 2 * padding);
    // }

    private void drawMovableEntity(Graphics g, MovableEntity mobile, int col, int row) {
        BufferedImage img;

        if (mobile instanceof Robot) {
            img = AssetManager.getInstance().getRobot();
        } else {
            img = AssetManager.getInstance().getPlayer();
        }

        drawCell(g, img, col, row, Color.BLUE);
    }
}