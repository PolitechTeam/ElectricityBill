package Controllers;

import javafx.event.ActionEvent;
import Database.DatabaseHandler;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import Model.User;

public class EditPasswordController
{
    private User user;
    @FXML
    public Label lblPassIsEmpty;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField txtFieldCopyNewPas;
    @FXML
    private JFXButton btnSavePas;
    @FXML
    private Label lblCurrentPassword;
    @FXML
    private JFXButton btnCancelEditPas;
    @FXML
    private Label lblPassNotMatch;
    @FXML
    private TextField txtFldNewPas;

    @FXML
    void initialize() {
        this.user = ControllerAuthorization.getSignedInUser();
        this.lblPassNotMatch.setVisible(false);
        this.lblPassIsEmpty.setVisible(false);
        this.lblCurrentPassword.setText(this.user.getPassword());
        this.btnCancelEditPas.setOnAction(event -> this.closeView());
        this.btnSavePas.setOnAction(event -> this.savePassword());
    }

    private void closeView() {
        final Stage stage = (Stage)this.btnCancelEditPas.getScene().getWindow();
        stage.close();
    }

    private void savePassword() {
        this.lblPassIsEmpty.setVisible(false);
        this.lblPassNotMatch.setVisible(false);
        final String newPassword = this.txtFldNewPas.getText();
        final String copyNewPassword = this.txtFieldCopyNewPas.getText();
        if (newPassword.isEmpty()) {
            this.lblPassIsEmpty.setVisible(true);
        }
        else if (!newPassword.equals(copyNewPassword)) {
            this.lblPassNotMatch.setVisible(true);
        }
        else {
            DatabaseHandler.getDataBase().updateUser(this.user.getId(), this.user.getLogin(), newPassword);
            final User updatedUser = DatabaseHandler.getDataBase().getUser(this.user.getLogin(), newPassword);
            UserController.setNewUser(updatedUser);
            this.user = updatedUser;
            this.lblCurrentPassword.setText(newPassword);
            ControllerAuthorization.updateUser(updatedUser);
            this.closeView();
        }
    }
}