package Database;

import Configurations.DatabaseConfig;
import Model.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends DatabaseConfig {

    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;
        String dbRequest = "SELECT * FROM user WHERE login=? AND password=?";
        try{
            PreparedStatement prepStat = getDbConnection().prepareStatement(dbRequest);
            prepStat.setString(1, user.getLogin());
            prepStat.setString(2, user.getPassword());
            resSet = prepStat.executeQuery();
        }catch(SQLException | ClassNotFoundException | NoSuchMethodException
                | InvocationTargetException | InstantiationException | IllegalAccessException ex){
            System.out.println("Ошибка при подключении к базе данных!");
            ex.printStackTrace();
        }
        return resSet;
    }

    public List<User> getAllUsers() {
        String dbRequest = "SELECT * FROM user";
        List<User> users = new ArrayList<>();

        try {
            PreparedStatement prepStat = getDbConnection().prepareStatement(dbRequest);
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

                User user = new User(userId, login, password, firstName, surName, fatherName, city, street, flat, house);
                users.add(user);
            }

        } catch(SQLException | ClassNotFoundException | NoSuchMethodException
                | InvocationTargetException | InstantiationException | IllegalAccessException ex){
            System.out.println("Ошибка при подключении к базе данных!");
            ex.printStackTrace();
        }
        return users;
    }
}
