package com.starstuff.controller;

import com.starstuff.model.GameWorld;
import com.starstuff.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles input and orchestrates the game loop.
 */
public class GameController {
    private final GameWorld model;
    private final GameView view;
    
    private final BlockingQueue<KeyCode> inputQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    // View is now created inside Controller or passed in, 
    // but usually, Main passes both. We assume Main is updated to match.
    public GameController(GameWorld model, GameView view) {
        this.model = model;
        this.view = view;
        setupInputHandlers();
    }

    private void setupInputHandlers() {
        view.getScene().setOnKeyPressed(event -> {
            inputQueue.offer(event.getCode());
        });
    }

    public void startGame() {
        // 1. Logic Thread (Simulation)
        Thread logicThread = new Thread(() -> {
            while (running) {
                processInputs();
                
                // Update world mechanics (Triggers, Bridges)
                model.updateWorldLogic();
                
                try {
                    Thread.sleep(16); // ~60 ticks per second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Game-Logic-Thread");
        logicThread.start();

        // 2. Render Loop (JavaFX Application Thread)
        AnimationTimer renderLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                view.render(model);
            }
        };
        renderLoop.start();
    }

    private void processInputs() {
        KeyCode key;
        while ((key = inputQueue.poll()) != null) {
            switch (key) {
                case W -> model.moveAgent(model.getPlayer(), 0, 1);
                case S -> model.moveAgent(model.getPlayer(), 0, -1);
                case A -> model.moveAgent(model.getPlayer(), -1, 0);
                case D -> model.moveAgent(model.getPlayer(), 1, 0);
                case SPACE -> model.interact(model.getPlayer());
                case R -> System.out.println("Reset Level");
            }
        }
    }

    public void stop() {
        running = false;
    }
}