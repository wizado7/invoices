package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("Не удалось найти файл конфигурации db.properties");
            }
            properties.load(input);

            String driver = properties.getProperty("db.driver");
            if (driver != null) {
                Class.forName(driver);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка при инициализации ConnectionManager", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, username, password);
    }
}
