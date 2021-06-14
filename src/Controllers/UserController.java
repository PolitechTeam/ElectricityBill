package Controllers;

import Database.DatabaseHandler;
import Model.Bill;
import Model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parsing.ExcelParser;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnHistory;
    @FXML
    private Button btnGenBill;
    @FXML
    private Button btnAccount;
    @FXML
    private Button btnSignOut;
    @FXML
    private Pane pnlGenBill;
    @FXML
    private Pane pnlHome;
    @FXML
    private Pane pnlHistory;
    @FXML
    private Pane pnlAccount;

    private List<Bill> history;
    private DatabaseHandler dbHandler;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = DatabaseHandler.getDataBase();
        user = ControllerAuthorization.getSignedInUser();

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
        if (source == btnGenBill) {
            initializeReceipt();
            pnlGenBill.toFront();
        }
        if (source == btnAccount) {
            pnlAccount.toFront();
        }
        if (source == btnHistory) {
            pnlHistory.toFront();
        }
        if(source == btnHome)
        {
            pnlHome.toFront();
        }
        if (source == btnSignOut) {
            Platform.exit();
        }
    }
}
