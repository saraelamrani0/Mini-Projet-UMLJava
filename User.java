package ma.est.biblio.model;

public class User {

    private int id;
    private String login;
    private String passwordHash;
    private Role role;        // ADMIN ou USER
    private boolean active;
    private Adherent adherent; // null si ADMIN

    public User() {}

    public User(String login, String passwordHash, Role role, boolean active) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
    }

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }
    
    
}
