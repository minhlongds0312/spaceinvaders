package invaders;

import javafx.application.Application;
import javafx.stage.Stage;
import invaders.engine.ConfigReader;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.util.Map;

import org.json.simple.JSONObject;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Map<String, String> params = getParameters().getNamed();

        GameEngine model = new GameEngine("src/main/resources/config.json");
        ConfigReader configReader = new ConfigReader("src/main/resources/config.json");
        JSONObject config = configReader.getGame();
        Long width = (Long) ((JSONObject) config.get("size")).get("x");
        long widthValue = width.longValue();
        int widthInt = (int) widthValue;
        Long height = (Long) ((JSONObject) config.get("size")).get("y");
        long heightValue = height.longValue();
        int heightInt = (int) heightValue;
    

        GameWindow window = new GameWindow(model, widthInt, heightInt);
        window.run();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();

    }
}
