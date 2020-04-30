package View_Controller;

import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {
    ObservableList<Appointment> appointmentList = Appointment.returnAllAppointments(Login.getUserId());
    ObservableList<Users> userList = Users.returnAllUsers();
    public static Appointment appointmentToEdit;
    Users chosenUser;

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> customerColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;


    @FXML
    private Button customerButton;
    @FXML
    private Button uniqueReportButton;
    @FXML
    private Button totalReportButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button editAptButton;
    @FXML
    private Button delAptButton;

    @FXML
    private RadioButton viewAllRadio;
    @FXML
    private ToggleGroup appointmentView;
    @FXML
    private RadioButton viewWeekRadio;
    @FXML
    private RadioButton viewMonthRadio;

    @FXML
    private ComboBox<Users> userPicker;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Adds all users to userlist for obtaining appointments by user.
        userPicker.setItems(userList);

        // Sets current user to logged in user.
        for (int i = 0; i < userList.size(); i++) {
            Users currentUser = userList.get(i);
            if (currentUser.getUserId() == Login.getUserId()) {
                chosenUser = currentUser;
                userPicker.setValue(currentUser);
            }
        }

        // Returns all appointments matching the logged in user ID.
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        appointmentTable.setItems(appointmentList);
        appointmentTable.getSortOrder().add(startColumn);

        // Checks for appointment within 15 minutes of login.
        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment appointmentBeingChecked = appointmentList.get(i);
            LocalDateTime appointmentTime = appointmentBeingChecked.getStartZDT().toLocalDateTime();

            // If there is an appointment within 15 minutes of the Login time an alert pops up.
            if (Login.getLoginTime().isBefore(appointmentTime) && Login.getLoginTime().isAfter(appointmentTime.minusMinutes(15))) {
                // Returns a string in 12 hour time format with AM or PM
                String appointmentStringTime = Time.amOrPm(appointmentBeingChecked.getStartZDT().toLocalTime());

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initModality(Modality.NONE);
                alert.setTitle("Appointment");
                alert.setHeaderText("Appointment");
                alert.setContentText("Appointment with " + appointmentBeingChecked.getCustomerName() +
                        "\nat " + appointmentStringTime);
                alert.showAndWait();
                return;
            }
        }

        // Listener for toggle values.
        // Provides functionality to view appointments by week, month, or all.
        appointmentView.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (userPicker.getSelectionModel().getSelectedItem() != null) {
                    chosenUser = userPicker.getSelectionModel().getSelectedItem();
                }

                if (appointmentView.getSelectedToggle() != null) {
                    if (viewAllRadio.isSelected()) {
                        appointmentList = Appointment.returnAllAppointments(chosenUser.getUserId());
                    } else if (viewWeekRadio.isSelected()) {
                        appointmentList = Appointment.returnWeekAppointments(chosenUser.getUserId());
                    } else if (viewMonthRadio.isSelected()) {
                        appointmentList = Appointment.returnMonthAppointments(chosenUser.getUserId());
                    }

                    appointmentTable.setItems(appointmentList);
                    appointmentTable.getSortOrder().add(startColumn);
                }
            }
        });

    }

    // Refreshes appointment table
    void refreshTable() {
        if (userPicker.getSelectionModel().getSelectedItem() != null) {
            chosenUser = userPicker.getSelectionModel().getSelectedItem();
        }

        if (viewAllRadio.isSelected()) {
            appointmentList = Appointment.returnAllAppointments(chosenUser.getUserId());
        } else if (viewWeekRadio.isSelected()) {
            appointmentList = Appointment.returnWeekAppointments(chosenUser.getUserId());
        } else if (viewMonthRadio.isSelected()) {
            appointmentList = Appointment.returnMonthAppointments(chosenUser.getUserId());
        }
        appointmentTable.setItems(appointmentList);
        appointmentTable.getSortOrder().add(startColumn);

    }

    // Opens popup window to add appointment
    // Uses lambda expression to refresh table on hide or close.
    @FXML
    void addAptAction(ActionEvent event) {
        // Opens create appointment window.
        CreateAppointmentController.createAppointment = true;
        Node source = (Node) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateAppointmentScreen.fxml"));
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
        stage.setOnCloseRequest(event1 -> refreshTable());
        stage.setOnHiding(event1 -> refreshTable());

    }

    // Opens popup window to del appointment
    @FXML
    void delAptAction(ActionEvent event) {
        if (appointmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No appointment selected");
            alert.showAndWait();
            return;
        }

        // Gets selected appointment
        Appointment appointmentToDel = appointmentTable.getSelectionModel().getSelectedItem();

        // Confirms user wants to delete the appointment.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Value returning lambda expression
        Message message = s -> "Are you sure you want to delete the appointment with \r" + s + "?\r";

        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText("Confirm deletion.");
        alert.setContentText(message.getMessage(appointmentToDel.getCustomerName()));
        Optional<ButtonType> result = alert.showAndWait();

        // If user confirms, appointment will be deleted, and the table refreshed.
        if (result.get() == ButtonType.OK) {
            Appointment.deleteAppointment(appointmentToDel.getAppointmentId());
            refreshTable();
        }
        return;
    }

    // Opens popup window to edit appointment
    // Uses lambda expression to refresh table on hide or close.
    @FXML
    void editAptAction(ActionEvent event) {
        if (appointmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No customer selected");
            alert.showAndWait();
            return;
        }

        // Gets selected customer
        appointmentToEdit = appointmentTable.getSelectionModel().getSelectedItem();

        // Opens customer window.
        CreateAppointmentController.createAppointment = false;
        Node source = (Node) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateAppointmentScreen.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            ;
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
        stage.setOnCloseRequest(event1 -> refreshTable());
        stage.setOnHiding(event1 -> refreshTable());
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
        if (Login.userLogout()) {
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


    @FXML
    void updateByUser(ActionEvent event) {
        refreshTable();

    }

    @FXML
    void uniqueReportAction(ActionEvent event) {
        if (userPicker.getSelectionModel().getSelectedItem() != null) {
            chosenUser = userPicker.getSelectionModel().getSelectedItem();
        }

        LocalDateTime timeNow = LocalDateTime.now();
        int month = timeNow.getMonthValue();
        int year = timeNow.getYear();
        int uniqueType = Appointment.returnUniqueType(chosenUser.getUserId(), month, year);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Report");
        alert.setHeaderText("Unique Type Report");
        alert.setContentText("User " + chosenUser.getUsername() + " has " + uniqueType + " unique types of appointments this month.");
        alert.showAndWait();
        return;
    }

    @FXML
    void totalReportAction(ActionEvent event) {
        if (userPicker.getSelectionModel().getSelectedItem() != null) {
            chosenUser = userPicker.getSelectionModel().getSelectedItem();
        }
        int uniqueTotal = Appointment.returnUniqueTotal(chosenUser.getUserId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Report");
        alert.setHeaderText("Unique Total Report");
        alert.setContentText("User " + chosenUser.getUsername() + " has " + uniqueTotal + " total appointments.");
        alert.showAndWait();
        return;
    }

}