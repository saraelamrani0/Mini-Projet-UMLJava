package ma.est.biblio.service;

import ma.est.biblio.dao.EmpruntDAO;
import ma.est.biblio.model.Emprunt;
import ma.est.biblio.model.Livre;
import java.util.Date;
import java.util.List;

public class EmpruntService {

    private final EmpruntDAO empruntDAO;
    private final LivreService livreService;
    private final AdherentService adherentService;

    public EmpruntService() {
        this.empruntDAO = new EmpruntDAO();
        this.livreService = new LivreService();
        this.adherentService = new AdherentService();
    }

    public Emprunt emprunterLivre(int adherentId, String isbn) {
        if (!adherentService.peutEmprunter(adherentId)) {
            throw new IllegalStateException("L'adhérent n'est pas autorisé à emprunter.");
        }

        Livre livre = livreService.getLivreByIsbn(isbn);
        if (livre == null || livre.getQuantiteDisponible() <= 0) {
            throw new IllegalStateException("Livre indisponible.");
        }

        Emprunt emprunt = new Emprunt(adherentId, livre.getId());
        Emprunt saved = empruntDAO.save(emprunt);

        if (saved != null) {
            livreService.emprunterLivre(isbn);
            return saved;
        }
        throw new RuntimeException("Erreur lors de l'enregistrement.");
    }

    public boolean retournerLivre(int empruntId) {
        Emprunt emprunt = empruntDAO.findById(empruntId);
        
        if (emprunt == null) throw new IllegalArgumentException("Emprunt introuvable.");
        if (emprunt.getDateRetourEffective() != null) throw new IllegalStateException("Livre déjà rendu.");


        boolean success = empruntDAO.updateRetour(empruntId, Emprunt.STATUT_RETOURNE);

        if (success) {
            Livre livre = livreService.getAllLivres().stream()
                            .filter(l -> l.getId() == emprunt.getLivreId())
                            .findFirst().orElse(null);
            
            if (livre != null) {
                livreService.retournerLivre(livre.getIsbn());
            }
            return true;
        }
        return false;
    }

    public List<Emprunt> getTousLesEmprunts() {
        return empruntDAO.findAll();
    }
}
