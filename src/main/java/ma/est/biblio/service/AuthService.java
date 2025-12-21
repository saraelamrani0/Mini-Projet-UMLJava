package ma.est.biblio.service;

import ma.est.biblio.dao.PersonneDAO;
import ma.est.biblio.model.Personne;

public class AuthService {

    private static final PersonneDAO personneDAO = new PersonneDAO();

    public static boolean authentifier(String login, String password) {

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            return false;
        }

        Personne user = personneDAO.findByLogin(login);
        if (user == null) {
            return false;
        }

        return user.verifierPassword(password);
    }
    public static String roleUtilisateur(String login) {

        if (login == null || login.isEmpty()) {
            return null;
        }

        Personne user = personneDAO.findByLogin(login);
        if (user == null) {
            return null;
        }

        return user.getRole();
    }
}
