//src/main/java/Main.java
import javax.swing.SwingUtilities;
import model.GameModel;
import view.GameView;
import controller.GameController;
import model.board.Board;
import model.board.Matrix;
import model.entity.*;
import model.physic.Direction;
import model.physic.Position;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Matrix<Items> matrix = new Matrix<>(10, 10);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    matrix.setItem(i, j, new Ground(0, new Position(j, i)));
                }
            }

            Board board = new Board(matrix);
            GameModel model = new GameModel(board);

            Switch sw1 = new Switch(1, new Position(2, 2), true, null, Direction.UP);
            Bridge bridge1 = new Bridge(2, new Position(5, 5), false, Direction.UP);
            
            bridge1.addSwitch(sw1);
            
            matrix.setItem(2, 2, sw1);
            matrix.setItem(5, 5, bridge1); 

            model.addBridge(bridge1);

            Robot player = new Robot(new Position(0, 0));
            board.getMovableEntities().add(player);
            model.setPlayer(player);

            GameView view = new GameView(model);
            GameController controller = new GameController(model, view);

            controller.start();
        });
    }
}