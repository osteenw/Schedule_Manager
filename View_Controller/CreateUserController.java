package View_Controller;

import Model.NewUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class CreateUserController {
    public Stage stage;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    void cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel New User");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel?\r");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void save(ActionEvent event) {
        boolean parseError = false;
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String userName = usernameField.getText();
        String password = passwordField.getText();
        String createdBy = firstName + " " + lastName;
        String lastUpdateBy = firstName + " " + lastName;

        if(firstName.isEmpty() == true || lastName.isEmpty() == true || userName.isEmpty() == true || password.isEmpty() == true) {
            parseError = true;
        }

        if(parseError) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("All fields must be filled in.");
            alert.showAndWait();
            return;
        }

        if(!parseError) {
            NewUser user = new NewUser(userName, password, createdBy, lastUpdateBy);
            user.createNewUser();
        }

        //closes window
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

}
