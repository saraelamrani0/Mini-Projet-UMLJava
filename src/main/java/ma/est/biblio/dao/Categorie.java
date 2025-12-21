package ma.est.biblio.dao;

import ma.est.biblio.model.Categorie;
import ma.est.biblio.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    public Categorie save(Categorie categorie) {

        if (categorie == null || categorie.getLibelle() == null || categorie.getLibelle().trim().isEmpty()) {
            return null;
        }

        String sql = "INSERT INTO categorie (libelle) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categorie.getLibelle().trim());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categorie.setId(rs.getInt(1));
                }
            }

            return categorie;

        } catch (SQLException e) {
            System.err.println("Erreur CategorieDAO.save : " + e.getMessage());
            return null;
        }
    }
    public Categorie findById(int id) {

        if (id <= 0) {
            return null;
        }

        String sql = "SELECT * FROM categorie WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToCategorie(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(" Erreur CategorieDAO.findById : " + e.getMessage());
        }

        return null;
    }

    public List<Categorie> findAll() {

        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie ORDER BY libelle";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(mapToCategorie(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur CategorieDAO.findAll : " + e.getMessage());
        }

        return categories;
    }

    public boolean update(Categorie categorie) {

        if (categorie == null || categorie.getId() <= 0 ||
            categorie.getLibelle() == null || categorie.getLibelle().trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE categorie SET libelle=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categorie.getLibelle().trim());
            stmt.setInt(2, categorie.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(" Erreur CategorieDAO.update : " + e.getMessage());
            return false;
        }
    }
    public boolean delete(int id) {

        if (id <= 0) {
            return false;
        }

        String sql = "DELETE FROM categorie WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(" Erreur CategorieDAO.delete : " + e.getMessage());
            return false;
        }
    }

    public Categorie findByLibelle(String libelle) {

        if (libelle == null || libelle.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM categorie WHERE libelle=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libelle.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToCategorie(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(" Erreur CategorieDAO.findByLibelle : " + e.getMessage());
        }

        return null;
    }
    private Categorie mapToCategorie(ResultSet rs) throws SQLException {
        return new Categorie(
                rs.getInt("id"),
                rs.getString("libelle")
        );
    }
}
