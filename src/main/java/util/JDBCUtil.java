package util;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@UtilityClass
public class JDBCUtil {
    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection =  DriverManager.getConnection(
                    "jdbc:sqlite:Database.sqlite");
        } catch (SQLException e) {
            System.out.println("Подключение не выполнено");
            throw new RuntimeException(e);
        }
        return connection;
    }




}
