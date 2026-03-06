//src/main/java/Main.java
import javax.swing.SwingUtilities;
import model.GameModel;
import model.Level;
import view.GameView;
import controller.GameController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //on charge le niveau voulu ici
            Level level = Level.LEVEL_1;

            //creation du modele
            GameModel model = new GameModel(level.getWidth(), level.getHeight());

            //on applique la config du niveau
            level.setup(model);

            //on lance la vue et le controller
            GameView view = new GameView(model);
            GameController controller = new GameController(model, view);

            controller.start();
        });
    }
}