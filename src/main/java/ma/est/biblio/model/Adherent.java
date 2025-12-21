
package ma.est.biblio.model;

import ma.est.biblio.util.PasswordUtils;

public class Adherent {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String login;
    private String password;
    private String role;
    private boolean bloque; 

    public Adherent() {}

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
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isBloque() { return bloque; }
    public void setBloque(boolean bloque) { this.bloque = bloque; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { 
        this.password = PasswordUtils.hashPassword(password); 
    }
}