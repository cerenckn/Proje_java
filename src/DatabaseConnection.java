import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.SQLEcxeption;

import java.sql.*;

public class DatabaseConnection {
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/user_management";
        String dbUser = "root"; 
        String dbPassword = "*Crn123.*"; 

        try {
            // Bağlantıyı oluştur
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            System.out.println("Veritabanına başarılı bir şekilde bağlanıldı.");
            return conn;
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // Test bağlantısı
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            System.out.println("Bağlantı başarılı!");
        } else {
            System.out.println("Bağlantı başarısız!");
        }
    }

	
}
