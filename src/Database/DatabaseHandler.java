package Database;


import Model.Bill;
import Model.User;
import org.h2.tools.RunScript;

import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseHandler {

    private static DatabaseHandler instance;

    private static Connection connection;
    private String DB_URL = "jdbc:h2:/" + System.getProperty("user.dir");
    private String DB_DRIVER = "org.h2.Driver";

    private DatabaseHandler() {
        System.out.println("Database constructor");
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL + File.separator + "Base");
            RunScript.execute(connection, new FileReader("dataBase/script.sql"));
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("БД Подключена");
            System.err.println(ex.getMessage());
        } catch (Exception e) {
            System.out.println("Не удалось подключиться к базе данных");
            System.err.println(e.getMessage());
        }
    }

    public static DatabaseHandler getDataBase() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    //-------------------------------------------User-------------------------------------------------------------------
    public User getUser(String login, String password) {
        User user = null;
        String dbRequest = "SELECT * FROM user WHERE login=? AND password=?";
        try {
            PreparedStatement statement = connection.prepareStatement(dbRequest);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10)
                );
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка при подключении к базе данных!");
            ex.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() {
        String dbRequest = "SELECT * FROM `User`";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement prepStat = connection.prepareStatement(dbRequest);
            ResultSet resultSet = prepStat.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String login = resultSet.getString(2);
                String password = resultSet.getString(3);
                String firstName = resultSet.getString(4);
                String surName = resultSet.getString(5);
                String fatherName = resultSet.getString(6);
                String city = resultSet.getString(7);
                String street = resultSet.getString(8);
                String house = resultSet.getString(9);
                int flat = resultSet.getInt(10);

                User user = new User(userId, login, password, firstName, surName, fatherName, city, street, house, flat);
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка при подключении к базе данных!");
            ex.printStackTrace();
        }
        return users;
    }

    public void deleteUser(int id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM `User` where id = ?")) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public int addUser(String login, String password, String firstName, String surname, String fatherName,
                       String city, String street, String house, int flat) {
        int id = -1;
        String[] generatedColumns = {"Id"};
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `USER` (`LOGIN`, `PASSWORD`, `NAME`, `SURNAME`, `FATHERNAME`, `CITY`, `STREET`, `HOUSE`, `FLAT`) " +
                        "VALUES(?,?,?,?,?,?,?,?,?)", generatedColumns
        )) {
            System.out.println("Data base addUser");
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, firstName);
            statement.setString(4, surname);
            statement.setString(5, fatherName);
            statement.setString(6, city);
            statement.setString(7, street);
            statement.setString(8, house);
            statement.setInt(9, flat);
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return id;
    }

    public void updateUser(int id, String login, String password) {

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE `User` " +
                        "SET `Login`=?, `Password`=? " +
                        "WHERE `Id`=?"
        )) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setInt(3, id);
            statement.execute();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
    }

    //-------------------------------------------Bill-------------------------------------------------------------------
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Bill`")) {
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                bills.add(new Bill(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getDate(4)
                ));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return bills;
    }

    public int addBill(int userId, int indication, Date paymentDate) {
        int id = -1;
        String[] generatedColumns = {"Id"};
        try (final PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `Bill`(`UserId`, `Indication`, `PaymentDate`) " +
                        "values (?, ?, ?)"
        )) {
            statement.setInt(1, userId);
            statement.setInt(2, indication);
            statement.setDate(3, paymentDate);
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return id;
    }

    public List<Bill> getUserBills(int userId) {
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Bill` WHERE `UserId` = ?")) {
            statement.setInt(1, userId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                bills.add(new Bill(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getDate(4)
                ));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return bills;
    }
}
