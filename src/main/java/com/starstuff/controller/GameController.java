// package com.starstuff.controller;

// import com.starstuff.model.GameWorld;
// import com.starstuff.view.GameView;
// import javafx.animation.AnimationTimer;
// import javafx.scene.input.KeyCode;

// import java.util.concurrent.BlockingQueue;
// import java.util.concurrent.LinkedBlockingQueue;

// /**
//  * Handles input and orchestrates the game loop.
//  */
// public class GameController {
//     private final GameWorld model;
//     private final GameView view;
    
//     private final BlockingQueue<KeyCode> inputQueue = new LinkedBlockingQueue<>();
//     private volatile boolean running = true;

//     // View is now created inside Controller or passed in, 
//     // but usually, Main passes both. We assume Main is updated to match.
//     public GameController(GameWorld model, GameView view) {
//         this.model = model;
//         this.view = view;
//         setupInputHandlers();
//     }

//     private void setupInputHandlers() {
//         view.getScene().setOnKeyPressed(event -> {
//             inputQueue.offer(event.getCode());
//         });
//     }

//     public void startGame() {
//         // 1. Logic Thread (Simulation)
//         Thread logicThread = new Thread(() -> {
//             while (running) {
//                 processInputs();
                
//                 // Update world mechanics (Triggers, Bridges)
//                 model.updateWorldLogic();
                
//                 try {
//                     Thread.sleep(16); // ~60 ticks per second
//                 } catch (InterruptedException e) {
//                     Thread.currentThread().interrupt();
//                 }
//             }
//         }, "Game-Logic-Thread");
//         logicThread.start();

//         // 2. Render Loop (JavaFX Application Thread)
//         AnimationTimer renderLoop = new AnimationTimer() {
//             @Override
//             public void handle(long now) {
//                 view.render(model);
//             }
//         };
//         renderLoop.start();
//     }

//     private void processInputs() {
//         KeyCode key;
//         while ((key = inputQueue.poll()) != null) {
//             switch (key) {
//                 case W -> model.moveAgent(model.getPlayer(), 0, 1);
//                 case S -> model.moveAgent(model.getPlayer(), 0, -1);
//                 case A -> model.moveAgent(model.getPlayer(), -1, 0);
//                 case D -> model.moveAgent(model.getPlayer(), 1, 0);
//                 case SPACE -> model.interact(model.getPlayer());
//                 case R -> System.out.println("Reset Level");
//             }
//         }
//     }

//     public void stop() {
//         running = false;
//     }
// }


package com.starstuff.controller;

import com.starstuff.model.GameWorld;
import com.starstuff.view.GamePanel;
import com.starstuff.view.TerminalPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private GameWorld world;
    private GamePanel gameView;
    private TerminalPanel terminalView;
    private JFrame mainFrame;
    private Timer loop;

    public GameController(GameWorld world, GamePanel gameView, TerminalPanel terminalView, JFrame frame) {
        this.world = world;
        this.gameView = gameView;
        this.terminalView = terminalView;
        this.mainFrame = frame;

        initInputs();
        startLoop();
    }

    private void initInputs() {
        // We add KeyListener to the Frame to capture WASD
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // If Terminal is open, disable game movement
                if (terminalView.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        terminalView.setVisible(false);
                        mainFrame.pack();
                    }
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: world.moveAgent(world.getPlayer(), 0, -1); break;
                    case KeyEvent.VK_S: world.moveAgent(world.getPlayer(), 0, 1); break;
                    case KeyEvent.VK_A: world.moveAgent(world.getPlayer(), -1, 0); break;
                    case KeyEvent.VK_D: world.moveAgent(world.getPlayer(), 1, 0); break;
                    case KeyEvent.VK_SPACE: handleInteraction(); break;
                }
                gameView.repaint();
            }
        });
    }

    private void handleInteraction() {
        // Check Terminal entry
        if (world.getPlayer().getPosition().equals(world.getTerminal().getAccessPosition())) {
            // Open Terminal UI
            terminalView.setVisible(true);
            mainFrame.pack(); // Adjust size to fit side panel
            return;
        }

        // Normal interaction (Pick/Drop)
        world.interact(world.getPlayer());
    }

    private void startLoop() {
        // Game Tick (e.g., for Robot movement)
        loop = new Timer(200, new ActionListener() { // 200ms delay
            @Override
            public void actionPerformed(ActionEvent e) {
                world.update();
                gameView.repaint();
            }
        });
        loop.start();
    }
}