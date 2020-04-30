package Model;

import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class Appointment {
    int appointmentId;
    int customerId;
    String title;
    String customerName;
    String type;
    String description;
    String contact;
    String start;
    String end;
    ZonedDateTime startZDT;
    ZonedDateTime endZDT;

    public Appointment(int appointmentId, int customerId, String title, String customerName, String type, String description,
                       String contact, String start, String end, ZonedDateTime startZDT, ZonedDateTime endZDT) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.customerName = customerName;
        this.type = type;
        this.description = description;
        this.contact = contact;
        this.start = start;
        this.end = end;
        this.startZDT = startZDT;
        this.endZDT = endZDT;
    }

    public static ObservableList<Appointment> returnAllAppointments(int userId) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT a.userId, a.appointmentId, c.customerId, a.title, c.customerName, " +
                "a.type, a.description, a.contact, a.start, a.end  " +
                "FROM appointment AS a " +
                "INNER JOIN customer AS c ON a.customerId = c.customerId " +
                "WHERE userId = " + userId;
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());


        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int dbAppointmentId = resultSet.getInt("appointmentId");
                int dbCustomerId = resultSet.getInt("customerId");
                String dbTitle = resultSet.getString("title");
                String dbCustomerName = resultSet.getString("customerName");
                String dbType = resultSet.getString("type");
                String dbDescription = resultSet.getString("description");
                String dbContact = resultSet.getString("contact");
                LocalDate dbStartDate = resultSet.getDate("start").toLocalDate();
                LocalTime dbStartTime = resultSet.getTime("start").toLocalTime();
                LocalDate dbEndDate = resultSet.getDate("end").toLocalDate();
                LocalTime dbEndTime = resultSet.getTime("end").toLocalTime();

                // Creates zone date time of start time in utc and converts it to the users local time.
                ZonedDateTime utcStartZDT = ZonedDateTime.of(dbStartDate, dbStartTime, ZoneId.of("UTC"));
                ZonedDateTime localStartZDT = utcStartZDT.withZoneSameInstant(localZoneId);

                // Creates zone date time of end time in utc and converts it to the users local time.
                ZonedDateTime utcEndZDT = ZonedDateTime.of(dbEndDate, dbEndTime, ZoneId.of("UTC"));
                ZonedDateTime localEndZDT = utcEndZDT.withZoneSameInstant(localZoneId);


                String dbStart = localStartZDT.toLocalDate().toString() + " " + Time.amOrPm(localStartZDT.toLocalTime());
                String dbEnd = localEndZDT.toLocalDate().toString() + " " + Time.amOrPm(localEndZDT.toLocalTime());

                appointmentList.add(new Appointment(dbAppointmentId, dbCustomerId, dbTitle,
                        dbCustomerName, dbType, dbDescription, dbContact,
                        dbStart, dbEnd, localStartZDT, localEndZDT));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> returnWeekAppointments(int userId) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT a.userId, a.appointmentId, c.customerId, a.title, c.customerName, " +
                "a.type, a.description, a.contact, a.start, a.end  " +
                "FROM appointment AS a " +
                "INNER JOIN customer AS c ON a.customerId = c.customerId " +
                "WHERE userId = " + userId;
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());


        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int dbAppointmentId = resultSet.getInt("appointmentId");
                int dbCustomerId = resultSet.getInt("customerId");
                String dbTitle = resultSet.getString("title");
                String dbCustomerName = resultSet.getString("customerName");
                String dbType = resultSet.getString("type");
                String dbDescription = resultSet.getString("description");
                String dbContact = resultSet.getString("contact");
                LocalDate dbStartDate = resultSet.getDate("start").toLocalDate();
                LocalTime dbStartTime = resultSet.getTime("start").toLocalTime();
                LocalDate dbEndDate = resultSet.getDate("end").toLocalDate();
                LocalTime dbEndTime = resultSet.getTime("end").toLocalTime();

                // Creates zone date time of start time in utc and converts it to the users local time.
                ZonedDateTime utcStartZDT = ZonedDateTime.of(dbStartDate, dbStartTime, ZoneId.of("UTC"));
                ZonedDateTime localStartZDT = utcStartZDT.withZoneSameInstant(localZoneId);

                // Creates zone date time of end time in utc and converts it to the users local time.
                ZonedDateTime utcEndZDT = ZonedDateTime.of(dbEndDate, dbEndTime, ZoneId.of("UTC"));
                ZonedDateTime localEndZDT = utcEndZDT.withZoneSameInstant(localZoneId);


                String dbStart = localStartZDT.toLocalDate().toString() + " " + Time.amOrPm(localStartZDT.toLocalTime());
                String dbEnd = localEndZDT.toLocalDate().toString() + " " + Time.amOrPm(localEndZDT.toLocalTime());

                LocalDate loginDate = Login.getLoginTime().toLocalDate();
                LocalDate appointmentBeginDate = utcStartZDT.toLocalDate();
                boolean withinSevenDays = false;

                // If the login date is on or after the appointment date minus 7 days
                if (loginDate.equals(appointmentBeginDate.minusDays(7)) ||
                        loginDate.isAfter(appointmentBeginDate.minusDays(7))) {
                    // and the appointment begin date is within 7 days of the appointment
                    if (appointmentBeginDate.isAfter(loginDate) || appointmentBeginDate.equals(loginDate)) {
                        withinSevenDays = true;
                    } else {
                        withinSevenDays = false;
                    }
                } else {
                    withinSevenDays = false;
                }

                if (withinSevenDays) {
                    appointmentList.add(new Appointment(dbAppointmentId, dbCustomerId, dbTitle,
                            dbCustomerName, dbType, dbDescription, dbContact,
                            dbStart, dbEnd, localStartZDT, localEndZDT));
                }
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> returnMonthAppointments(int userId) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT a.userId, a.appointmentId, c.customerId, a.title, c.customerName, " +
                "a.type, a.description, a.contact, a.start, a.end  " +
                "FROM appointment AS a " +
                "INNER JOIN customer AS c ON a.customerId = c.customerId " +
                "WHERE userId = " + userId;
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());


        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int dbAppointmentId = resultSet.getInt("appointmentId");
                int dbCustomerId = resultSet.getInt("customerId");
                String dbTitle = resultSet.getString("title");
                String dbCustomerName = resultSet.getString("customerName");
                String dbType = resultSet.getString("type");
                String dbDescription = resultSet.getString("description");
                String dbContact = resultSet.getString("contact");
                LocalDate dbStartDate = resultSet.getDate("start").toLocalDate();
                LocalTime dbStartTime = resultSet.getTime("start").toLocalTime();
                LocalDate dbEndDate = resultSet.getDate("end").toLocalDate();
                LocalTime dbEndTime = resultSet.getTime("end").toLocalTime();

                // Creates zone date time of start time in utc and converts it to the users local time.
                ZonedDateTime utcStartZDT = ZonedDateTime.of(dbStartDate, dbStartTime, ZoneId.of("UTC"));
                ZonedDateTime localStartZDT = utcStartZDT.withZoneSameInstant(localZoneId);

                // Creates zone date time of end time in utc and converts it to the users local time.
                ZonedDateTime utcEndZDT = ZonedDateTime.of(dbEndDate, dbEndTime, ZoneId.of("UTC"));
                ZonedDateTime localEndZDT = utcEndZDT.withZoneSameInstant(localZoneId);


                String dbStart = localStartZDT.toLocalDate().toString() + " " + Time.amOrPm(localStartZDT.toLocalTime());
                String dbEnd = localEndZDT.toLocalDate().toString() + " " + Time.amOrPm(localEndZDT.toLocalTime());

                LocalDate loginDate = Login.getLoginTime().toLocalDate();
                LocalDate appointmentBeginDate = utcStartZDT.toLocalDate();
                boolean withinMonth = false;

                // If the login date is on or after the appointment date minus 7 days
                if (loginDate.getMonth().equals(appointmentBeginDate.getMonth())) {
                    withinMonth = true;
                }


                if (withinMonth) {
                    appointmentList.add(new Appointment(dbAppointmentId, dbCustomerId, dbTitle,
                            dbCustomerName, dbType, dbDescription, dbContact,
                            dbStart, dbEnd, localStartZDT, localEndZDT));
                }
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return appointmentList;
    }

    public static int returnUniqueType(int userId, int month, int year) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT Count(DISTINCT type) AS DistinctType FROM appointment " +
                "WHERE userId = " + userId + " AND MONTH(start) = " + month + " AND YEAR(start) = " + year;
        int uniqueType = 0;

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                uniqueType = resultSet.getInt("DistinctType");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return uniqueType;

    }

    public static int returnUniqueTotal(int userId) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT Count(*) AS total FROM appointment " +
                "WHERE userId = " + userId;
        int uniqueTotal = 0;

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                uniqueTotal = resultSet.getInt("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return uniqueTotal;

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

    public ZonedDateTime getStartZDT() {
        return startZDT;
    }

    public void setStartZDT(ZonedDateTime startZDT) {
        this.startZDT = startZDT;
    }

    public ZonedDateTime getEndZDT() {
        return endZDT;
    }

    public void setEndZDT(ZonedDateTime endZDT) {
        this.endZDT = endZDT;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}