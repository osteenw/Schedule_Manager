package Model;

import Utils.DBQuery;

import java.sql.SQLException;
import java.sql.Statement;

public class NewUser {
    private String userName;
    private String password;
    private int active = 0;
    private String createDate = "CONVERT(NOW(), CHAR)";
    private String lastUpdate = "CONVERT(NOW(), CHAR)";
    private String createdBy;
    private String lastUpdateBy;

    public NewUser(String userName, String password, String createdBy, String lastUpdateBy) {
        this.userName = userName;
        this.password = password;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }

    public void createNewUser() {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "INSERT INTO user(userName, password, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                "VALUES('" + this.userName + "', "
                + "'" + this.password + "'" + ", " +
                + this.active + ", "
                + this.createDate + ", '"
                + this.createdBy + "', "
                + this.lastUpdate + ", '"
                + this.lastUpdateBy + "')";

//        System.out.println(insertStatement); // test for insert statement.

        try {
            statement.execute(insertStatement);
        } catch (SQLException throwables) {
            System.out.println("SQL Execute error.");
            throwables.printStackTrace();
        }
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

}
