package Controllers;

import Database.DatabaseHandler;
import Model.Bill;
import Model.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    @FXML
    private Label successLabel;

    private List<HBox> historyItems;
    private List<HBox> userItems;
    private HBox selectedUserItem;

    DatabaseHandler dbHandler;
    List<Bill> bills;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlHome.toFront();

        System.out.println("initializing database");
        dbHandler = DatabaseHandler.getDataBase(); // initializing database
        bills = dbHandler.getAllBills();
        initializeHistory(bills);
        initializeUsers();
        errorLabel.setVisible(false);
        successLabel.setVisible(false);

    }
    @FXML
    private Label userCountLabel;
    private void initializeUsers() {
        List<User> users = dbHandler.getAllUsers();
        userCountLabel.setText(String.valueOf(users.size()));
        pnUserItems.getChildren().clear();
        try {
            userItems = new ArrayList<>();
            for (User user : users) {
                addUserItem(user);
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

    public void handleUserRemove() {
        userItems.remove(selectedUserItem);
        pnUserItems.getChildren().remove(selectedUserItem);
        int selectedUserId = Integer.parseInt(((Label) selectedUserItem.lookup("#userIdLabel")).getText());
        System.out.println(dbHandler.deleteUser(selectedUserId));
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
    @FXML
    private Label billsCountLabel;
    private void initializeHistory(List<Bill> bills) {
        historyItems = new ArrayList<>();
        pnHistoryItems.getChildren().clear();
        billsCountLabel.setText(String.valueOf(bills.size()));
        for (Bill bill : bills) {
            try {
                addHistoryItem(bill);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void addUserItem(User user) throws IOException {
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
        pnUserItems.getChildren().add(item);
    }

    private void addHistoryItem(Bill bill) throws IOException {
        HBox item = FXMLLoader.load(
                getClass().getResource("../resources/fxml/HistoryItem.fxml"));
        Label userId = (Label) item.lookup("#userId");
        Label paymentDate = (Label) item.lookup("#paymentDate");
        Label indication = (Label) item.lookup("#indication");


        userId.setText(String.valueOf(bill.getUserId()));
        paymentDate.setText(String.valueOf(bill.getPaymentDate()));
        indication.setText(String.valueOf(bill.getIndication()));

        historyItems.add(item);
        pnHistoryItems.getChildren().add(item);
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnNewUser) {
            pnlNewUser.toFront();
        }
        if (actionEvent.getSource() == btnUsers) {
            clearFields();
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

    private void clearFields() {
        loginInput.clear();
        passwordInput.clear();
        nameInput.clear();
        surnameInput.clear();
        fatherNameInput.clear();
        cityInput.clear();
        streetInput.clear();
        houseInput.clear();
        flatInput.clear();
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

    public void handleNewUserClick(ActionEvent actionEvent) throws IOException {
        if (isFullyFilled()) {
            String login = loginInput.getText().toLowerCase(Locale.ROOT);
            String password = passwordInput.getText();
            String firstName = nameInput.getText();
            String surName = surnameInput.getText();
            String fatherName = fatherNameInput.getText();
            String city = cityInput.getText();
            String street = streetInput.getText();
            String house = houseInput.getText();
            int flat = 0;
            // Checking if we have user with identical login
            if (dbHandler.hasUserWithLogin(login)) {
                showErrorLabel("Пользователь с логином " + login + " уже сущ.");
                return;
            }
            if (!flatInput.getText().isEmpty()) {
                try {
                    flat = Integer.parseInt(flatInput.getText());
                } catch (NumberFormatException exception) {
                    showErrorLabel("Неверный формат номера квартиры");
                    return;
                }
            }

            int id = dbHandler.addUser(login, password, firstName, surName, fatherName, city, street, house, flat);
            User user = new User(id, login, password, firstName, surName, fatherName, city, street, house, flat);
            addUserItem(user);
            showSuccessLabel("Успешно добавлен");
        } else {
            showErrorLabel("Заполните все необходимые поля");
        }
    }

    private void showSuccessLabel(String s) {
        errorLabel.setVisible(false);
        successLabel.setText(s);
        successLabel.setVisible(true);
    }

    private void showErrorLabel(String err) {
        errorLabel.setText(err);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
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

    public void datePickerHandler(ActionEvent actionEvent) {
        if (datePicker.getValue() == null) {
            initializeHistory(bills);
            return;
        }
        final String date = datePicker.getValue().toString();
        List<Bill> billList = new ArrayList<>();
        for (Bill bill: bills) {
            if (bill.getPaymentDate().toString().equals(date)) {
                billList.add(bill);
            }
        }
        initializeHistory(billList);
    }


}
