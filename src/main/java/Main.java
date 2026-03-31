//src/main/java/Main.java
import javax.swing.SwingUtilities;

import controller.GameController;
import view.AssetManager;
import view.WindowManager;

public class Main {
    public static void main(String[] args) {
        // On charge d'abord les images (sinon le menu sera vide)
        AssetManager.loadAssets();

        SwingUtilities.invokeLater(() -> {
            // On crée le gestionnaire de fenêtres (il crée la JFrame et le CardLayout)
            WindowManager winManager = new WindowManager();

            // On crée le contrôleur en lui donnant l'accès au manager
            GameController controller = new GameController(winManager);

            // On donne le contrôleur au manager (pour que le bouton "Jouer" puisse l'appeler)
            winManager.setController(controller);

            // On lance l'affichage sur le menu
            winManager.init();
        });
    }
}