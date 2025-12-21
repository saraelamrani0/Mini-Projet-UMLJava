package ma.est.biblio.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_bibliotheque" +
                                     "?useSSL=false" +
                                     "&serverTimezone=UTC" +
                                     "&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "SARAELAMRANI1234__";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur: Driver MySQL non trouvé");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Connexion à la base de données réussie !");
            
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT DATABASE() as db, USER() as user");
            if (rs.next()) {
                System.out.println("Base: " + rs.getString("db"));
                System.out.println("Utilisateur MySQL: " + rs.getString("user"));
            }
            
        } catch (SQLException e) {
            System.out.println("Échec de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        testConnection();
    }
}