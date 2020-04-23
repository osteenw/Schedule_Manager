package View_Controller;

import Model.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AppointmentController {

    @FXML
    private TableView<?> appointmentTable;

    @FXML
    private TableColumn<?, ?> customerColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> contactColumn;

    @FXML
    private TableColumn<?, ?> startColumn;

    @FXML
    private TableColumn<?, ?> endColumn;

    @FXML
    private Button logOutButton;

    // Closes appointment window, and reopens the login screen.
    @FXML
    void logOutAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initModality(Modality.NONE);
        alert.setTitle("Log Off");
        alert.setHeaderText("Confirm log out.");
        alert.setContentText("Are you sure you want to log off?\r");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Node source = (Node) event.getSource();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
            Parent root1 = null;
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.show();

            // Closes window
            stage = (Stage) source.getScene().getWindow();
            stage.close();
            return;
        }
    }

}