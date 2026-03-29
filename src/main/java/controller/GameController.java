//src/main/java/controller/GameController.java
package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.GameModel;
import model.Level;
import model.board.Board;
import model.entity.Block;
import model.entity.Items;
import model.entity.Switch;
import model.physic.Direction;
import model.physic.MovementManager;
import model.physic.Position;
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
                model.getPlayer().setFacing(Direction.UP);
                actionPerfomed = true;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                moveMgr.moveAgent(model.getPlayer(), Direction.DOWN);
                model.getPlayer().setFacing(Direction.DOWN);
                actionPerfomed = true;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                moveMgr.moveAgent(model.getPlayer(), Direction.LEFT);
                model.getPlayer().setFacing(Direction.LEFT);
                actionPerfomed = true;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                moveMgr.moveAgent(model.getPlayer(), Direction.RIGHT);
                model.getPlayer().setFacing(Direction.RIGHT); 
                actionPerfomed = true;
                break;
            case KeyEvent.VK_SPACE:
                if (model.getPlayer().isCarrying()) {
                    moveMgr.dropBlock(model.getPlayer());
                } else {

                    // on essaie TOUJOURS de grab d'abord
                    Position p = model.getPlayer().getPos();
                    Direction d = model.getPlayer().getFacing();

                    int i = p.getI() + d.getDi();
                    int j = p.getJ() + d.getDj();

                    Board board = model.getBoard();

                    if (board.getElement(i,j).isInside(i, j)) {

                        // PRIORITÉ AU BLOC
                        if (board.getEntityAt(i, j) instanceof Block) {
                        moveMgr.grabBlock(model.getPlayer());
                        } else {
                            // sinon interaction avec switch
                            Items item = board.getElement(i, j);
                            if (item instanceof Switch sw) {
                                sw.onInteract(model.getPlayer());
                            }
                        }
                    }
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