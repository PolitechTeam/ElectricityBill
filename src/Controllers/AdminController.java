package Controllers;

import Database.DatabaseHandler;
import Model.User;
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

    DatabaseHandler dbHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlHome.toFront();

        System.out.println("initializing database");
        dbHandler = DatabaseHandler.getDataBase(); // initializing database

        initializeHistory();
        initializeUsers();
        errorLabel.setVisible(false);
    }

    private void initializeUsers() {
        List<User> users = dbHandler.getAllUsers();
        pnUserItems.getChildren().clear();
        try {
            userItems = new ArrayList<>();

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);

                HBox item = FXMLLoader.load(Objects.requireNonNull(
                        getClass().getResource("../resources/fxml/UserItem.fxml")));
                Label userId = (Label) item.lookup("#userIdLabel");
                Label userLogin = (Label) item.lookup("#userLoginLabel");
                Label userPassword = (Label) item.lookup("#userPasswordLabel");
                Label userName = (Label) item.lookup("#userNameLabel");
                Label userAddress = (Label) item.lookup("#userAddressLabel");

                userId.setText(String.valueOf(user.getId()));
                userLogin.setText(user.getLogin());
                userPassword.setText(user.getPassword());
                userName.setText(user.getFIO());
                userAddress.setText(user.getAddress());

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

    public void handleDelUser() {
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
        for (HBox historyItem : historyItems) {
            pnHistoryItems.getChildren().add(historyItem);
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
            Stage stage = (Stage) btnSignOut.getScene().getWindow();
            stage.close();
        }
    }

    public void handleNewUserClick(ActionEvent actionEvent) {
        // TODO Search login for existence. If login exists, show error in the error label
        if (isFullyFilled()) {
            // TODO Add new user
            String login = loginInput.getText();
            String password = passwordInput.getText();
            String firstName = nameInput.getText();
            String surName = surnameInput.getText();
            String fatherName = fatherNameInput.getText();
            String city = cityInput.getText();
            String street = streetInput.getText();
            String house = houseInput.getText();
            int flat = 0;
            if (!flatInput.getText().isEmpty()) {
                try {
                    flat = Integer.parseInt(flatInput.getText());
                } catch (NumberFormatException exception) {
                    showErrorLabel("Неверный формат номера квартиры");
                    return;
                }
            }
            System.out.println(dbHandler.addUser(login, password, firstName, surName, fatherName, city, street, house, flat));
        } else {
            showErrorLabel("Заполните все необходимые поля");
        }
    }

    private void showErrorLabel(String err) {
        errorLabel.setText(err);
        errorLabel.setVisible(true);
    }

    private boolean isFullyFilled() {
        return !loginInput.getText().isEmpty()
                && !passwordInput.getText().isEmpty()
                && !nameInput.getText().isEmpty()
                && !surnameInput.getText().isEmpty()
                && !fatherNameInput.getText().isEmpty()
                && !cityInput.getText().isEmpty()
                && !streetInput.getText().isEmpty()
                && !houseInput.getText().isEmpty();
    }
}
