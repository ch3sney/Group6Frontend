import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("Unable to load SQLite JDBC driver.");
            }
            connection = DriverManager.getConnection("jdbc:sqlite:db/EquipmentRenting.db");
        }
        return connection;
    }
}
