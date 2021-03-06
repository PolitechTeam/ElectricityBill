package Controllers;

import Database.DatabaseHandler;
import Model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class ControllerAuthorization {

    private final String ADMIN_LOGIN = "admin";
    private final String ADMIN_PASSWORD = "admin";
    @FXML
    public JFXButton closeButton;
    @FXML
    public Label wrongAuthorizationLabel;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton signInButton;
    @FXML
    private JFXTextField loginField;

    private DatabaseHandler dbHandler;
    private static User signedInUser;
    private static Stage mainStage;

    @FXML
    void initialize() {
        dbHandler = DatabaseHandler.getDataBase(); // initializing dataBase
        signInButton.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String passwordText = passwordField.getText().trim();
            if (!loginText.equals("") && !passwordText.equals("")) {
                if (loginText.equals(ADMIN_LOGIN) && passwordText.equals(ADMIN_PASSWORD)){
                    openAdminView();
                }else {
                    signIn(loginText, passwordText);
                }
            } else {
                System.out.println("Проверьте логин и пароль!");
            }
        });

        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

    }

    public static void showWindow(Stage primaryStage) throws IOException {
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(ControllerAuthorization.class.getResource("../resources/fxml/AuthorizationView.fxml"))
        );
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setScene(new Scene(root, 712, 435));
        mainStage.show();
    }

    private void signIn(String login, String password) {
        signedInUser = dbHandler.getUser(login, password);
        if (signedInUser != null) {
            wrongAuthorizationLabel.setText("");
            openUserView();
        } else {
            wrongAuthorizationLabel.setText("Неверный логин или пароль!");
        }
    }

    private double x1, y1; // Used to drag screen
    private void openAdminView() {
        signInButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/AdminView.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        // Remove window frame
        stage.initStyle(StageStyle.UNDECORATED);

        //--Dragging screen--
        root.setOnMousePressed(event -> {
            x1 = event.getSceneX();
            y1 = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x1);
            stage.setY(event.getScreenY() - y1);

        });
        //--------------------
        stage.showAndWait();
        mainStage.show();
    }

    private double x2, y2; // Used to drag screen
    private void openUserView() {
        signInButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/userView.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            System.out.println("Ошибка при загрузке файла userView.fxml");
            ex.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Remove window frame
        stage.initStyle(StageStyle.UNDECORATED);

        //--Dragging screen--
        root.setOnMousePressed(event -> {
            x2 = event.getSceneX();
            y2 = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x2);
            stage.setY(event.getScreenY() - y2);

        });

        stage.showAndWait();
        mainStage.show();
    }



    public static User getSignedInUser() {
        return signedInUser;
    }

    public static void updateUser(User user){
        signedInUser = user;
    }


}
