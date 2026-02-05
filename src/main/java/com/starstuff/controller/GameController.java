package com.starstuff.controller;

import com.starstuff.common.Vector2;
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
        
        // Pass frame to terminal for packing
        terminalView.setParentFrame(frame);

        initInputs();
        startLoop();
    }

    private void initInputs() {
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // If Terminal is focused/active, Escape to close
                if (terminalView.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        terminalView.setVisible(false);
                    }
                    // Prevent movement keys if typing in text fields (none yet, but good practice)
                    // For now, allow movement even if terminal open? Or freeze?
                    // Typically freeze player.
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
        // 1. Check Terminal Open
        if (world.getTerminal() != null) {
            Vector2 playerPos = world.getPlayer().getPosition();
            Vector2 termPos = world.getTerminal().getAccessPosition();
            
            if (playerPos.equals(termPos)) {
                if (!terminalView.isVisible()) {
                    terminalView.setVisible(true);
                }
                return;
            }
        }

        // 2. Normal Interaction
        world.interact(world.getPlayer());
    }

    private void startLoop() {
        // Fast tick (50ms) for smooth UI/Auto-hide checks
        loop = new Timer(50, new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                world.update(); // Robot logic inside handles its own delay
                
                checkAutoCloseTerminal();
                
                gameView.repaint();
            }
        });
        loop.start();
    }
    
    private void checkAutoCloseTerminal() {
        if (terminalView.isVisible() && world.getTerminal() != null) {
            Vector2 playerPos = world.getPlayer().getPosition();
            Vector2 termAccess = world.getTerminal().getAccessPosition();
            
            // Distance check (Manhattan)
            int dist = Math.abs(playerPos.x - termAccess.x) + Math.abs(playerPos.y - termAccess.y);
            
            // Allow standing ON access point (dist 0) or adjacent (dist 1) to keep open?
            // Usually strict: Must stand ON access point.
            if (dist > 1) { // Allow 1 tile tolerance
                terminalView.setVisible(false);
            }
        }
    }
}