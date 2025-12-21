package ma.est.biblio.ui;

import ma.est.biblio.model.Livre;
import ma.est.biblio.service.LivreService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionLivresUI extends JFrame {
    private JTextField txtIsbn, txtTitre, txtAuteur, txtEditeur, txtQuantite, txtSearch;
    private JButton btnAjouter, btnAnnuler;
    private JTable tableLivres;
    private DefaultTableModel tableModel;
    private LivreService livreService;
    private JTabbedPane tabbedPane;

    public GestionLivresUI() {
        livreService = new LivreService();
        setTitle("Système de Gestion des Livres");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📚 Inventaire & Recherche", createListPanel());
        tabbedPane.addTab("➕ Formulaire Livre", createAddPanel());
        
        add(tabbedPane);
        chargerDonnees();
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Rechercher");
        JButton btnRefresh = new JButton("Actualiser");
        
        JButton btnEdit = new JButton("Modifier");
        btnEdit.setBackground(new Color(52, 152, 219));
        btnEdit.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Supprimer");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);

        toolBar.add(new JLabel("Recherche :"));
        toolBar.add(txtSearch);
        toolBar.add(btnSearch);
        toolBar.add(btnRefresh);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);

        String[] columns = {"ISBN", "Titre", "Auteur", "Editeur", "Total", "Disponibles"};
        tableModel = new DefaultTableModel(columns, 0) {
            
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableLivres = new JTable(tableModel);
        
        btnSearch.addActionListener(e -> filtrerDonnees());
        btnRefresh.addActionListener(e -> chargerDonnees());
        btnDelete.addActionListener(e -> supprimerSelection());
        btnEdit.addActionListener(e -> preparerModification());

        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableLivres), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAddPanel() {
        JPanel mainAddPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIsbn = new JTextField(20);
        txtTitre = new JTextField(20);
        txtAuteur = new JTextField(20);
        txtEditeur = new JTextField(20);
        txtQuantite = new JTextField(20);

        btnAjouter = new JButton("Enregistrer le livre");
        btnAjouter.setBackground(new Color(46, 204, 113));
        btnAjouter.setForeground(Color.WHITE);

        btnAnnuler = new JButton("Annuler / Nouveau");
        btnAnnuler.setVisible(false);

        addFormField(mainAddPanel, "ISBN :", txtIsbn, gbc, 0);
        addFormField(mainAddPanel, "Titre :", txtTitre, gbc, 1);
        addFormField(mainAddPanel, "Auteur :", txtAuteur, gbc, 2);
        addFormField(mainAddPanel, "Éditeur :", txtEditeur, gbc, 3);
        addFormField(mainAddPanel, "Quantité Totale :", txtQuantite, gbc, 4);
        
        gbc.gridx = 1; gbc.gridy = 5;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnAnnuler);
        mainAddPanel.add(buttonPanel, gbc);

        btnAjouter.addActionListener(e -> executerAction());
        btnAnnuler.addActionListener(e -> reinitialiserFormulaire());
        
        return mainAddPanel;
    }

    private void addFormField(JPanel p, String label, JTextField tf, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(tf, gbc);
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0);
        List<Livre> livres = livreService.getAllLivres();
        for (Livre l : livres) {
            tableModel.addRow(new Object[]{
                l.getIsbn(), l.getTitre(), l.getAuteur(), l.getEditeur(),
                l.getQuantiteTotale(), l.getQuantiteDisponible()
            });
        }
    }

    private void filtrerDonnees() {
        String terme = txtSearch.getText().trim();
        List<Livre> resultats = livreService.rechercherLivres(terme);
        tableModel.setRowCount(0);
        for (Livre l : resultats) {
            tableModel.addRow(new Object[]{
                l.getIsbn(), l.getTitre(), l.getAuteur(), l.getEditeur(),
                l.getQuantiteTotale(), l.getQuantiteDisponible()
            });
        }
    }

    private void preparerModification() {
        int row = tableLivres.getSelectedRow();
        if (row != -1) {
            txtIsbn.setText(getCellValue(row, 0));
            txtTitre.setText(getCellValue(row, 1));
            txtAuteur.setText(getCellValue(row, 2));
            txtEditeur.setText(getCellValue(row, 3));
            txtQuantite.setText(getCellValue(row, 4));

            txtIsbn.setEditable(false);
            txtIsbn.setBackground(new Color(236, 240, 241));
            btnAjouter.setText("Mettre à jour le livre");
            btnAjouter.setBackground(new Color(52, 152, 219));
            btnAnnuler.setVisible(true);

            tabbedPane.setSelectedIndex(1); 
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez d'abord un livre dans la liste.");
        }
    }

    private String getCellValue(int row, int col) {
        Object val = tableModel.getValueAt(row, col);
        return (val != null) ? val.toString() : "";
    }

    private void executerAction() {
        try {
            Livre l = new Livre();
            l.setIsbn(txtIsbn.getText().trim());
            l.setTitre(txtTitre.getText().trim());
            l.setAuteur(txtAuteur.getText().trim());
            l.setEditeur(txtEditeur.getText().trim());
            int qte = Integer.parseInt(txtQuantite.getText().trim());
            l.setQuantiteTotale(qte);
            l.setQuantiteDisponible(qte); 
            l.setCategorieId(1);

            if (!txtIsbn.isEditable()) {
                livreService.modifierLivre(l);
                JOptionPane.showMessageDialog(this, "Livre mis à jour !");
            } else {
                livreService.ajouterLivre(l);
                JOptionPane.showMessageDialog(this, "Livre ajouté !");
            }

            reinitialiserFormulaire();
            chargerDonnees();
            tabbedPane.setSelectedIndex(0); 

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void reinitialiserFormulaire() {
        txtIsbn.setText("");
        txtTitre.setText("");
        txtAuteur.setText("");
        txtEditeur.setText("");
        txtQuantite.setText("");
        txtIsbn.setEditable(true);
        txtIsbn.setBackground(Color.WHITE);
        btnAjouter.setText("Enregistrer le livre");
        btnAjouter.setBackground(new Color(46, 204, 113));
        btnAnnuler.setVisible(false);
    }

    private void supprimerSelection() {
        int row = tableLivres.getSelectedRow();
        if (row != -1) {
            String isbn = getCellValue(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le livre " + isbn + " ?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (livreService.supprimerLivre(isbn)) {
                    chargerDonnees();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez un livre.");
        }
    }
}