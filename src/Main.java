import Controllers.ControllerAuthorization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ControllerAuthorization.showWindow(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
