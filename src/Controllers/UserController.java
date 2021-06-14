package Controllers;

import Database.DatabaseHandler;
import Model.Bill;
import Model.User;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parsing.ExcelParser;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    public Label lblGreeting;
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
    private TextArea txtAreaConsumption;

    @FXML
    private JFXButton btnGenBill;

    @FXML
    private Label lblHouse;

    @FXML
    private Label lblInitials;


    private List<Bill> history;
    private DatabaseHandler dbHandler;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = DatabaseHandler.getDataBase();
        user = ControllerAuthorization.getSignedInUser();

        fillUserInfo();
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        lblCurrentDate.setText(currentDate);
        int currentConsumption = dbHandler.getUserConsumption(user.getId());
        lblCurrentConsumption.setText(currentConsumption + " кВт. ч.");
        initializeHistory();
        pnlHome.toFront();
    }

    private void initializeHistory() {
        // TODO Добавить логику отображения всех элементов истории на панель
        history = dbHandler.getUserBills(user.getId());

        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("../resources/fxml/UserHistoryItem.fxml"));

                //give the items some effect

                nodes[i].setOnMouseEntered(event -> nodes[j].setStyle("-fx-background-color : #C4D4FF"));
                nodes[i].setOnMouseExited(event -> nodes[j].setStyle("-fx-background-color : #D0DCFF"));
                pnItems.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
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
            initializeReceipt();
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
        lblGreeting.setText("Добро пожаловать, " + user.getName() + " " + user.getFatherName() + "!");
        lblUserNumber.setText("ИНВ" + user.getId());
        lblInitials.setText(user.getFullName());
        lblCity.setText(user.getCity());
        lblStreet.setText(user.getStreet());
        lblHouse.setText(user.getHouse());
        lblFlat.setText(user.getFlat() + "");
    }
}
