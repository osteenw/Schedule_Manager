package Utils;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class DBConnection {

    // JDBC URL Parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U06avA";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver Interface reference
    private static final String MYSQL_JDBC_Driver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U06avA"; // Username
    private static final String password = "53688712522"; // Password

    public static Connection startConnection() {
        try {
            Class.forName(MYSQL_JDBC_Driver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection succesful.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.out.println("Error closing DB connection: " + e.getMessage());
        }
    }

}
