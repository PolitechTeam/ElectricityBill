package Controllers;

import javafx.event.ActionEvent;
import Database.DatabaseHandler;
import javafx.stage.Stage;
import Model.User;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import javafx.fxml.FXML;
import java.util.ResourceBundle;

public class EditLoginController
{
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField txtFieldCopyNewLogin;
    @FXML
    private Label lblLoginIsEmpty;
    @FXML
    private Label lblLoginNotMatch;
    @FXML
    private TextField txtFldNewLogin;
    @FXML
    private JFXButton btnCancelEditLogin;
    @FXML
    private JFXButton btnSaveLogin;
    @FXML
    private Label lblCurrentLogin;
    private User user;

    @FXML
    void initialize() {
        this.user = ControllerAuthorization.getSignedInUser();
        this.lblLoginNotMatch.setVisible(false);
        this.lblLoginIsEmpty.setVisible(false);
        this.lblCurrentLogin.setText(this.user.getLogin());
        this.btnCancelEditLogin.setOnAction(event -> this.closeView());
        this.btnSaveLogin.setOnAction(event -> this.saveLogin());
    }

    private void closeView() {
        final Stage stage = (Stage)this.btnCancelEditLogin.getScene().getWindow();
        stage.close();
    }

    private void saveLogin() {
        this.lblLoginIsEmpty.setVisible(false);
        this.lblLoginNotMatch.setVisible(false);
        final String newLogin = this.txtFldNewLogin.getText();
        final String copyNewLogin = this.txtFieldCopyNewLogin.getText();
        if (newLogin.isEmpty()) {
            this.lblLoginIsEmpty.setVisible(true);
        }
        else if (!newLogin.equals(copyNewLogin)) {
            this.lblLoginNotMatch.setVisible(true);
        }
        else {
            DatabaseHandler.getDataBase().updateUser(this.user.getId(), newLogin, this.user.getPassword());
            final User updatedUser = DatabaseHandler.getDataBase().getUser(newLogin, this.user.getPassword());
            UserController.setNewUser(updatedUser);
            this.user = updatedUser;
            this.lblCurrentLogin.setText(newLogin);
            ControllerAuthorization.updateUser(updatedUser);
            this.closeView();
        }
    }
}