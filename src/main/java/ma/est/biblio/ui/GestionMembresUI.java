package ma.est.biblio.ui;

import ma.est.biblio.model.Adherent;
import ma.est.biblio.service.AdherentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionMembresUI extends JFrame {
    private JTextField txtNom, txtPrenom, txtEmail, txtTel;
    private JTable tableAdherents;
    private DefaultTableModel tableModel;
    private AdherentService adherentService;

    public GestionMembresUI() {
        adherentService = new AdherentService();
        setTitle("Gestion des Adhérents");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0);
        tableAdherents = new JTable(tableModel);

        setLayout(new BorderLayout(10, 10));
        add(createFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(tableAdherents), BorderLayout.CENTER);

        chargerDonnees();
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel(new FlowLayout());
        txtNom = new JTextField(10);
        txtPrenom = new JTextField(10);
        txtEmail = new JTextField(15);
        JButton btnAjouter = new JButton("Inscrire");

        p.add(new JLabel("Nom:")); p.add(txtNom);
        p.add(new JLabel("Prénom:")); p.add(txtPrenom);
        p.add(new JLabel("Email:")); p.add(txtEmail);
        p.add(btnAjouter);

        btnAjouter.addActionListener(e -> executerAjout());
        return p;
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0);

        List<Adherent> liste = adherentService.getAllAdherents();

        for (Adherent a : liste) {
            tableModel.addRow(new Object[]{
                a.getId(),
                a.getNom(),
                a.getPrenom(),
                a.getEmail(),
                a.isBloque() ? "Bloqué" : "Actif"
            });
        }
    }

    private void executerAjout() {
        try {
            Adherent a = new Adherent();
            a.setNom(txtNom.getText().trim());
            a.setPrenom(txtPrenom.getText().trim());
            a.setEmail(txtEmail.getText().trim());

            adherentService.inscrireAdherent(a);

            JOptionPane.showMessageDialog(this, "Adhérent inscrit !");

            chargerDonnees(); 
            
            txtNom.setText(""); txtPrenom.setText(""); txtEmail.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
}