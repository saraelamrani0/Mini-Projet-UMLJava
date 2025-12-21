package ma.est.biblio.dao;

import ma.est.biblio.model.Livre;
import ma.est.biblio.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    public Livre save(Livre livre) {
        String sql = "INSERT INTO livre (isbn, titre, auteur, editeur, annee_publication, categorie_id, quantite_totale, quantite_disponible) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, livre.getIsbn());
            ps.setString(2, livre.getTitre());
            ps.setString(3, livre.getAuteur());
            ps.setString(4, livre.getEditeur());
            ps.setInt(5, livre.getAnneePublication());
            ps.setInt(6, livre.getCategorieId());
            ps.setInt(7, livre.getQuantiteTotale());
            ps.setInt(8, livre.getQuantiteDisponible());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    livre.setId(rs.getInt(1));
                }
            }
            return livre;

        } catch (SQLException e) {
            System.err.println("❌ Erreur LivreDAO.save : " + e.getMessage());
            return null;
        }
    }
    public List<Livre> findAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre ORDER BY titre";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(map(rs));
            }

        } catch (SQLException e) {
            System.err.println(" Erreur LivreDAO.findAll : " + e.getMessage());
        }
        return livres;
    }
    public Livre findByIsbn(String isbn) {
        String sql = "SELECT * FROM livre WHERE isbn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println(" Erreur LivreDAO.findByIsbn : " + e.getMessage());
        }
        return null;
    }

    
    public boolean update(Livre livre) {
        String sql = "UPDATE livre SET titre=?, auteur=?, editeur=?, annee_publication=?, categorie_id=?, quantite_totale=?, quantite_disponible=? "
                   + "WHERE isbn=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getEditeur());
            ps.setInt(4, livre.getAnneePublication());
            ps.setInt(5, livre.getCategorieId());
            ps.setInt(6, livre.getQuantiteTotale());
            ps.setInt(7, livre.getQuantiteDisponible());
            ps.setString(8, livre.getIsbn());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur LivreDAO.update : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String isbn) {
        String sql = "DELETE FROM livre WHERE isbn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur LivreDAO.delete : " + e.getMessage());
            return false;
        }
    }
    public void decrementerStock(String isbn) {
        String sql = "UPDATE livre SET quantite_disponible = quantite_disponible - 1 "
                   + "WHERE isbn = ? AND quantite_disponible > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void incrementerStock(String isbn) {
        String sql = "UPDATE livre SET quantite_disponible = quantite_disponible + 1 "
                   + "WHERE isbn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private Livre map(ResultSet rs) throws SQLException {
        Livre l = new Livre();
        l.setId(rs.getInt("id"));
        l.setIsbn(rs.getString("isbn"));
        l.setTitre(rs.getString("titre"));
        l.setAuteur(rs.getString("auteur"));
        l.setEditeur(rs.getString("editeur"));
        l.setAnneePublication(rs.getInt("annee_publication"));
        l.setCategorieId(rs.getInt("categorie_id"));
        l.setQuantiteTotale(rs.getInt("quantite_totale"));
        l.setQuantiteDisponible(rs.getInt("quantite_disponible"));
        return l;
    }
}
