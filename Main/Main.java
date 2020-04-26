package Main;

import Model.Login;
import Utils.DBConnection;
import Utils.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

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

        Login.setActive(false); // Sets logged in user to inactive on application close.
        DBConnection.closeConnection();
        statement.close();

    }
}
