package Database;

import Configurations.DatabaseConfig;
import Model.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class DatabaseHandler extends DatabaseConfig {

    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }

    public ResultSet getUser(User user){
        ResultSet resSet = null;
        String dbRequest = "SELECT * FROM user WHERE login=? AND password=?";
        try{
            PreparedStatement prepStat = getDbConnection().prepareStatement(dbRequest);
            prepStat.setString(1, user.getLogin());
            prepStat.setString(2, user.getPassword());
            resSet = prepStat.executeQuery();
        }catch(SQLException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex){
            System.out.println("Ошибка при подключении к базе данных!");
            ex.printStackTrace();
        }
        return resSet;
    }
}
