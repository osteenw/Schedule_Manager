package View_Controller;

import Model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class CreateAppointmentController implements Initializable {
    public Stage stage;
    ObservableList<Customer> customerList = Customer.returnAllCustomers();

    @FXML    private Label titleLabel;
    @FXML    private Button saveButton;
    @FXML    private Button cancelButton;
    @FXML    private TextField titleField;
    @FXML    private TextField typeField;
    @FXML    private TextField descriptionField;
    @FXML    private TextField locationField;
    @FXML    private TextField contactField;
    @FXML    private ComboBox<Customer> customerPicker;
    @FXML    private ChoiceBox<?> startHrs;
    @FXML    private ChoiceBox<?> startMins;
    @FXML    private DatePicker datePicker;
    @FXML    private ChoiceBox<?> endHrs;
    @FXML    private ChoiceBox<?> endMins;

    @FXML
    void cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel New Customer");
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

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerPicker.setItems(customerList);

    }
}