package ma.est.biblio.model;

import java.util.Date;
import java.util.Calendar;

public class Emprunt {

    private int id;
    private int adherentId;      
    private int livreId;         
    private Date dateEmprunt;
    private Date dateRetourPrevue;
    private Date dateRetourEffective;
    private String statut;

   
    public static final String STATUT_EN_COURS = "EN_COURS";
    public static final String STATUT_RETOURNE = "RETOURNE";  
    public static final String STATUT_EN_RETARD = "EN_RETARD";  

    public Emprunt() {
        this.dateEmprunt = new Date();
        this.dateRetourPrevue = calculDateRetour(this.dateEmprunt);
        this.statut = STATUT_EN_COURS;
    }

    public Emprunt(int adherentId, int livreId) {
        this(); 
        this.adherentId = adherentId;
        this.livreId = livreId;
    }

    private Date calculDateRetour(Date dateEmprunt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateEmprunt);
        cal.add(Calendar.DAY_OF_MONTH, 14); 
        return cal.getTime();
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAdherentId() { return adherentId; }
    public void setAdherentId(int adherentId) { this.adherentId = adherentId; }

    public int getLivreId() { return livreId; }
    public void setLivreId(int livreId) { this.livreId = livreId; }

    public Date getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(Date dateEmprunt) { this.dateEmprunt = dateEmprunt; }

    public Date getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(Date dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }

    public Date getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(Date dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public boolean estEnRetard() {
        if (dateRetourEffective != null) return false;
        return new Date().after(dateRetourPrevue);
    }

    @Override
    public String toString() {
        return "Emprunt #" + id + " [Adhérent ID: " + adherentId + ", Livre ID: " + livreId + "]";
    }
}