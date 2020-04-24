package View_Controller;

import Model.Customer;
import Model.Login;
import Utils.DBQuery;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateCustomerController extends CustomerController implements Initializable {
    public Stage stage;
    public static boolean createCustomer;

    @FXML
    private Label titleLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField address2Field;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalcodeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField phoneField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (createCustomer) {
            titleLabel.setText("Create Customer");
        } else {
            titleLabel.setText("Modify Customer");
            nameField.setText(customerToEdit.getCustomerName());
            addressField.setText(customerToEdit.getAddress());
            address2Field.setText(customerToEdit.getAddress2());
            cityField.setText(customerToEdit.getCity());
            postalcodeField.setText(customerToEdit.getPostalCode());
            countryField.setText(customerToEdit.getCountry());
            phoneField.setText(customerToEdit.getPhone());
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
    void save(ActionEvent event) throws SQLException {
        boolean parseError = false;

        String customerName = nameField.getText();
        String address = addressField.getText();
        String address2 = address2Field.getText();
        String city = cityField.getText();
        String postalcode = postalcodeField.getText();
        String country = countryField.getText();
        String phone = phoneField.getText();

        if (customerName.isEmpty() == true || address.isEmpty() == true || city.isEmpty() == true ||
                postalcode.isEmpty() || country.isEmpty() || phone.isEmpty()) {
            parseError = true;
        }

        if (parseError) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Required field is blank.");
            alert.showAndWait();
            return;
        }


        if (createCustomer) {
            int countryId = Customer.lookupCountry(country); // Looks up country, and creates if it does not exist
            int cityId = Customer.lookupCity(city, countryId); // Looks up city, and creates if it does not exist
            int addressId = Customer.lookupAddress(address); // Looks up address for address id
            if (addressId == 0) {
                Customer.createAddress(address, address2, postalcode, phone, cityId); // Creates address
                addressId = Customer.lookupAddress(address);
            }

            Customer.createCustomer(customerName, addressId); // Adds customer to DB

            // Closes Window
            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();
            return;
        }

        // Updates customer
        if (!createCustomer) {
            Statement statement = DBQuery.getStatement();
            int countryId;
            int cityId;
            int addressId;
            int customerId = customerToEdit.getCustomerId();

            // If country is different, gets a country id to update the city. Checks to see if city is different as well.
            if (!customerToEdit.getCountry().equals(country)) {
                countryId = Customer.lookupCountry(country); // Looks up country and creates if it does not exist;
                if (customerToEdit.getCity().equals(city)) {
                    cityId = customerToEdit.getCityId();
                    String insertStatement = "UPDATE city SET countryId = " + countryId + ", lastUpdate = " +
                            "CONVERT(NOW(), CHAR), lastUpdateBy = '" + Login.getUsername() + "' " + " WHERE cityId = " + cityId;
                    statement.execute(insertStatement);
                } else {
                    cityId = Customer.lookupCity(city, countryId);
                }

            }

            // country is the same
            else {
                countryId = customerToEdit.getCountryId();
            }

            // if city has changed, gets new city id and checks to see if address1 is the same and updates the city id there.
            if (!customerToEdit.getCity().equals(city)) { // city is different
                cityId = Customer.lookupCity(city, countryId); // return city id for the new city
                if (customerToEdit.getAddress().equals(address)) { // if the address is the same, update the city id for the address.
                    addressId = customerToEdit.getAddressId();
                    String insertStatement = "UPDATE address SET cityId = " + cityId + ", address = '" + address +"', address2 = '" + address2 + "', postalCode = '" +
                            postalcode + "', phone = '" + phone + "', lastUpdate = CONVERT(NOW(), CHAR), lastUpdateBy = '" + Login.getUsername() + "' " +
                            "WHERE addressId = " + addressId;
                    statement.execute(insertStatement);
                } else { // city is different and so is the address... need to make new address.
                    Customer.createAddress(address, address2, postalcode, phone, cityId); // Creates address
                    addressId = Customer.lookupAddress(address);
                }

            }

            // country is the same, and city is the same. updates address.
            else {
                cityId = customerToEdit.getCityId();
                addressId = customerToEdit.getAddressId();

                String insertStatement = "UPDATE address SET cityId = " + cityId + ", address = '" + address + "', address2 = '" + address2 +
                        "', postalCode = '" + postalcode + "', phone = '" + phone + "', lastUpdate = " +
                        "CONVERT(NOW(), CHAR), lastUpdateBy = '" + Login.getUsername() + "' " +
                        "WHERE addressId = " + addressId;
                statement.execute(insertStatement);
            }

            // updates customer
            String insertStatement = "UPDATE customer " +
                    "SET customerName = '" + customerName + "', addressId = " + addressId + ", lastUpdate = " +
                    "CONVERT(NOW(), CHAR), lastUpdateBy = '" + Login.getUsername() + "'" +
                    "WHERE customerId = " + customerId;

            statement.execute(insertStatement);

            // Closes Window
            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();
            return;
        }


    }

}

