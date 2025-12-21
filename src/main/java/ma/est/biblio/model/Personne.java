package ma.est.biblio.model;

import ma.est.biblio.util.PasswordUtils;
public class Personne {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String type;     
    private String password; 
    private String role;     
    private String login;    
    public Personne() {}

    
    public Personne(String nom, String prenom, String email, String telephone, String adresse, String type) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.type = type;
    }

    public Personne(String nom, String prenom, String email, String telephone, String adresse, String type, String login, String password) {
        this(nom, prenom, email, telephone, adresse, type);
        this.login = login;
        if (password != null) {
            this.password = PasswordUtils.hashPassword(password);
        }
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { 
        // On ne hache que si le mot de passe n'est pas déjà un hash (simple vérification)
        if (password != null && password.length() < 30) { 
            this.password = PasswordUtils.hashPassword(password);
        } else {
            this.password = password;
        }
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

   
    public boolean verifierPassword(String motDePasseSaisi) {
        if (this.password == null || motDePasseSaisi == null) return false;
        return PasswordUtils.verifyPassword(motDePasseSaisi, this.password);
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}