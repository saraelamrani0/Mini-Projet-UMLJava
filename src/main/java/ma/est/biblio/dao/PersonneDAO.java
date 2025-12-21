package ma.est.biblio.dao;

import ma.est.biblio.model.Personne;
import ma.est.biblio.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneDAO {

    public Personne save(Personne personne) {
        if (personne == null) return null;

        String query = "INSERT INTO personne (nom, prenom, email, telephone, adresse, role, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, personne.getNom());
            stmt.setString(2, personne.getPrenom());
            stmt.setString(3, personne.getEmail());
            stmt.setString(4, personne.getTelephone());
            stmt.setString(5, personne.getAdresse());
            stmt.setString(6, personne.getRole());
            stmt.setString(7, personne.getPassword());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    personne.setId(rs.getInt(1));
                }
            }
            return personne;

        } catch (SQLException e) {
            System.err.println(" Erreur PersonneDAO.save: " + e.getMessage());
            return null;
        }
    }

    public Personne findById(int id) {
        String query = "SELECT * FROM personne WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur PersonneDAO.findById: " + e.getMessage());
        }
        return null;
    }

    public Personne findByLogin(String login) {
        if (login == null || login.isEmpty()) return null;

        String query = "SELECT * FROM personne WHERE login = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur PersonneDAO.findByLogin: " + e.getMessage());
        }
        return null;
    }

    public Personne findByEmail(String email) {
        if (email == null || email.isEmpty()) return null;

        String query = "SELECT * FROM personne WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur PersonneDAO.findByEmail: " + e.getMessage());
        }
        return null;
    }
    public List<Personne> findAll() {
        List<Personne> personnes = new ArrayList<>();
        String query = "SELECT * FROM personne ORDER BY nom, prenom";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                personnes.add(map(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur PersonneDAO.findAll: " + e.getMessage());
        }
        return personnes;
    }

    public List<Personne> findByRole(String role) {
        List<Personne> personnes = new ArrayList<>();
        if (role == null || role.isEmpty()) return personnes;

        String query = "SELECT * FROM personne WHERE role = ? ORDER BY nom, prenom";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    personnes.add(map(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println(" Erreur PersonneDAO.findByRole: " + e.getMessage());
        }
        return personnes;
    }
    public List<Personne> rechercherParNom(String nom) {
        List<Personne> personnes = new ArrayList<>();
        if (nom == null || nom.isEmpty()) return personnes;

        String query = "SELECT * FROM personne WHERE nom LIKE ? OR prenom LIKE ? ORDER BY nom, prenom";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String search = "%" + nom + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    personnes.add(map(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur PersonneDAO.rechercherParNom: " + e.getMessage());
        }
        return personnes;
    }

    // ================= UPDATE =================
    public boolean update(Personne personne) {
        if (personne == null || personne.getId() <= 0) return false;

        String query = "UPDATE personne SET nom = ?, prenom = ?, email = ?, telephone = ?, adresse = ?, role = ?, password = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, personne.getNom());
            stmt.setString(2, personne.getPrenom());
            stmt.setString(3, personne.getEmail());
            stmt.setString(4, personne.getTelephone());
            stmt.setString(5, personne.getAdresse());
            stmt.setString(6, personne.getRole());
            stmt.setString(7, personne.getPassword());
            stmt.setInt(8, personne.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur PersonneDAO.update: " + e.getMessage());
            return false;
        }
    }
    public boolean delete(int id) {
        if (id <= 0) return false;

        String query = "DELETE FROM personne WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur PersonneDAO.delete: " + e.getMessage());
            return false;
        }
    }
    public boolean emailExists(String email, Integer excludeId) {
        if (email == null || email.isEmpty()) return false;

        String query = (excludeId != null)
                ? "SELECT COUNT(*) FROM personne WHERE email = ? AND id != ?"
                : "SELECT COUNT(*) FROM personne WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            if (excludeId != null) {
                stmt.setInt(2, excludeId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur PersonneDAO.emailExists: " + e.getMessage());
        }
        return false;
    }
    private Personne map(ResultSet rs) throws SQLException {
        Personne personne = new Personne();
        personne.setId(rs.getInt("id"));
        personne.setNom(rs.getString("nom"));
        personne.setPrenom(rs.getString("prenom"));
        personne.setEmail(rs.getString("email"));
        personne.setTelephone(rs.getString("telephone"));
        personne.setAdresse(rs.getString("adresse"));
        personne.setRole(rs.getString("role")); // <- corrigé
        personne.setPassword(rs.getString("password"));
        return personne;
    }
}
