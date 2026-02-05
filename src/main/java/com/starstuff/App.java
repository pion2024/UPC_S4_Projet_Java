package com.starstuff;

import com.starstuff.controller.GameController;
import com.starstuff.model.GameWorld;
import com.starstuff.model.Level;
import com.starstuff.view.GamePanel;
import com.starstuff.view.TerminalPanel;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Star Stuff 2D Swing Clone");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // 1. Model - Initialize with LEVEL_1
            GameWorld world = new GameWorld(Level.LEVEL_TEST);

            // 2. View
            GamePanel gamePanel = new GamePanel(world);
            TerminalPanel terminalPanel = new TerminalPanel(world);
            
            // Start Terminal hidden
            terminalPanel.setVisible(false);

            frame.add(gamePanel, BorderLayout.CENTER);
            frame.add(terminalPanel, BorderLayout.EAST);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setFocusable(true); 
            frame.requestFocus();

            // 3. Controller
            new GameController(world, gamePanel, terminalPanel, frame);
        });
    }
}