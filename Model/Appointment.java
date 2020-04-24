package Model;

import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Appointment {
    int appointmentId;
    String title;
    String customerName;
    String type;
    String description;
    String contact;
    String start;
    String end;

    public Appointment(int appointmentId, String title, String customerName, String type, String description, String contact, String start, String end) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.customerName = customerName;
        this.type = type;
        this.description = description;
        this.contact = contact;
        this.start = start;
        this.end = end;
    }

    public static ObservableList<Appointment> returnAllAppointments() {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT a.userId, a.appointmentId, a.title, c.customerName, " +
                "a.type, a.description, a.contact, a.start, a.end  " +
                "FROM appointment AS a " +
                "INNER JOIN customer AS c ON a.customerId = c.customerId " +
                "WHERE userId = " + Login.getUserId();
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int dbAppointmentId = resultSet.getInt("appointmentId");
                String dbTitle = resultSet.getString("title");
                String dbCustomerName = resultSet.getString("customerName");
                String dbType = resultSet.getString("type");
                String dbDescription = resultSet.getString("description");
                String dbContact = resultSet.getString("contact");
                String dbStart = resultSet.getString("start");
                String dbEnd = resultSet.getString("end");

                appointmentList.add(new Appointment(dbAppointmentId, dbTitle,
                        dbCustomerName, dbType, dbDescription, dbContact,
                        dbStart, dbEnd));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return appointmentList;
    }

    public static void deleteAppointment(int appointmentId) {
        Statement statement = DBQuery.getStatement();
        String deleteStatement = "DELETE FROM appointment WHERE appointmentId = " + appointmentId;
        try {
            statement.execute(deleteStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
