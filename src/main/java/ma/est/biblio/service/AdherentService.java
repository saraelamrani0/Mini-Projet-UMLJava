package ma.est.biblio.service;

import ma.est.biblio.dao.AdherentDAO;
import ma.est.biblio.model.Adherent;
import java.util.List;

public class AdherentService {

    private final AdherentDAO adherentDAO;

    public AdherentService() {
        this.adherentDAO = new AdherentDAO();
    }

    public void inscrireAdherent(Adherent a) {
        if (a == null || a.getEmail() == null || a.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Données de l'adhérent invalides (Email requis)");
        }
        adherentDAO.save(a);
    }

    public List<Adherent> getAllAdherents() {
        return adherentDAO.findAll();
    }

    public boolean peutEmprunter(int adherentId) {
        // L'adhérent peut emprunter s'il n'est PAS bloqué
        return !adherentDAO.checkStatus(adherentId);
    }
}
