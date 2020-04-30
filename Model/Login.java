package Model;

import Utils.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

public class Login {
    private static int userId;
    private static String username;
    private static LocalDateTime loginTime;

    public static void setActive(boolean active) {
        Statement statement = DBQuery.getStatement();
        int userActive;
        if (active) {
            userActive = 1;
        } else {
            userActive = 0;
        }
        String updateStatement = "UPDATE user SET active = " + userActive +
                " WHERE userId = " + userId;

        try {
            statement.execute(updateStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

    }

    public static boolean userLogin(String username, String password) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT * FROM user WHERE userName = '" +
                username + "' AND password = '" + password + "'";

        // SQL query for username and password
        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        // Obtains results from username and password query
        try {
            ResultSet results = statement.getResultSet();


            // Forward scrolls result set looking for user match.
            while (results.next()) {

                int dbUserId = results.getInt("userId");
                String dbUserName = results.getString("userName");

                if (dbUserName.equals(username)) {
                    String dbPassword = results.getString("password");

                    // Launches main appointment window.
                    // if username and password both match, user was found.
                    // User credentials will be sent to static login class for reference.

                    if (dbPassword.equals(password)) {
                        setUserId(dbUserId);
                        setUsername(dbUserName);
                        setActive(true);
                        setLoginTime(LocalDateTime.now());
                        return true;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean userLogout() {
        //Confirms user wants to log out.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initModality(Modality.NONE);
        alert.setTitle("Log Off");
        alert.setHeaderText("Confirm log out.");
        alert.setContentText("Are you sure you want to log off?\r");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            // Sets user to inactive.
            Login.setActive(false);
            return true;
        }
        return false;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Login.userId = userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Login.username = username;
    }

    public static LocalDateTime getLoginTime() {
        return loginTime;
    }

    public static void setLoginTime(LocalDateTime loginTime) {
        Login.loginTime = loginTime;
    }
}
