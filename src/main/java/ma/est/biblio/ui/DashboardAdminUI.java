package ma.est.biblio.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardAdminUI extends JFrame {
    
    public DashboardAdminUI() {
        setTitle("Tableau de Bord - Administrateur");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Tableau de Bord Administrateur");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel userLabel = new JLabel("Administrateur");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        statsPanel.add(createStatCard(" Livres", "125", new Color(41, 128, 185)));
        statsPanel.add(createStatCard(" Adhérents", "45", new Color(39, 174, 96)));
        statsPanel.add(createStatCard(" Emprunts actifs", "28", new Color(142, 68, 173)));
        statsPanel.add(createStatCard(" Retards", "3", new Color(230, 126, 34)));
        statsPanel.add(createStatCard(" Taux d'emprunt", "78%", new Color(22, 160, 133)));
        statsPanel.add(createStatCard(" Sauvegarde", "Hier", new Color(192, 57, 43)));
        
        JPanel quickActionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        quickActionsPanel.setBorder(BorderFactory.createTitledBorder("Actions Rapides"));

        JButton btnLivre = createActionButton(" Ajouter Livre");
        btnLivre.addActionListener(e -> new GestionLivresUI().setVisible(true));

        JButton btnAdherent = createActionButton(" Nouvel Adhérent");
        btnAdherent.addActionListener(e -> new GestionMembresUI().setVisible(true));

        JButton btnEmprunt = createActionButton(" Gérer Emprunts");
        btnEmprunt.addActionListener(e -> new GestionEmpruntsUI().setVisible(true));

        JButton btnLogout = createActionButton(" Déconnexion");
        btnLogout.addActionListener(e -> {
            new LoginUI().setVisible(true);
            this.dispose();
        });

        quickActionsPanel.add(btnLivre);
        quickActionsPanel.add(btnAdherent);
        quickActionsPanel.add(btnEmprunt);
        quickActionsPanel.add(btnLogout);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(quickActionsPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(236, 240, 241));
        button.setFocusPainted(false);
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DashboardAdminUI().setVisible(true);
        });
    }
}