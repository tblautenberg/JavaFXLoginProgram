import java.io.File;
import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application 
{

    // Main scene here

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlPath = "LoginScene.fxml";
        File file = new File(fxmlPath);
        Parent root = FXMLLoader.load(file.toURI().toURL());
        primaryStage.setTitle("JavaFX App");
        primaryStage.setScene(new Scene(root, 774, 707));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
 