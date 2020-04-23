package View_Controller;

import Model.Appointment;
import Model.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML    private TableView<Appointment> appointmentTable;
    @FXML    private TableColumn<Appointment, String> titleColumn;
    @FXML    private TableColumn<Appointment, String> customerColumn;
    @FXML    private TableColumn<Appointment, String> typeColumn;
    @FXML    private TableColumn<Appointment, String> descriptionColumn;
    @FXML    private TableColumn<Appointment, String> contactColumn;
    @FXML    private TableColumn<Appointment, String> startColumn;
    @FXML    private TableColumn<Appointment, String> endColumn;


    @FXML    private Button customerButton;
    @FXML    private Button logOutButton;
    @FXML    private Button addAppointmentButton;
    @FXML    private Button editAptButton;
    @FXML    private Button delAptButton;

    // Initializes appointment table
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Returns all appointments matching the logged in user ID.
        appointmentList = Appointment.returnAllAppointments();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        appointmentTable.setItems(appointmentList);

    }

    // Opens popup window to add appointment
    @FXML
    void addAptAction(ActionEvent event) {

    }

    // Opens popup window to del appointment
    @FXML
    void delAptAction(ActionEvent event) {

    }

    // Opens popup window to edit appointment
    @FXML
    void editAptAction(ActionEvent event) {

    }

    // Opens customer window
    @FXML
    void gotoCustomerAction(ActionEvent event) {
        // Opens customer window.
        Node source = (Node) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerScreen.fxml"));
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


    // Closes appointment window, and reopens the login screen.
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