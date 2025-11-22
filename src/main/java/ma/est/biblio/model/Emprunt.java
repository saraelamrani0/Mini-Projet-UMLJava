package ma.est.biblio.model;

import java.util.Date;

public class Emprunt {

    private int id;
    private Livre livre;
    private Adherent adherent;
    private Date dateEmprunt;
    private Date dateRetour;

    public Emprunt() {}
}
