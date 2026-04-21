package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameController;

public class WindowManager {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel container;
    
    // nouveaux panels pour gérer l'affichage fractionné (Jeu + Panneau de commande)
    private JPanel gameMainPanel; 
    private JPanel commandPanelContainer;
    
    private MenuView menuView;
    private GameView gameView;
    private GameController controller;

    public WindowManager() {
        frame = new JFrame("Star Stuff");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        
        // Vue du jeu
        gameView = new GameView(null);
        JPanel gameCenteringPanel = new JPanel(new GridBagLayout());
        gameCenteringPanel.setBackground(new Color(10, 15, 40)); 
        gameCenteringPanel.add(gameView);
        
        // plateau de jeu : Jeu au centre, Commandes à droite
        gameMainPanel = new JPanel(new BorderLayout());
        gameMainPanel.add(gameCenteringPanel, BorderLayout.CENTER);
        
        commandPanelContainer = new JPanel(new BorderLayout());
        commandPanelContainer.setBackground(new Color(10, 15, 40));
        gameMainPanel.add(commandPanelContainer, BorderLayout.EAST);
        
        // Menu
        menuView = new MenuView(levelNum -> switchToGame(levelNum));
        
        container.add(menuView, "MENU");
        container.add(gameMainPanel, "GAME");
        
        frame.add(container);
    }

    public void init() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        showMenu();
    }

    public void switchToGame(int levelNum) {
        hideCommandPanel(); 
        // cacher le menu de commande est caché au lancement
        if (controller != null) {
            controller.prepareLevel(levelNum);
        }
        
        cardLayout.show(container, "GAME");
        frame.pack(); 
        frame.setLocationRelativeTo(null); 
        container.requestFocusInWindow(); 
    }

    public void showMenu() {
        cardLayout.show(container, "MENU");
    }

    // Méthodes pour afficher/cacher le panneau de commandes ---
    public void showCommandPanel(JPanel panel) {
        commandPanelContainer.removeAll();
        commandPanelContainer.add(panel, BorderLayout.CENTER);
        commandPanelContainer.revalidate();
        commandPanelContainer.repaint();
        frame.pack(); // Ajuste la taille de la fenêtre pour s'adapter au menu
    }
    
    public void hideCommandPanel() {
        commandPanelContainer.removeAll();
        commandPanelContainer.revalidate();
        commandPanelContainer.repaint();
        frame.pack();
        container.requestFocusInWindow(); // Rend le focus au jeu pour pouvoir re-bouger
    }

    // Getters / Setters
    public GameView getGameView() { return gameView; }
    public JPanel getContainer() { return container; }
    public void setController(GameController controller) { this.controller = controller; }
}