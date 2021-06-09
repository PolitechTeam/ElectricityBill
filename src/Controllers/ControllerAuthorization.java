package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import Database.DatabaseHandler;
import Model.User;

public class ControllerAuthorization {

    private final String ADMIN_LOGIN = "1";
    private final String ADMIN_PASSWORD = "1";

    @FXML
    public JFXButton closeButton;

    @FXML
    public Label wrongAuthorizationLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton signInButton;

    @FXML
    private JFXTextField loginField;

    @FXML
    void initialize() {
        signInButton.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String passwordText = passwordField.getText().trim();
            if (!loginText.equals("") && !passwordText.equals("")) {
                if (loginText.equals(ADMIN_LOGIN) && passwordText.equals(ADMIN_PASSWORD)){
                    openAdminView();
                }
                signIn(loginText, passwordText);
            } else {
                System.out.println("Проверьте логин и пароль!");
            }
        });

        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

    }

    private void signIn(String loginText, String passwordText) {
        try {
            DatabaseHandler dbHandler = new DatabaseHandler();
            User user = new User();
            user.setLogin(loginText);
            user.setPassword(passwordText);
            ResultSet result = dbHandler.getUser(user);
            int countDbEntries = 0;
            while (result.next()) {
                countDbEntries++;
            }
            if (countDbEntries != 0) {
                wrongAuthorizationLabel.setText("");
                openMenuView();

            } else {
                wrongAuthorizationLabel.setText("Неверный логин или пароль!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openAdminView() {
        signInButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/AdminView.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            System.out.println("Ошибка при загрузке файла userView.fxml");
            ex.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


    private void openMenuView() {
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
        stage.showAndWait();
    }


}
