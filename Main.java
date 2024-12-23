//DeMichael Morgan
//COMP167.001
//11.25.24
//The Main class initializes the JavaFX application,
// it sets up the primary stage and scene for the game.
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ProfilePane profilePane = new ProfilePane("profileName.txt", "configFile");
        Scene scene = new Scene(profilePane, 400, 300);

        primaryStage.setTitle("Brick Breaker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
