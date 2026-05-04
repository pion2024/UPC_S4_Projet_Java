package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuView extends JPanel {
    private Consumer<Integer> onLevelSelected; // envoie le level choisi a windowManager

    public MenuView(Consumer<Integer> onLevelSelected) {
        this.onLevelSelected = onLevelSelected;
        this.setPreferredSize(new Dimension(800, 600)); // Taille du menu
        setLayout(new BorderLayout());

        // Panneau central pour le Titre et les Boutons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false); // Important pour voir l'image de fond derrière

        // Espace flexible en haut
        mainPanel.add(Box.createVerticalGlue());

        // LOGO
        JLabel titleLabel = new JLabel("Star Stuff");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        // Espace entre titre et boutons
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // BOUTONS
        JButton playButton = createMenuButton("JOUER");
        JButton quitButton = createMenuButton("QUITTER");

        playButton.addActionListener(e -> showLevelSelection());
        quitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(playButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espace entre boutons
        mainPanel.add(quitButton);

        // 5. Espace flexible en bas
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel, BorderLayout.CENTER);

    }

    // ici l'image est réellement dessinée
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // On vérifie si l'image est bien chargée dans l'AssetManager
        if (AssetManager.getInstance().getBackground() != null) {
            // On dessine l'image pour qu'elle occupe tout le panneau
            g.drawImage(AssetManager.getInstance().getBackground(), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Couleur de secours si l'image ne charge pas
            g.setColor(new Color(30, 30, 30));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void showLevelSelection() {
        String[] levels = {"Niveau 1", "Niveau 2", "Niveau 3 (Bientôt)"};
        String choice = (String) JOptionPane.showInputDialog(this, 
            "Choisissez un monde :", "Sélection de niveau",
            JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);

        if (choice != null) {
            if (choice.contains("Bientôt")) {
            JOptionPane.showMessageDialog(this, "Ce niveau n'est pas encore disponible !");
            return;
            }
            // On récupère le dernier caractère pour avoir le numéro (ex: "1")
            int levelNum = Character.getNumericValue(choice.charAt(choice.length() - 1));
            onLevelSelected.accept(levelNum);
        }
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setMaximumSize(new Dimension(250, 60));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}