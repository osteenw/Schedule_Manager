package Main;

import Model.Login;
import Utils.DBConnection;
import Utils.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/LoginScreen.fxml"));
        primaryStage.setTitle("Schedule Manager");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
//        DBConnection.startConnection();
        Connection conn = DBConnection.startConnection(); // Connect to database
        DBQuery.setStatement(conn); // Create statement
        Statement statement = DBQuery.getStatement(); // Get statement reference

        launch(args);
        DBConnection.closeConnection();
        statement.close();
    }
}
