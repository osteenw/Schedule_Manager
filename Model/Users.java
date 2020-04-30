package Model;

import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class Users {
    int userId;
    String username;

    public Users(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static ObservableList<Users> returnAllUsers() {
        ObservableList<Users> userlist = FXCollections.observableArrayList();
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT * FROM user";

        // SQL query for username and password
        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        // Obtains results from username and password query
        try {
            ResultSet resultSet = statement.getResultSet();

            // Forward scrolls results
            while (resultSet.next()) {
                int dbUserId = resultSet.getInt("userId");
                String dbUserName = resultSet.getString("userName");

                userlist.add(new Users(dbUserId, dbUserName));
                }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return userlist;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return (username);
    }
}
