package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private VBox pnHistoryItems;

    @FXML
    private VBox pnUserItems;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnNewUser;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnSignOut;

    @FXML
    private Pane pnlHome;

    @FXML
    private Pane pnlHistory;

    @FXML
    private Pane pnlNewUser;

    @FXML
    private Pane pnlUsers;

    @FXML
    private Button delUserButton;

    //--------------Sign Up Elements--------------------
    @FXML
    private Label errorLabel;
    @FXML
    private Button addNewUserBtn;
    @FXML
    private TextField loginInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField surnameInput;
    @FXML
    private TextField fatherNameInput;
    @FXML
    private TextField cityInput;
    @FXML
    private TextField streetInput;
    @FXML
    private TextField houseInput;
    @FXML
    private TextField flatInput;

    private List<HBox> historyItems;
    private List<HBox> userItems;
    private HBox selectedUserItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pnlHome.toFront();

        initializeHistory();
        initializeUsers();
        errorLabel.setVisible(false);
    }

    private void initializeUsers() {
        try {
            userItems = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                HBox item = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../resources/fxml/UserItem.fxml")));
                userItems.add(item);
                item.setOnMouseClicked(this::handleUserSelection);
                pnUserItems.getChildren().add(userItems.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUserSelection(MouseEvent event) {
        HBox item = (HBox) event.getSource();
        if (selectedUserItem != null) {
            int index = userItems.indexOf(selectedUserItem);
            userItems.get(index).setStyle("-fx-background-color:  #D0DCFF;");
        }
        selectedUserItem = item;
        item.setStyle("-fx-background-color:  #839aff;");
        delUserButton.setDisable(false);
    }

    public void handleDelUser(MouseEvent event) {
        userItems.remove(selectedUserItem);
        pnUserItems.getChildren().remove(selectedUserItem);
        selectedUserItem = null;
        delUserButton.setDisable(true);
    }
    @FXML
    private DatePicker datePicker;

    public void clearDate(MouseEvent event) {
        datePicker.setValue(null);
    }

    private void fillHistoryPane() {
        for (int i = 0; i < historyItems.size(); i++) {
            pnHistoryItems.getChildren().add(historyItems.get(i));
        }
    }

    private void initializeHistory() {
        historyItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            try {
                historyItems.add(FXMLLoader.load(getClass().getResource("../resources/fxml/HistoryItem.fxml")));
                pnHistoryItems.getChildren().add(historyItems.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnNewUser) {
            pnlNewUser.toFront();
        }
        if (actionEvent.getSource() == btnUsers) {
            pnlUsers.toFront();
        }
        if (actionEvent.getSource() == btnHistory) {
            pnlHistory.toFront();
        }
        if (actionEvent.getSource() == btnHome) {
            pnlHome.toFront();

        }
        if (actionEvent.getSource() == btnSignOut) {
            Stage stage = (Stage)btnSignOut.getScene().getWindow();
            stage.close();
        }
    }

    public void handleNewUserClick(ActionEvent actionEvent) {
        // TODO Search login for existence. If login exists, show error in the error label
        if (isFullyFilled()) {
            // TODO Add new user
        } else {
            errorLabel.setText("Заполните все необходимые поля");
            errorLabel.setVisible(true);
        }
    }

    private boolean isFullyFilled() {
        if (loginInput.getText().isEmpty()
                || passwordInput.getText().isEmpty()
                || nameInput.getText().isEmpty()
                || surnameInput.getText().isEmpty()
                || fatherNameInput.getText().isEmpty()
                || cityInput.getText().isEmpty()
                || streetInput.getText().isEmpty()
                || houseInput.getText().isEmpty())
            return false;
        else
            return true;
    }
}
