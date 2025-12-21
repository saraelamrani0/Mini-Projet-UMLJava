package ma.est.biblio.service;

import ma.est.biblio.dao.LivreDAO;
import ma.est.biblio.dao.CategorieDAO;
import ma.est.biblio.model.Livre;
import java.util.List;
import java.util.stream.Collectors;

public class LivreService {

    private final LivreDAO livreDAO;
    private final CategorieDAO categorieDAO;

    public LivreService() {
        this.livreDAO = new LivreDAO();
        this.categorieDAO = new CategorieDAO();
    }

    public Livre ajouterLivre(Livre livre) {
        if (livreDAO.findByIsbn(livre.getIsbn()) != null) {
            throw new IllegalArgumentException("Un livre avec cet ISBN existe déjà.");
        }
        
        if (livre.getQuantiteTotale() < 0) {
            throw new IllegalArgumentException("La quantité totale ne peut pas être négative.");
        }

        livre.setQuantiteDisponible(livre.getQuantiteTotale());
        
        return livreDAO.save(livre);
    }
	
    public boolean modifierLivre(Livre livre) {
        Livre existant = livreDAO.findByIsbn(livre.getIsbn());
        if (existant == null) {
            return false;
        }
        int livresEmpruntes = existant.getQuantiteTotale() - existant.getQuantiteDisponible();
        
        if (livre.getQuantiteTotale() < livresEmpruntes) {
            throw new IllegalArgumentException("Impossible de réduire le stock : " + livresEmpruntes + " livres sont actuellement empruntés.");
        }

        livre.setQuantiteDisponible(livre.getQuantiteTotale() - livresEmpruntes);

        return livreDAO.update(livre);
    }

    public boolean supprimerLivre(String isbn) {
        Livre livre = livreDAO.findByIsbn(isbn);
        if (livre != null && livre.getQuantiteDisponible() < livre.getQuantiteTotale()) {
            throw new IllegalStateException("Impossible de supprimer ce livre car des exemplaires sont encore empruntés.");
        }
        return livreDAO.delete(isbn);
    }

    public Livre getLivreByIsbn(String isbn) {
        return livreDAO.findByIsbn(isbn);
    }

    public List<Livre> getAllLivres() {
        return livreDAO.findAll();
    }
    public List<Livre> rechercherLivres(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            return getAllLivres();
        }
        
        String lowerTerme = terme.toLowerCase();
        return livreDAO.findAll().stream()
                .filter(l -> l.getTitre().toLowerCase().contains(lowerTerme) || 
                             l.getAuteur().toLowerCase().contains(lowerTerme) ||
                             l.getIsbn().contains(terme))
                .collect(Collectors.toList());
    }

    public boolean estDisponible(String isbn) {
        Livre livre = livreDAO.findByIsbn(isbn);
        return livre != null && livre.getQuantiteDisponible() > 0;
    }

    public void emprunterLivre(String isbn) {
        if (estDisponible(isbn)) {
            livreDAO.decrementerStock(isbn);
        } else {
            throw new IllegalStateException("Le livre n'est plus disponible.");
        }
    }

    public void retournerLivre(String isbn) {
        Livre livre = livreDAO.findByIsbn(isbn);
        if (livre != null && livre.getQuantiteDisponible() < livre.getQuantiteTotale()) {
            livreDAO.incrementerStock(isbn);
        }
    }
}
