//src/main/java/controller/GameController.java
package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.GameModel;
import model.Level;
import model.physic.Direction;
import model.physic.MovementManager;
import view.GameView;
import view.WindowManager;

public class GameController extends KeyAdapter {
    private GameModel model;
    private GameView view;
    private WindowManager window;
    private MovementManager moveMgr;

    public GameController(WindowManager window) {
        this.window = window;
        this.view = window.getGameView();
        // On attache l'écouteur de touches au conteneur principal géré par le WindowManager
        window.getContainer().addKeyListener(this);

    }

    public void prepareLevel(int levelNum) {
        // On récupère la configuration dans l'Enum Level
        Level[] allLevels = Level.values();
    
        // Si on demande un niveau qui n'est pas encore codé dans l'Enum
        if (levelNum > allLevels.length) {
            System.out.println("Niveau non implémenté.");
            return; 
        }

        Level selectedLevel = allLevels[levelNum - 1];

        //On crée un nouveau modèle propre pour ce niveau
        this.model = new GameModel(selectedLevel.getWidth(), selectedLevel.getHeight());
        
        //On demande au Level de remplir le modèle (îles, ponts, robot...)
        selectedLevel.setup(this.model);

        //On branche la vue sur ce nouveau modèle
        this.view.setModel(model);
        
        //On initialise le moteur de mouvement pour ce nouveau plateau
        this.moveMgr = new MovementManager(model.getBoard());
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