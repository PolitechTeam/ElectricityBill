package Controllers;

import Model.Bill;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
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

    List<Bill> history;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlHome.toFront();
        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("../resources/fxml/UserHistoryItem.fxml"));

                //give the items some effect

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #C4D4FF");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #D0DCFF");
                });
                pnItems.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void handleClicks(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == btnGenBill) {
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
