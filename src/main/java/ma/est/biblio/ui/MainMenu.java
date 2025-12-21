package ma.est.biblio.ui;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu(String role) {
        setTitle("Menu Principal - " + role);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Bienvenue, rôle : " + role);
        panel.add(label);

        add(panel);
    }
}
