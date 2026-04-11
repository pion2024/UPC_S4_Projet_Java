package view;

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
    
    private MenuView menuView;
    private GameView gameView;
    private GameController controller;

    public WindowManager() {
        frame = new JFrame("Star Stuff");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        
        // On crée la vue du jeu (vide au début)
        gameView = new GameView(null);

        JPanel gameCenteringPanel = new JPanel(new GridBagLayout());
        gameCenteringPanel.setBackground(new Color(10, 15, 40)); // Optionnel : fond noir autour du jeu
        gameCenteringPanel.add(gameView);
        
        // On crée le menu en lui disant quoi faire via WindowManager
        menuView = new MenuView(levelNum -> switchToGame(levelNum));
        
        container.add(menuView, "MENU");
        container.add(gameCenteringPanel, "GAME");
        
        frame.add(container);
    }

    public void init() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        showMenu();
    }

    // Basculer vers le jeu
    public void switchToGame(int levelNum) {
        // C'est ici qu'on demande au contrôleur d'initialiser le niveau
        if (controller != null) {
            controller.prepareLevel(levelNum);
        }
        
        cardLayout.show(container, "GAME");

        frame.pack(); // Redimensionne la fenêtre selon la GameView
        frame.setLocationRelativeTo(null); // Recentrer la fenêtre si tu veux

        container.requestFocusInWindow(); // Pour le clavier
    }

    // Revenir au menu
    public void showMenu() {
        cardLayout.show(container, "MENU");
    }

    // Getters pour que le contrôleur puisse manipuler les vues
    public GameView getGameView() { return gameView; }
    public JPanel getContainer() { return container; }
    public void setController(GameController controller) { this.controller = controller; }
}
