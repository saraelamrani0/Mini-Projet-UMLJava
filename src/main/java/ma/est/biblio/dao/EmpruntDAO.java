package ma.est.biblio.dao;

import ma.est.biblio.model.Emprunt;
import ma.est.biblio.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

    public Emprunt save(Emprunt emprunt) {
        if (emprunt == null) return null;

        String sql = "INSERT INTO emprunt (personne_id, livre_id, date_emprunt, date_retour_prevue, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, emprunt.getAdherentId());
            ps.setInt(2, emprunt.getLivreId());
            ps.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));
            ps.setDate(4, new java.sql.Date(emprunt.getDateRetourPrevue().getTime()));
            ps.setString(5, emprunt.getStatut());

            int affectedRows = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emprunt.setId(rs.getInt(1));
                }
            }
            return emprunt;
        } catch (SQLException e) {
            System.err.println(" Erreur EmpruntDAO.save : " + e.getMessage());
            return null;
        }
    }

    public Emprunt findById(int id) {
        String sql = "SELECT * FROM emprunt WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToEmprunt(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println(" Erreur EmpruntDAO.findById : " + e.getMessage());
        }
        return null;
    }

    public boolean updateRetour(int empruntId, String nouveauStatut) {
        String sql = "UPDATE emprunt SET date_retour_effectif = CURRENT_DATE, statut = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nouveauStatut);
            ps.setInt(2, empruntId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Erreur EmpruntDAO.updateRetour : " + e.getMessage());
            return false;
        }
    }

    public int countActifsByAdherent(int adherentId) {
        String sql = "SELECT COUNT(*) FROM emprunt WHERE personne_id = ? AND date_retour_effectif IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adherentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Emprunt> findAll() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt ORDER BY date_emprunt DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(mapToEmprunt(rs));
            }
        } catch (SQLException e) {
            System.err.println(" Erreur EmpruntDAO.findAll : " + e.getMessage());
        }
        return emprunts;
    }

    private Emprunt mapToEmprunt(ResultSet rs) throws SQLException {
        Emprunt e = new Emprunt();
        e.setId(rs.getInt("id"));
        e.setAdherentId(rs.getInt("personne_id"));
        e.setLivreId(rs.getInt("livre_id"));
        e.setDateEmprunt(rs.getDate("date_emprunt"));
        e.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
        
        Date dateEff = rs.getDate("date_retour_effectif");
        if (!rs.wasNull()) {
            e.setDateRetourEffective(dateEff);
        }
        
        e.setStatut(rs.getString("statut"));
        return e;
    }
}
