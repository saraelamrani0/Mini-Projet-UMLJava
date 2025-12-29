package ma.est.biblio.dao;

import ma.est.biblio.model.User;
import ma.est.biblio.model.Role;
import ma.est.biblio.model.Adherent;
import ma.est.biblio.util.PasswordUtils;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private List<User> users = new ArrayList<>();
    private int nextId = 1;

    public UserDAO() {
        // Admin
        User admin = new User("admin", PasswordUtils.hashPassword("admin123"), Role.ADMIN, true);
        admin.setId(nextId++);
        users.add(admin);

        // Utilisateur classique
        User user = new User("user", PasswordUtils.hashPassword("user123"), Role.USER, true);
        user.setId(nextId++);
        
        // Créer un Adherent mock
        Adherent adherent = new Adherent();
        adherent.setNom("Jean");  // Nom affiché dans le dashboard
        user.setAdherent(adherent);

        users.add(user);
    }

    public User findByLogin(String login) {
        for (User u : users) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                return u;
            }
        }
        return null;
    }
}
