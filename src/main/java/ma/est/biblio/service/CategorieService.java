package ma.est.biblio.service;

import ma.est.biblio.dao.CategorieDAO;
import ma.est.biblio.model.Categorie;

import java.util.List;
import java.util.stream.Collectors;

public class CategorieService {

    private final CategorieDAO categorieDAO;

    public CategorieService() {
        this.categorieDAO = new CategorieDAO();
    }
    public Categorie ajouterCategorie(Categorie categorie) {

        if (categorie == null || categorie.getLibelle() == null || categorie.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé de la catégorie est obligatoire.");
        }
        if (categorieDAO.findByLibelle(categorie.getLibelle()) != null) {
            throw new IllegalArgumentException("Une catégorie avec ce libellé existe déjà.");
        }

        return categorieDAO.save(categorie);
    }
    public boolean modifierCategorie(Categorie categorie) {

        if (categorie == null || categorie.getId() <= 0) {
            return false;
        }

        return categorieDAO.update(categorie);
    }

    public boolean supprimerCategorie(int id) {

        if (id <= 0) {
            return false;
        }

        return categorieDAO.delete(id);
    }

    public Categorie getCategorieById(int id) {
        return categorieDAO.findById(id);
    }

    public Categorie getCategorieByLibelle(String libelle) {

        if (libelle == null || libelle.trim().isEmpty()) {
            return null;
        }

        return categorieDAO.findByLibelle(libelle);
    }

    public List<Categorie> getAllCategories() {
        return categorieDAO.findAll();
    }

    public List<Categorie> rechercherCategories(String libelle) {

        if (libelle == null || libelle.trim().isEmpty()) {
            return getAllCategories();
        }

        return categorieDAO.findAll()
                .stream()
                .filter(c -> c.getLibelle() != null &&
                        c.getLibelle().toLowerCase().contains(libelle.toLowerCase()))
                .collect(Collectors.toList());
    }
}
