package ma.est.biblio.ui;

import ma.est.biblio.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private JTextField loginField;
    private JPasswordField mdpField;

    public LoginUI() {
        setTitle("Connexion - Bibliothèque");
        setSize(360, 230);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Connexion à la Bibliothèque", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Login :"), gbc);

        gbc.gridx = 1;
        loginField = new JTextField(15);
        mainPanel.add(loginField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Mot de passe :"), gbc);

        gbc.gridx = 1;
        mdpField = new JPasswordField(15);
        mainPanel.add(mdpField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton btnLogin = new JButton("Se connecter");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        btnLogin.addActionListener(e -> connecter());

        mainPanel.add(btnLogin, gbc);

        gbc.gridy = 4;
        JLabel infoLabel = new JLabel("Test : admin / admin123", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoLabel.setForeground(Color.GRAY);
        mainPanel.add(infoLabel, gbc);

        add(mainPanel);
    }

    private void connecter() {
        String login = loginField.getText().trim();
        String mdp = new String(mdpField.getPassword());

        if (login.isEmpty() || mdp.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (AuthService.authentifier(login, mdp)) {
            String role = AuthService.roleUtilisateur(login);

            JOptionPane.showMessageDialog(
                    this,
                    "Connexion réussie !\nRôle : " + role,
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
            );

            this.dispose();

            if ("ADMIN".equalsIgnoreCase(role)) {
                new DashboardAdminUI().setVisible(true);
            } else {
                new DashboardUserUI(login).setVisible(true);
            }

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Login ou mot de passe incorrect",
                    "Échec",
                    JOptionPane.ERROR_MESSAGE
            );
            mdpField.setText("");
            loginField.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new LoginUI().setVisible(true);
        });
    }
}
