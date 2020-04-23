package View_Controller;

import Model.Login;
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

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Stage stage;
    @FXML    private Hyperlink createUser;
    @FXML    private TextField usernameField;
    @FXML    private PasswordField passwordField;
    @FXML    private Label errorLabel;
    @FXML    private Label errorLabel2;
    @FXML    private Button loginButton;
    @FXML    private Button exitButton;
    Locale english = new Locale("en", "US");
    Locale spanish = new Locale("es", "ES");
    Locale french = new Locale("fr", "FR");
    private ResourceBundle rb;



    // Clears error labels of any text.
    // Determines user location.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setText("");
        errorLabel2.setText("");

        try {
            // Locale.setDefault(spanish); // Sets language to Spanish. Used for testing resource bundles.
            rb = ResourceBundle.getBundle("Resources/Nat", Locale.getDefault());
        } catch (Exception e) {
            Locale.setDefault(english);
            rb = ResourceBundle.getBundle("Resources/Nat", Locale.getDefault());
            System.out.println(e.getMessage());
        }
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
        String username = usernameField.getText();
        String password = passwordField.getText();


        // Error message in local language (or english if not supported) for blank username or password fields.
        if (username.isEmpty()) {
            errorLabel.setText(rb.getString("Username") + " " +
                    rb.getString("cannot") + " " +
                    rb.getString("be") + " " +
                    rb.getString("blank") + ".");
            return;
        } else if (password.isEmpty()) {
            errorLabel.setText(rb.getString("Password") + " " +
                    rb.getString("cannot") + " " +
                    rb.getString("be") + " " +
                    rb.getString("blank") + ".");
            return;
        }

        // Calls query to check login credentials. If there is a match the username and id will be stored
        // as a static variable, and the boolean will return true.
        boolean loginValid = Login.userLogin(username, password);

        // Launches main appointment window.
        // if loginValid is true, a user match was found.
        if (loginValid) {
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
        } else {
            // Error message if no results returned matching username and password.
            // Error message in local language, or english if not supported.
            errorLabel.setText(rb.getString("Username") + " " +
                    rb.getString("or") + " " +
                    rb.getString("password") + " " +
                    rb.getString("invalid") + ".");
            return;
        }
    }
}