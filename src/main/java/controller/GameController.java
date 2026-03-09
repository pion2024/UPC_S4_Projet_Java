//src/main/java/controller/GameController.java
package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import model.GameModel;
import model.physic.Direction;
import model.physic.MovementManager;
import view.GameView;

public class GameController extends KeyAdapter {
    private GameModel model;
    private GameView view;
    private MovementManager moveMgr;
    private JFrame frame;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.moveMgr = new MovementManager(model.getBoard());

        frame = new JFrame("VP2C");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);
    }

    public void start() {
        frame.setVisible(true);
        view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (model.getPlayer() == null) return;

        boolean actionPerfomed = false;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                moveMgr.moveAgent(model.getPlayer(), Direction.UP);
                actionPerfomed = true;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                moveMgr.moveAgent(model.getPlayer(), Direction.DOWN);
                actionPerfomed = true;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                moveMgr.moveAgent(model.getPlayer(), Direction.LEFT);
                actionPerfomed = true;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                moveMgr.moveAgent(model.getPlayer(), Direction.RIGHT);
                actionPerfomed = true;
                break;
            case KeyEvent.VK_SPACE:
                // Si le joueur porte déjà quelque chose, il le pose (DROP)
                if (model.getPlayer().isCarrying()) {
                    moveMgr.dropBlock(model.getPlayer());
                } else {
                // Sinon, il essaie de ramasser (GRAB)
                    moveMgr.grabBlock(model.getPlayer());
                }
                actionPerfomed = true;
                break;
        }

        if (actionPerfomed) {
            model.update();
            view.repaint();
        }
    }
}