package util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class JDBCUtil {
    private final static String DRIVER_NAME_KEY = "db.driver";
    private static final String db = "Database.sqlite";
    Connection connection = null;

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesUtil.get(DRIVER_NAME_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection(){

            try {
                URL resource  = JDBCUtil.class.getClassLoader().getResource(db);
                assert resource != null;
                String path = "jdbc:sqlite:" + new File(resource.toURI()).getAbsolutePath();

                connection = DriverManager.getConnection(path);
            } catch (SQLException e) {
                System.out.println("Подключение к базе данных не выполнено");
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        return connection;
    }
}
