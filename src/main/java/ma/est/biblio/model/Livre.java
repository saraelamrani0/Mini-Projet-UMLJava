package ma.est.biblio.model;

public class Livre {

    private int id;
    private String isbn;
    private String titre;
    private String auteur;
    private String editeur;
    private int anneePublication;
    private int categorieId;          
    private int quantiteTotale;       
    private int quantiteDisponible; 
    public Livre() {}

   
    public Livre(int id, String isbn, String titre, String auteur, String editeur, 
                 int anneePublication, int categorieId, int quantiteTotale) {
        this.id = id;
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.editeur = editeur;
        this.anneePublication = anneePublication;
        this.categorieId = categorieId;
        this.quantiteTotale = quantiteTotale;
        this.quantiteDisponible = quantiteTotale; 
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }

    public int getAnneePublication() { return anneePublication; }
    public void setAnneePublication(int anneePublication) { this.anneePublication = anneePublication; }

    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

    public int getQuantiteTotale() { return quantiteTotale; }
    public void setQuantiteTotale(int quantiteTotale) { this.quantiteTotale = quantiteTotale; }

    public int getQuantiteDisponible() { return quantiteDisponible; }
    public void setQuantiteDisponible(int quantiteDisponible) { this.quantiteDisponible = quantiteDisponible; }

   
    
    public boolean emprunter() {
        if (this.quantiteDisponible > 0) {
            this.quantiteDisponible--;
            return true;
        }
        return false;
    }

    public void retourner() {
        if (this.quantiteDisponible < this.quantiteTotale) {
            this.quantiteDisponible++;
        }
    }

    @Override
    public String toString() {
        return titre + " - " + auteur + " [" + isbn + "]";
    }
}