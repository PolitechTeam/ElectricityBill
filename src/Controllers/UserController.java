package Controllers;

import Database.DatabaseHandler;
import Model.Bill;
import Model.User;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parsing.ExcelParser;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class UserController implements Initializable {

    @FXML
    public Label lblGreeting;
    @FXML
    public TextField txtFieldConsumption;
    @FXML
    public Label lblFotoInfo;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane pnlGenBill;

    @FXML
    private Label lblFlat;

    @FXML
    private Button btnSignOut;

    @FXML
    private Pane pnlHistory;

    @FXML
    private Label lblCurrentConsumption;

    @FXML
    private JFXButton btnGiveConsumption;

    @FXML
    private JFXButton btnEditPassword;

    @FXML
    private Button btnSectionHome;

    @FXML
    private Button btnSectionAccount;

    @FXML
    private VBox pnItems;

    @FXML
    private Pane pnlAccount;

    @FXML
    private Label lblCity;

    @FXML
    private Button btnSectionGenBill;

    @FXML
    private Pane pnlHome;

    @FXML
    private Button btnSectionHistory;

    @FXML
    private Label lblCurrentDate;

    @FXML
    private JFXButton btnEditProfile;

    @FXML
    private Label lblUserNumber;

    @FXML
    private Label lblStreet;


    @FXML
    private JFXButton btnGenBill;

    @FXML
    private Label lblHouse;

    @FXML
    private Label lblInitials;

    private List<Bill> bills;
    private List<HBox> historyItems;
    private DatabaseHandler dbHandler;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = DatabaseHandler.getDataBase();
        user = ControllerAuthorization.getSignedInUser();
        bills = dbHandler.getUserBills(user.getId());

        btnGenBill.setDisable(true);
        btnGenBill.setOnAction(event -> generateReceipt());
        btnGiveConsumption.setOnAction(event -> addNewIndication());

        fillUserInfo();
        pnlAccount.toFront();
    }

    private void fillConsumptionInfo() {
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        lblCurrentDate.setText(currentDate);
        lblCurrentConsumption.setText(getLastIndication() + " кВт. ч.");
    }

    private int getLastIndication() {
        return bills.size() > 0 ? bills.get(bills.size() - 1).getIndication() : 0;
    }

    private void addNewIndication() {
        if (!txtFieldConsumption.getText().isEmpty()) {
            int newIndication = Integer.parseInt(txtFieldConsumption.getText());
            txtFieldConsumption.setText("");

            if (newIndication > getLastIndication()) {
                java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
                dbHandler.addBill(user.getId(), newIndication, now);
                bills = dbHandler.getUserBills(user.getId());

                btnGenBill.setDisable(false);
                fillConsumptionInfo();

                System.out.println("Данные успешно занесены в БД!");
            } else {
                System.err.println("Введены некорректные данные!");
            }
        }
    }

    private void fillHistoryInfo() {
        historyItems = new ArrayList<>();
        pnItems.getChildren().clear();

        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            int prevIndication = i > 0 ? bills.get(i - 1).getIndication() : 0;

            try {
                HBox item = FXMLLoader.load(
                        getClass().getResource("../resources/fxml/UserHistoryItem.fxml"));
                Label paymentDate = (Label) item.lookup("#paymentDate");
                Label indication = (Label) item.lookup("#indication");
                Label consumedEnergy = (Label) item.lookup("#consumedEnergy");

                paymentDate.setText(String.valueOf(bill.getPaymentDate()));
                indication.setText(String.valueOf(bill.getIndication()));
                consumedEnergy.setText(String.valueOf(bill.getIndication() - prevIndication));

                item.setOnMouseEntered(event -> item.setStyle("-fx-background-color : #C4D4FF"));
                item.setOnMouseExited(event -> item.setStyle("-fx-background-color : #D0DCFF"));

                historyItems.add(item);
                pnItems.getChildren().add(item);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void generateReceipt() {
        final String PATH = "src/resources/receipts/";
        final String IN_FILE_NAME = PATH + "in_receipt.xlsx";
        final String OUT_FILE_NAME = PATH + "out_receipt.xlsx";
        final String PDF_FILE_NAME = PATH + "receipt.pdf";

        Bill currBill = bills.get(bills.size() - 1);
        int prevIndication = bills.size() > 1 ? bills.get(bills.size() - 2).getIndication() : 0;

        new Thread(() -> {
            btnGenBill.setDisable(true);
            XSSFWorkbook book = ExcelParser.openFromXLSX(IN_FILE_NAME);
            ExcelParser.writeToXLSX(book, user, currBill, prevIndication);
            ExcelParser.saveToXLSX(book, OUT_FILE_NAME);

            ExcelParser.saveToPDF(OUT_FILE_NAME, PDF_FILE_NAME);
            btnGenBill.setDisable(false);

            ExcelParser.openPDF(PDF_FILE_NAME);
        }).start();
    }

    public void handleClicks(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == btnSectionGenBill) {
            fillConsumptionInfo();
            pnlGenBill.toFront();
        }
        if (source == btnSectionAccount) {
            fillUserInfo();
            pnlAccount.toFront();
        }
        if (source == btnSectionHistory) {
            fillHistoryInfo();
            pnlHistory.toFront();
        }
        if(source == btnSectionHome) {
            pnlHome.toFront();
        }
        if (source == btnSignOut) {
            Stage stage = (Stage) btnSignOut.getScene().getWindow();
            stage.close();
        }
    }

    private void fillUserInfo() {
        lblFotoInfo.setText(user.getName() + " " + user.getFatherName());
        lblGreeting.setText("Добро пожаловать, " + user.getName() + " " + user.getFatherName() + "!");
        lblUserNumber.setText("ИНВ" + user.getId());
        lblInitials.setText(user.getFullName());
        lblCity.setText(user.getCity());
        lblStreet.setText(user.getStreet());
        lblHouse.setText(user.getHouse());
        lblFlat.setText(user.getFlat() + "");
    }
}
