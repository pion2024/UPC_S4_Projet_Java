package com.starstuff;

import com.starstuff.controller.GameController;
import com.starstuff.model.GameWorld;
import com.starstuff.view.GameView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main Entry Point.
 */
public class App extends Application {

    private GameController controller;

    @Override
    public void start(Stage stage) {
        // 1. Initialize MVC Components
        GameWorld model = new GameWorld();
        GameView view = new GameView(model);
        
        // 2. Initialize Controller
        controller = new GameController(model, view);

        // 3. Configure Stage
        stage.setTitle("Star Stuff Clone - JavaFX MWE");
        stage.setScene(view.getScene());
        stage.setResizable(false);
        stage.show();

        // 4. Start Game Loop
        controller.startGame();
    }

    @Override
    public void stop() throws Exception {
        controller.stop();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}