package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class CustomerController implements Initializable {
    ObservableList<Customer> customerList;
    public static Customer customerToEdit;

    @FXML    private TableView<Customer> customerTable;
    @FXML    private TableColumn<Customer, String> nameColumn;
    @FXML    private TableColumn<Customer, String> addressColumn;
    @FXML    private TableColumn<Customer, String> cityColumn;
    @FXML    private TableColumn<Customer, String> zipColumn;
    @FXML    private TableColumn<Customer, String> countryColumn;
    @FXML    private TableColumn<Customer, String> phoneColumn;

    @FXML    private Button customerButton;
    @FXML    private Button logOutButton;
    @FXML    private Button addCustomerButton;
    @FXML    private Button editCustomerButton;
    @FXML    private Button delCustomerButton;

    // Initializes customer table
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Returns all customers.
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        refreshTable();

    }

    public void refreshTable() {
        customerList = Customer.returnAllCustomers();
        customerTable.setItems(customerList);
    }

    @FXML
    void addCustomerAction(ActionEvent event) {
        // Opens add customer window.
        CreateCustomerController.createCustomer = true;
        Node source = (Node) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateCustomerScreen.fxml"));
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
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                refreshTable();
            }
        });
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                refreshTable();
            }
        });
    }

    @FXML
    void delCustomerAction(ActionEvent event) {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No customer selected");
            alert.showAndWait();
            return;
        }

        // Gets selected appointment
        Customer customerToDel = customerTable.getSelectionModel().getSelectedItem();

        // Confirms user wants to delete the appointment.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Value returning lambda expression
        Message message = s -> "Are you sure you want to delete customer \n" + s + "?\n \n " +
                "This will delete all appointments with the customer also.\r";

        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText("Confirm deletion.");
        alert.setContentText(message.getMessage("customerToDel.getCustomerName()") );
        Optional<ButtonType> result = alert.showAndWait();

        // If user confirms, appointment will be deleted, and the table refreshed.
        if (result.get() == ButtonType.OK) {
            Customer.deleteCustomer(customerToDel.getCustomerId(), customerToDel.getAddressId());
            refreshTable();
        }
        return;
    }

    @FXML
    void editCustomerAction(ActionEvent event) {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No customer selected");
            alert.showAndWait();
            return;
        }

        // Gets selected customer
        customerToEdit = customerTable.getSelectionModel().getSelectedItem();

        // Opens customer window.
        CreateCustomerController.createCustomer = false;
        Node source = (Node) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateCustomerScreen.fxml"));
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
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                refreshTable();
            }
        });
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                refreshTable();
            }
        });
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
