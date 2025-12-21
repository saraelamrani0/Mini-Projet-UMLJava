package ma.est.biblio.ui;

import ma.est.biblio.model.Emprunt;
import ma.est.biblio.service.EmpruntService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class GestionEmpruntsUI extends JFrame {

    private JTable empruntsTable;
    private DefaultTableModel tableModel;
    private EmpruntService empruntService;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public GestionEmpruntsUI() {
        this.empruntService = new EmpruntService();

        setTitle("Système de Gestion des Emprunts");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Actions Rapides"));

        JButton btnNouveau = createStyledButton("➕ Nouvel Emprunt", new Color(41, 128, 185));
        JButton btnRetour = createStyledButton("↩️ Marquer comme Rendu", new Color(39, 174, 96));
        JButton btnActualiser = createStyledButton("🔄 Actualiser la liste", new Color(127, 140, 141));

        topPanel.add(btnNouveau);
        topPanel.add(btnRetour);
        topPanel.add(btnActualiser);

        String[] colonnes = {"ID", "ID Adhérent", "ID Livre", "Date Emprunt", "Retour Prévu", "Date Retour Réelle", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            
            public boolean isCellEditable(int row, int column) { return false; }
        };

        empruntsTable = new JTable(tableModel);
        empruntsTable.setRowHeight(30);
        empruntsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        empruntsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        empruntsTable.setDefaultRenderer(Object.class, new EmpruntRenderer());

        JScrollPane scrollPane = new JScrollPane(empruntsTable);

        btnNouveau.addActionListener(e -> ouvrirFormulaireEmprunt());
        btnRetour.addActionListener(e -> traiterRetourLivre());
        btnActualiser.addActionListener(e -> chargerDonnees());

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        chargerDonnees();
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0);
        try {
            List<Emprunt> liste = empruntService.getTousLesEmprunts();
            for (Emprunt e : liste) {
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getAdherentId(),
                        e.getLivreId(),
                        sdf.format(e.getDateEmprunt()),
                        sdf.format(e.getDateRetourPrevue()),
                        (e.getDateRetourEffective() != null) ? sdf.format(e.getDateRetourEffective()) : "---",
                        e.getStatut()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement : " + ex.getMessage());
        }
    }

    private void ouvrirFormulaireEmprunt() {
        JTextField txtAdherentId = new JTextField();
        JTextField txtIsbn = new JTextField();

        Object[] fields = {
            "ID de l'Adhérent :", txtAdherentId,
            "ISBN du Livre :", txtIsbn
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Enregistrer un Emprunt", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                if (txtAdherentId.getText().trim().isEmpty() || txtIsbn.getText().trim().isEmpty()) {
                    throw new Exception("Veuillez remplir tous les champs.");
                }

                int adhId = Integer.parseInt(txtAdherentId.getText().trim());
                String isbn = txtIsbn.getText().trim();

                Emprunt cree = empruntService.emprunterLivre(adhId, isbn);

                if (cree != null) {
                    JOptionPane.showMessageDialog(this, "Emprunt enregistré avec succès !");
                    chargerDonnees();
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "L'ID Adhérent doit être un nombre valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Échec de l'emprunt", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void traiterRetourLivre() {
        int selectedRow = empruntsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt dans le tableau.");
            return;
        }

        int idEmprunt = (int) tableModel.getValueAt(selectedRow, 0);
        String statut = (String) tableModel.getValueAt(selectedRow, 6);

        if ("RENDU".equals(statut)) {
            JOptionPane.showMessageDialog(this, "Ce livre a déjà été rendu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer le retour de ce livre ?", "Retour de livre", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (empruntService.retournerLivre(idEmprunt)) {
                    JOptionPane.showMessageDialog(this, "Retour effectué. Le stock a été mis à jour.");
                    chargerDonnees();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors du traitement du retour.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    class EmpruntRenderer extends DefaultTableCellRenderer {
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            
            String statut = table.getValueAt(row, 6).toString();

            if ("RENDU".equals(statut)) {
                c.setBackground(new Color(235, 247, 235)); 
                c.setForeground(new Color(39, 174, 96));
            } else if ("EN_COURS".equals(statut)) {
                c.setBackground(new Color(254, 249, 231)); 
                c.setForeground(new Color(212, 172, 13));
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }
            
            return c;
        }
    }
}