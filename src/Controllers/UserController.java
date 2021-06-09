package Controllers;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pnlHome.toFront();

        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("../resources/fxml/Item.fxml"));

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
        if (actionEvent.getSource() == btnGenBill) {
//            pnlGenBill.setStyle("-fx-background-color : #1620A1");
            pnlGenBill.toFront();
        }
        if (actionEvent.getSource() == btnAccount) {
//            pnlAccount.setStyle("-fx-background-color : #53639F");
            pnlAccount.toFront();
        }
        if (actionEvent.getSource() == btnHistory) {
//            pnlHistory.setStyle("-fx-background-color : #02030A");
            pnlHistory.toFront();
        }
        if(actionEvent.getSource()== btnHome)
        {
//            pnlHome.setStyle("-fx-background-color : #464F67");
            pnlHome.toFront();
        }
    }
}
