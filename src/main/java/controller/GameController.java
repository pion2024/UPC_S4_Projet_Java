// src/main/java/controller/GameController.java
package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;     

import javax.swing.Timer;

import model.GameModel;
import model.Level;
import model.board.Board;
import model.entity.ArrivalSwitch;
import model.entity.Block;
import model.entity.InteractionSwitch;
import model.entity.Items;
import model.entity.MovableEntity;
import model.entity.Robot;
import model.entity.Switch;
import model.entity.Terminal;
import model.physic.Direction;
import model.physic.MovementManager;
import model.physic.Position;
import view.CommandPanel;
import view.GameView;
import view.WindowManager;

public class GameController extends KeyAdapter {
    private GameModel model;
    private GameView view;
    private WindowManager window;
    private MovementManager moveMgr;

    private Timer robotTimer; 

    public GameController(WindowManager window) {
        this.window = window;
        this.view = window.getGameView();
        window.getContainer().addKeyListener(this);
    }

    public void prepareLevel(int levelNum) {
        Level[] allLevels = Level.values();
        if (levelNum > allLevels.length) {
            System.out.println("Niveau non implémenté.");
            return; 
        }

        Level selectedLevel = allLevels[levelNum - 1];
        this.model = new GameModel(selectedLevel.getWidth(), selectedLevel.getHeight(),levelNum);
        selectedLevel.setup(this.model);
        this.view.setModel(model);
        this.moveMgr = new MovementManager(model.getBoard());

        if (robotTimer != null) {
            robotTimer.stop(); 
        }
        
        robotTimer = new Timer(400, e -> {
            boolean robotMoved = false;
            
            // On crée une copie de la liste des entités mobiles car quand le robot fait "Pick Up" (remove) ou "Drop" (add), 
            // il modifie la liste originale. Si on itère directement sur l'originale, 
            // Java lance une "ConcurrentModificationException".
            List<MovableEntity> entitiesCopy = new ArrayList<>(model.getBoard().getMovableEntities());
            
            for (MovableEntity entity : entitiesCopy) {
                if (entity instanceof Robot) {
                    Robot r = (Robot) entity;
                    if (r.isExecuting()) {
                        r.executeStep(moveMgr, model.getBoard());
                        robotMoved = true;
                    }
                }
            }
            
            if (robotMoved) {
                model.update();
                view.repaint();
            }
        });
        robotTimer.start(); 
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
                Position p = model.getPlayer().getPos();
                Direction d = model.getPlayer().getFacing();

                int i = p.getI() + d.getDi();
                int j = p.getJ() + d.getDj();
                Board board = model.getBoard();

                if (board.isInside(i, j)) {
                    Items item = board.getItemAt(i, j);

                    // interaction avec le terminal 
                    if (item instanceof Terminal && d == Direction.RIGHT) {
                        Terminal terminal = (Terminal) item;

                        Robot targetRobot = null;
                        for (MovableEntity me : board.getMovableEntities()) {
                            if (me instanceof Robot) {
                                targetRobot = (Robot) me;
                                break;
                            }
                        }

                        CommandPanel cmdPanel = new CommandPanel(model, terminal, targetRobot, () -> {
                            window.hideCommandPanel();
                        });
                        window.showCommandPanel(cmdPanel);

                        return; 
                    }

                    // 2. NOUVEAU : Interaction avec ArrivalBlock ou InteractionBlock
                    if (item instanceof ArrivalSwitch) {
                        ((ArrivalSwitch) item).onInteract(model.getPlayer());
                        window.showLevelCompletionDialog(model.getLveleNum());
                        actionPerfomed = true;
                    } 
                    else if (item instanceof InteractionSwitch) {
                        ((InteractionSwitch) item).onInteract(model.getPlayer());
                        actionPerfomed = true;
                    }
                    else {
                        if (model.getPlayer().isCarrying()) {
                        moveMgr.dropBlock(model.getPlayer());
                        actionPerfomed = true;
                    } else {
                        if (board.getEntityAt(i, j) instanceof Block) {
                            moveMgr.grabBlock(model.getPlayer());
                            actionPerfomed = true;
                        } else {
                            if (item instanceof Switch sw) {
                                sw.onInteract(model.getPlayer());
                                actionPerfomed = true;
                            }
                        }
                    }
                    } 
                }
                break;
            case KeyEvent.VK_Q:
                p = model.getPlayer().getPos();
                d = model.getPlayer().getFacing();

                i = p.getI() + d.getDi();
                j = p.getJ() + d.getDj();
                board = model.getBoard();

                if (board.isInside(i, j)) {
                    Items item = board.getItemAt(i, j);
                    // interaction avec le terminal 
                    if (item instanceof InteractionSwitch && d == Direction.UP) {
                        InteractionSwitch sw = (InteractionSwitch) item;
                        sw.onInteract(model.getPlayer());
                        actionPerfomed = true;  
                    }     
                }
                break;
        }

        if (actionPerfomed) {
            model.update();
            view.repaint();
        }
    }
}