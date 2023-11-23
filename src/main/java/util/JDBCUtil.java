package util;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@UtilityClass
public class JDBCUtil {



    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection =  DriverManager.getConnection(
                    "jdbc:sqlite:C://Users//Сан//IdeaProjects//currency-exchange-api//Database.sqlite");
        } catch (SQLException e) {
            System.out.println("Подключение к базе данных не выполнено");
            throw new RuntimeException(e);
        }
        return connection;
    }




}
