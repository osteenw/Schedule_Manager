package Model;

import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {
    static String createDate = "CONVERT(NOW(), CHAR)";
    static String lastUpdate = "CONVERT(NOW(), CHAR)";

    int customerId;
    int addressId;
    int cityId;
    int countryId;
    String customerName;
    String address;
    String address2;
    String city;
    String postalCode;
    String country;
    String phone;

    public Customer(int customerId, int addressId, int cityId, int countryId, String customerName, String address,
                    String address2, String city, String postalCode, String country,
                    String phone) {
        this.customerId = customerId;
        this.addressId = addressId;
        this.cityId = cityId;
        this.countryId = countryId;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
    }

    public static ObservableList<Customer> returnAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT c.customerId, a.addressId, city.cityId, country.countryId, c.customerName, a.address, a.address2, " +
                "city.city, a.postalcode, country.country, a.phone " +
                "FROM customer AS c " +
                "INNER JOIN address AS a ON a.addressId = c.addressId " +
                "INNER JOIN city ON city.cityId = a.cityId " +
                "INNER JOIN country ON country.countryId = city.countryId";

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int dbCustomerId = resultSet.getInt("customerId");
                int dbAddressId = resultSet.getInt("addressId");
                int dbCityId = resultSet.getInt("cityId");
                int dbCountryId = resultSet.getInt("countryId");
                String dbCustomerName = resultSet.getString("customerName");
                String dbAddress = resultSet.getString("address");
                String dbAddress2 = resultSet.getString("address2");
                String dbCity = resultSet.getString("city");
                String dbPostalcode = resultSet.getString("postalcode");
                String dbCountry = resultSet.getString("country");
                String dbPhone = resultSet.getString("phone");

                customerList.add(new Customer(dbCustomerId, dbAddressId, dbCityId, dbCountryId, dbCustomerName, dbAddress,
                        dbAddress2, dbCity, dbPostalcode, dbCountry, dbPhone));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return customerList;

    }

    // Checks DB to see if a country exists.
    public static int lookupCountry(String country) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT * FROM country WHERE country = '" + country + "'";
        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int countryId = resultSet.getInt("countryId");

                return countryId;
            }
            if (!resultSet.next()) {
                createCountry(country);
                lookupCountry(country);
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return 0;

    }

    // Creates country
    public static void createCountry(String country) {
        String createdBy = Login.getUsername();
        String lastUpdateBy = Login.getUsername();

        Statement statement = DBQuery.getStatement();
        String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdate, lastUpdateBy ) " +
                "VALUES('" + country + "', " +
                createDate + ", " +
                "'" + createdBy + "', " +
                lastUpdate + ", " +
                "'" + lastUpdateBy + "')";

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

    }

    // Checks DB to see if a city exists.
    public static int lookupCity(String city, int countryId) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT * FROM city WHERE city = '" + city + "'";
        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int cityId = resultSet.getInt("cityId");

                return cityId;
            }
            if (!resultSet.next()) {
                createCity(city, countryId);
                lookupCity(city, countryId);
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return 0;

    }

    // Creates city
    public static void createCity(String city, int countryId) {
        String createdBy = Login.getUsername();
        String lastUpdateBy = Login.getUsername();

        Statement statement = DBQuery.getStatement();
        String insertStatement = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy ) " +
                "VALUES('" + city + "', " +
                countryId + ", " +
                createDate + ", " +
                "'" + createdBy + "', " +
                lastUpdate + ", " +
                "'" + lastUpdateBy + "')";

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

    }

    // Checks DB to see if an address exists.
    public static int lookupAddress(String address) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT * FROM address WHERE address = '" + address + "'";
        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int addressId = resultSet.getInt("addressId");
                return addressId;
            }
            if (!resultSet.next()) {
                return 0;
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return 0;

    }

    // Create Address
    public static void createAddress(String address, String address2, String postalCode, String phone, int cityId) {
        System.out.println("Creating address");
        System.out.println("City ID = " + cityId);

        String createdBy = Login.getUsername();
        String lastUpdateBy = Login.getUsername();

        Statement statement = DBQuery.getStatement();
        String insertStatement = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy ) " +
                "VALUES('" + address + "', " +
                "'" + address2 + "', " +
                cityId + ", " +
                "'" + postalCode + "', " +
                "'" + phone + "', " +
                createDate + ", " +
                "'" + createdBy + "', " +
                lastUpdate + ", " +
                "'" + lastUpdateBy + "')";

        System.out.println(insertStatement);

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

    }

    // Creates customer
    public static void createCustomer(String customerName, int addressId) {
        System.out.println("Creating customer");
        System.out.println("Address ID = " + addressId);

        String createdBy = Login.getUsername();
        String lastUpdateBy = Login.getUsername();

        Statement statement = DBQuery.getStatement();
        String insertStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy ) " +
                "VALUES('" + customerName + "', " +
                addressId + ", " +
                1 + ", " +
                createDate + ", " +
                "'" + createdBy + "', " +
                lastUpdate + ", " +
                "'" + lastUpdateBy + "')";

        System.out.println(insertStatement);

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

    }


    // Deletes customer
    public static void deleteCustomer(int customerId, int addressId) {
        Statement statement = DBQuery.getStatement();
        String deleteStatement1 = "DELETE FROM appointment WHERE customerId = " + customerId;
        String deleteStatement2 = "DELETE FROM customer WHERE customerId = " + customerId;
        String deleteStatement3 = "DELETE FROM address WHERE addressId = " + addressId;

        try {
            statement.execute(deleteStatement1);
            statement.execute(deleteStatement2);
            statement.execute(deleteStatement3);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
