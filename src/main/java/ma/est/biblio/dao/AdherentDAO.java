package ma.est.biblio.dao;

import ma.est.biblio.model.Adherent;
import ma.est.biblio.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdherentDAO {

    public void save(Adherent a) {
        String sql = "INSERT INTO personne (nom, prenom, email, telephone, bloque, type, login, password, role) VALUES (?, ?, ?, ?, ?, 'adherent', ?, ?, ?)";
        
        // DEBUG : Afficher la requête et valeurs
        System.out.println("=== DEBUG INSERT ===");
        System.out.println("SQL : " + sql);
        System.out.println("Nom: " + a.getNom() + ", Prenom: " + a.getPrenom() + ", Email: " + a.getEmail());
        System.out.println("Telephone: " + a.getTelephone() + ", Bloque: " + a.isBloque());
        System.out.println("Login: " + ((a.getLogin() == null || a.getLogin().isEmpty()) ? a.getEmail() : a.getLogin()));
        System.out.println("Password: " + ((a.getPassword() == null) ? "1234" : "[HACHÉ]"));  // Ne pas afficher en clair !
        System.out.println("Role: " + ((a.getRole() == null) ? "ADHERENT" : a.getRole()));
        System.out.println("====================");
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, a.getNom());
            ps.setString(2, a.getPrenom());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getTelephone());
            ps.setBoolean(5, a.isBloque());
            
            ps.setString(6, (a.getLogin() == null || a.getLogin().isEmpty()) ? a.getEmail() : a.getLogin());
            ps.setString(7, (a.getPassword() == null) ? "1234" : a.getPassword());
            ps.setString(8, (a.getRole() == null) ? "ADHERENT" : a.getRole());

            int affectedRows = ps.executeUpdate();
            System.out.println("Lignes affectées : " + affectedRows);  // Doit être 1
            
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        a.setId(generatedId);
                        System.out.println("ID généré : " + generatedId + " (vérifiez SELECT id=" + generatedId + " en DB)");
                    }
                }
            } else {
                System.out.println("ERREUR : 0 ligne insérée ! Vérifiez contraintes (ex. email unique ?)");
                throw new SQLException("Échec de l'insertion : aucune ligne ajoutée.");
            }
            
            // DEBUG : Vérifier auto-commit
            System.out.println("Auto-commit activé ? " + c.getAutoCommit());
            if (!c.getAutoCommit()) {
                c.commit();  // Force commit si désactivé
                System.out.println("Commit forcé exécuté.");
            }
            
        } catch (SQLException e) {
            System.out.println("ERREUR SQL DÉTAILLÉE : " + e.getMessage());
            System.out.println("Code erreur SQL : " + e.getErrorCode());
            e.printStackTrace();
            throw new RuntimeException("Erreur Base de données : " + e.getMessage(), e);
        }
    }

    public List<Adherent> findAll() {
        List<Adherent> list = new ArrayList<>();
        // On s'assure que le type correspond exactement à celui du INSERT
        String sql = "SELECT * FROM personne WHERE type = 'adherent' ORDER BY id DESC";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Adherent a = new Adherent();
                a.setId(rs.getInt("id"));
                a.setNom(rs.getString("nom"));
                a.setPrenom(rs.getString("prenom"));
                a.setEmail(rs.getString("email"));
                a.setTelephone(rs.getString("telephone"));
                a.setBloque(rs.getBoolean("bloque"))
                list.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération : " + e.getMessage());
        }
        return list;
    }

    public boolean checkStatus(int id) {
        String sql = "SELECT bloque FROM personne WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean("bloque");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
}
