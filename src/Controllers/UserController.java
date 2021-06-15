package Controllers;

import Database.DatabaseHandler;
import Model.Bill;
import Model.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parsing.ExcelParser;

import java.io.IOException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.*;

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

        fillUserInfo();
        initializeHistory();

        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        lblCurrentDate.setText(currentDate);

        int currentConsumption = bills.get(bills.size() - 1).getIndication();
        lblCurrentConsumption.setText(currentConsumption + " кВт. ч.");

        btnGenBill.setDisable(true);
        btnGenBill.setOnAction(event -> initializeReceipt());
        btnGiveConsumption.setOnAction(event -> btnGenBill.setDisable(false));

        pnlHome.toFront();
    }

    private void initializeHistory() {
        historyItems = new ArrayList<>();
        pnItems.getChildren().clear();

        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            int prevIndication = i != 0 ? bills.get(i - 1).getIndication() : 0;

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

    private void initializeReceipt() {
        // TODO Переделать на получение данных из БД

        final String PATH = "src/resources/receipts/";
        final String IN_FILE_NAME = PATH + "in_receipt.xlsx";
        final String OUT_FILE_NAME = PATH + "out_receipt.xlsx";
        final String PDF_FILE_NAME = PATH + "receipt.pdf";

        int prevIndication = 1200;
        int currIndication = 1500;

        Bill currBill = new Bill(1, user.getId(), currIndication, new Date());

        new Thread(() -> {
            XSSFWorkbook book = ExcelParser.openFromXLSX(IN_FILE_NAME);
            ExcelParser.writeToXLSX(book, user, currBill, prevIndication);
            ExcelParser.saveToXLSX(book, OUT_FILE_NAME);

            ExcelParser.saveToPDF(OUT_FILE_NAME, PDF_FILE_NAME);
        }).start();
    }


    public void handleClicks(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == btnSectionGenBill) {
            pnlGenBill.toFront();
        }
        if (source == btnSectionAccount) {
            pnlAccount.toFront();
        }
        if (source == btnSectionHistory) {
            pnlHistory.toFront();
        }
        if(source == btnSectionHome)
        {
            pnlHome.toFront();
        }
        if (source == btnSignOut) {
            Stage stage = (Stage) btnSignOut.getScene().getWindow();
            stage.close();
        }
    }

    private void fillUserInfo(){
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
