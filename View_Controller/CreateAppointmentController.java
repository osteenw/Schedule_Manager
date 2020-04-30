package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.Login;
import Model.Time;
import Utils.DBQuery;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class CreateAppointmentController extends AppointmentController implements Initializable {
    public Stage stage;
    ObservableList<Customer> customerList = Customer.returnAllCustomers();
    ObservableList<Time> timePicker = Time.returnTime();
    public static boolean createAppointment;


    @FXML
    private Label titleLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField titleField;
    @FXML
    private TextField typeField;
    @FXML
    private ComboBox<Customer> customerPicker;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Time> startTime;
    @FXML
    private ComboBox<Time> endTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerPicker.setItems(customerList);
        startTime.setItems(timePicker);
        endTime.setItems(timePicker);

        if (!createAppointment) {
            titleLabel.setText("Update Appointment");
            titleField.setText(appointmentToEdit.getTitle());
            typeField.setText(appointmentToEdit.getType());
            datePicker.setValue(appointmentToEdit.getStartZDT().toLocalDate());

            for (int i = 0; i < customerList.size(); i++) {
                Customer currentCustomer = customerList.get(i);
                if (appointmentToEdit.getCustomerId() == currentCustomer.getCustomerId()) {
                    customerPicker.setValue(currentCustomer);
                }
            }

        }
    }

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
        boolean parseError = false;
        boolean timeError = false;
        Statement statement = DBQuery.getStatement();

        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ObservableList<Appointment> appointmentList = Appointment.returnAllAppointments(Login.getUserId());
        Customer selectedCustomer = customerPicker.getSelectionModel().getSelectedItem();
        Time selectedStartTime = startTime.getSelectionModel().getSelectedItem();
        Time selectedEndTime = endTime.getSelectionModel().getSelectedItem();

        String title = titleField.getText();
        String type = typeField.getText();
        String currentUser = Login.getUsername();
        String currentDate = "CONVERT(NOW(), CHAR)";

        LocalDate dateFromPicker;
        LocalTime startTime;
        LocalTime endTime;

        // Checks for null values
        if (selectedCustomer == null || selectedStartTime == null ||
                selectedEndTime == null || title.isEmpty() || type.isEmpty() || datePicker.getValue() == null) {
            parseError = true;
        }

        // Checks if times are invalid.
        if(!parseError) {
            if (selectedEndTime.getHour() < selectedStartTime.getHour() ||
                    ((selectedEndTime.getHour() == selectedStartTime.getHour()) && (selectedEndTime.getMinutes() <= selectedStartTime.getMinutes()))) {
                timeError = true;
            }
        }

        if (parseError || timeError) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");

            if (selectedCustomer == null) {
                alert.setContentText("No customer selected.");
            } else if (selectedStartTime == null) {
                alert.setContentText("No start time selected");
            } else if (selectedEndTime == null) {
                alert.setContentText("No end time selected");
            } else if (timeError) {
                alert.setContentText("End time must be greater than the start time.");
            } else {
                alert.setContentText("All fields must be filled.");
            }
            alert.showAndWait();
            return;
        }

        // Gets selected values from form
        dateFromPicker = datePicker.getValue();
        startTime = LocalTime.of(selectedStartTime.getHour(), selectedStartTime.getMinutes());
        endTime = LocalTime.of(selectedEndTime.getHour(), selectedEndTime.getMinutes());

        // Creates ZDT objects in local date time
        ZonedDateTime startLocalZDT = ZonedDateTime.of(dateFromPicker, startTime, localZoneId);
        ZonedDateTime endLocalZDT = ZonedDateTime.of(dateFromPicker, endTime, localZoneId);

        // Converts ZDT objects to UTC date time
        ZonedDateTime startUTC_ZDT = startLocalZDT.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC_ZDT = endLocalZDT.withZoneSameInstant(ZoneId.of("UTC"));

        // Creates strings for SQL insert of ZDT
        String sqlStartUTC = startUTC_ZDT.toLocalDate().toString() + " " + startUTC_ZDT.toLocalTime().toString() + ":00";
        String sqlEndUTC = endUTC_ZDT.toLocalDate().toString() + " " + endUTC_ZDT.toLocalTime().toString() + ":00";

        // Checks for time overlap
        boolean timeOverlap = false;
        for (int i = 0; i < appointmentList.size(); i++) {
            boolean differentAppointment = true;
            Appointment currentAppointment = appointmentList.get(i);
            LocalDateTime ltStartTime = startLocalZDT.toLocalDateTime();
            LocalDateTime ltEndTime = endLocalZDT.toLocalDateTime();
            LocalDateTime ltAptStartTime = currentAppointment.getStartZDT().toLocalDateTime();
            LocalDateTime ltAptEndTime = currentAppointment.getEndZDT().toLocalDateTime();

            // If updating an appointment, checks to make sure the appointment being checked for a time overlap
            // isn't the same appointment that is being edited.
            if(!createAppointment) {
                if(currentAppointment.getAppointmentId() == appointmentToEdit.getAppointmentId()) {
                    differentAppointment = false;
                } else {
                    differentAppointment = true;
                }
            } else {
                differentAppointment = true;
            }

            // If the start time is between the start and end time
            if (ltStartTime.isAfter(ltAptStartTime) && ltStartTime.isBefore(ltAptEndTime) && differentAppointment) {
                timeOverlap = true;
            }

            // If the start time is the same as the start time or
            // If the end time is the same as the end time
            if ((ltStartTime.isEqual(ltAptStartTime) || ltEndTime.isEqual(ltAptEndTime)) && differentAppointment) {
                timeOverlap = true;
            }

            // If the end time is after the start time and before the end time
            if (ltEndTime.isAfter(ltAptStartTime) && ltEndTime.isBefore(ltAptEndTime) && differentAppointment) {
                timeOverlap = true;
            }

            // If the start time is before the start time and end the end time is after the end time
            if (ltStartTime.isBefore(ltAptStartTime) && ltEndTime.isAfter(ltAptEndTime) && differentAppointment) {
                timeOverlap = true;
            }

            // If there is a time overlap and if we are updating an appointment the overlap is from a different appointment
            // a warning pops up with the conflicting appointment.
            if (timeOverlap && differentAppointment) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initModality(Modality.NONE);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Appointment overlaps with appointment:\n " +
                        "Title: " + currentAppointment.getTitle() +
                        "\n Customer: " + currentAppointment.getCustomerName() +
                        "\n Start Time: " + ltAptStartTime.toLocalTime().toString() + " End Time: " + ltAptEndTime.toLocalTime().toString() + "\n");
                alert.showAndWait();
                return;
            }

            // Checks if appointment is outside business hours.
            if ((ltEndTime.getHour() > 17) || (ltStartTime.getHour() < 8)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initModality(Modality.NONE);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Appointment not within business hours. ");
                alert.showAndWait();
                return;
            }

        }

        // Creates appointment
        if (createAppointment) {
            // Insert statement for adding a new appointment.
            String insertStatement = "INSERT INTO appointment(customerId, userId, title, description, location, " +
                    "contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES(" + selectedCustomer.getCustomerId() + ", " + Login.getUserId() +
                    ", '" + title + "', 'not needed', 'not needed', 'not needed', '" + type + "', 'not needed', '" + sqlStartUTC + "', '" + sqlEndUTC + "', " +
                    currentDate + ", '" + currentUser + "', " + currentDate + ", '" + currentUser + "')";
            try {
                statement.execute(insertStatement);
            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
                ;
            }
        }

        // Updates appointment
        if (!createAppointment) {
            // Insert statement for updating appointment
            int appointmentId = appointmentToEdit.getAppointmentId();
            String updateStatement = "UPDATE appointment " +
                    "SET title = '" + title + "', customerId = " + selectedCustomer.getCustomerId() +
                    ", type = '" + type + "', start = '" + sqlStartUTC + "', end = '" + sqlEndUTC +
                    "', lastUpdate = " + currentDate + ", lastUpdateBy = '" + currentUser + "' " +
                    "WHERE appointmentId = " + appointmentId;

            try {
                statement.execute(updateStatement);
            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
                ;
            }

        }

        // Closes Window
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
        return;


    }
}