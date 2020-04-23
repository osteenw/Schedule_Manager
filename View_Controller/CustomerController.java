package View_Controller;

import Model.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class CustomerController {

    @FXML
    private Button customerButton;

    @FXML
    private TableView<?> appointmentTable;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> addressColumn;

    @FXML
    private TableColumn<?, ?> cityColumn;

    @FXML
    private TableColumn<?, ?> zipColumn;

    @FXML
    private TableColumn<?, ?> countryColumn;

    @FXML
    private TableColumn<?, ?> phoneColumn;

    @FXML
    private Button logOutButton;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button editCustomerButton;

    @FXML
    private Button delCustomerButton;

    @FXML
    void addCustomerAction(ActionEvent event) {

    }

    @FXML
    void delCustomerAction(ActionEvent event) {

    }

    @FXML
    void editCustomerAction(ActionEvent event) {

    }

    @FXML
    void gotoAppointmentAction(ActionEvent event) {
        // Opens customer window.
        Node source = (Node) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppointmentScreen.fxml"));
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

        // Closes current window
        stage = (Stage) source.getScene().getWindow();
        stage.close();
        return;
    }

    @FXML
    void logOutAction(ActionEvent event) {
        if(Login.userLogout()){
            // Opens login window.
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

            // Closes current window
            stage = (Stage) source.getScene().getWindow();
            stage.close();
            return;
        }
    }

}
