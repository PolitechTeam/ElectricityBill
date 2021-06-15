package Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class Utility {
    public static Optional<ButtonType> showInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(message);
        return alert.showAndWait();
    }


    public static Optional<ButtonType> showWarningDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(message);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showQuestionDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтвеждение");
        alert.setHeaderText(message);
        return alert.showAndWait();
    }
}
