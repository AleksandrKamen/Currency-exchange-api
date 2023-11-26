package util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


@UtilityClass
public class JDBCUtil {
    private final static String POLL_SIZE_KEY = "poll.size";
    private final static Integer POLL_DEFAULT_SIZE = 10;
    private static BlockingQueue<Connection> poll;            // Коллекция для прокси соединений
    private static List<Connection> sourceConnections;      // Коллекция для оригиналов соединений



    static {
        loadDriver();
        initConnectionPool();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection get(){              // Получаем соединение из нашего пула
        try {
            return poll.take();
        } catch (InterruptedException e) {
            System.out.println("Неудалось получить соединение");
            throw new RuntimeException(e);
        }
    }

private static void initConnectionPool(){
    var size = PropertiesUtil.get(POLL_SIZE_KEY);
    var poolSize = size == null?POLL_DEFAULT_SIZE:Integer.parseInt(size);
    poll = new ArrayBlockingQueue<>(poolSize);
    sourceConnections = new ArrayList<>(poolSize);
    for (int i = 0; i <poolSize; i++) {
    Connection connection = getConnection();
        var proxyConnection = (Connection) Proxy.newProxyInstance(JDBCUtil.class.getClassLoader(), new Class[]{Connection.class}, // создаем proxy объект для того чтобы при закрытии соединения оно возвращалось обратно в пулл
                (proxy, method, args) -> method.getName().equals("close")
                        ? poll.add(connection)
                        : method.invoke(connection, args));
poll.add(proxyConnection);
sourceConnections.add(connection);
        
    }

}

    private static Connection getConnection(){
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
