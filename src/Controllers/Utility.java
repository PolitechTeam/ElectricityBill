package Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utility {
    public static void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle("Ошибка");
        alert.showAndWait();
    }
}
