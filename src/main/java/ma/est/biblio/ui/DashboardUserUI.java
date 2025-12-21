package ma.est.biblio.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardUserUI extends JFrame {

    public DashboardUserUI(String username) {
        setTitle("Tableau de Bord - Utilisateur");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + username + " !");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnGererAdherents = createStyledButton("👤 Gérer Adhérents", new Color(41, 128, 185));
        JButton btnGererLivres = createStyledButton("📚 Gérer Livres", new Color(39, 174, 96));
        JButton btnGererEmprunts = createStyledButton("📖 Gérer Emprunts", new Color(230, 126, 34));
        JButton btnGererReservations = createStyledButton("📅 Gérer Réservations", new Color(142, 68, 173));

        btnGererAdherents.addActionListener(e -> {
            GestionMembresUI membresUI = new GestionMembresUI();
            membresUI.setVisible(true);
        });

        btnGererLivres.addActionListener(e -> {
            GestionLivresUI livresUI = new GestionLivresUI();
            livresUI.setVisible(true);
        });

        btnGererEmprunts.addActionListener(e -> {
            GestionEmpruntsUI empruntsUI = new GestionEmpruntsUI();
            empruntsUI.setVisible(true);
        });

        btnGererReservations.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Gérer Réservations à implémenter");
        });

        centerPanel.add(btnGererAdherents);
        centerPanel.add(btnGererLivres);
        centerPanel.add(btnGererEmprunts);
        centerPanel.add(btnGererReservations);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = createStyledButton("🚪 Déconnexion", new Color(192, 57, 43));
        btnLogout.addActionListener(e -> this.dispose());
        footerPanel.add(btnLogout);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardUserUI dashboard = new DashboardUserUI("utilisateur");
            dashboard.setVisible(true);
        });
    }
}
