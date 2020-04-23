package View_Controller;

import Model.Login;
import Utils.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    public Stage stage;

    @FXML
    private Hyperlink createUser;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label errorLabel2;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;

    // Clears error labels of any text.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setText("");
        errorLabel2.setText("");
    }

    // Calls window to create a new user.
    @FXML
    void createUserWindow(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateUserScreen.fxml"));
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
    }

    // Closes application
    @FXML
    void exitApplication(ActionEvent event) {
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    // Logs user into program. Default username is 'test', default password is 'test'.
    @FXML
    void loginAction(ActionEvent event) {
        Statement statement = DBQuery.getStatement();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String insertStatement = "SELECT * FROM user WHERE userName = '" +
                username + "' AND password = '" + password + "'";

        // Error message in English and Spanish for blank username or password fields.
        if(username.isEmpty()) {
            errorLabel.setText("Username can not be blank.");
            errorLabel2.setText("Nombre de usuario no puede estar en blanco.");
            return;
        } else if(password.isEmpty()) {
            errorLabel.setText("Password can not be blank.");
            errorLabel2.setText("La contraseña no puede estar en blanco.");
            return;
        }

        // Console output for testing SQL insert statement
//        System.out.println(insertStatement);

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
                while(results.next()) {

                    int dbUserId = results.getInt("userId");
                    String dbUserName = results.getString("userName");

                    if(dbUserName.equals(username)) {
                        String dbPassword = results.getString("password");

                        // Launches main appointment window.
                        // if username and password both match, user was found.
                        // User credentials will be sent to static login class for reference.

                        if(dbPassword.equals(password)) {
                            Login.setUserId(dbUserId);
                            Login.setUsername(dbUserName);

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
                            exitApplication(event);
                            return;
                        }
                    }
                }

            // Error message if no results returned matching username and password.
            // Error message in both English and Spanish.
            if(!results.next()) {
                errorLabel.setText("Username or password invalid");
                errorLabel2.setText("Nombre de usuario o contraseña inválidos.");
                return;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }

}