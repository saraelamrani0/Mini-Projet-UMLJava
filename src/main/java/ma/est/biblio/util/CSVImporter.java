package ma.est.biblio.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter {
    
    public static List<String[]> lireCSV(String cheminFichier, String separateur) {
        List<String[]> lignes = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(cheminFichier), StandardCharsets.UTF_8))) {
            
            String ligne;
            while ((ligne = br.readLine()) != null) {

                if (ligne.trim().isEmpty()) {
                    continue;
                }
                
                String[] colonnes = ligne.split(separateur);
                lignes.add(colonnes);
            }
            
            LoggerUtil.log("Fichier CSV '" + cheminFichier + "' importé avec succès. " + lignes.size() + " lignes lues.");
            
        } catch (FileNotFoundException e) {
            System.err.println("❌ Fichier non trouvé : " + cheminFichier);
            LoggerUtil.log("ERREUR: Fichier non trouvé - " + cheminFichier);
        } catch (IOException e) {
            System.err.println("❌ Erreur de lecture du fichier : " + e.getMessage());
            LoggerUtil.log("ERREUR: Lecture fichier CSV - " + e.getMessage());
        }
        
        return lignes;
    }

    public static List<LivreCSV> importerLivres(String cheminFichier) {
        List<LivreCSV> livres = new ArrayList<>();
        List<String[]> lignes = lireCSV(cheminFichier, ";");

        for (int i = 1; i < lignes.size(); i++) {
            String[] colonnes = lignes.get(i);
            
            if (colonnes.length >= 4) {
                LivreCSV livre = new LivreCSV();
                livre.isbn = colonnes[0].trim();
                livre.titre = colonnes[1].trim();
                livre.auteur = colonnes[2].trim();
                
                try {
                    livre.exemplaires = Integer.parseInt(colonnes[3].trim());
                } catch (NumberFormatException e) {
                    livre.exemplaires = 1;
                }
                
                if (colonnes.length >= 5) {
                    livre.description = colonnes[4].trim();
                }
                
                livres.add(livre);
            }
        }
        
        LoggerUtil.log("Import CSV: " + livres.size() + " livres importés depuis '" + cheminFichier + "'");
        return livres;
    }
    
    public static List<AdherentCSV> importerAdherents(String cheminFichier) {
        List<AdherentCSV> adherents = new ArrayList<>();
        List<String[]> lignes = lireCSV(cheminFichier, ";");
        
        // Ignorer l'en-tête
        for (int i = 1; i < lignes.size(); i++) {
            String[] colonnes = lignes.get(i);
            
            if (colonnes.length >= 5) {
                AdherentCSV adherent = new AdherentCSV();
                adherent.id = colonnes[0].trim();
                adherent.nom = colonnes[1].trim();
                adherent.prenom = colonnes[2].trim();
                adherent.email = colonnes[3].trim();
                adherent.telephone = colonnes[4].trim();
                
                adherents.add(adherent);
            }
        }
        
        LoggerUtil.log("Import CSV: " + adherents.size() + " adhérents importés depuis '" + cheminFichier + "'");
        return adherents;
    }
    
    public static boolean exporterCSV(String cheminFichier, List<String[]> donnees, String separateur) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(cheminFichier), StandardCharsets.UTF_8))) {
            
            for (String[] ligne : donnees) {
                writer.println(String.join(separateur, ligne));
            }
            
            LoggerUtil.log("Export CSV: " + donnees.size() + " lignes exportées vers '" + cheminFichier + "'");
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Erreur d'export CSV : " + e.getMessage());
            LoggerUtil.log("ERREUR: Export CSV - " + e.getMessage());
            return false;
        }
    }
    
    public static boolean validerFormatCSV(String cheminFichier, String separateur, int nombreColonnesAttendues) {
        List<String[]> lignes = lireCSV(cheminFichier, separateur);
        
        if (lignes.isEmpty()) {
            return false;
        }
        

        String[] premiereLigne = lignes.get(0);
        
        if (premiereLigne.length != nombreColonnesAttendues) {
            System.err.println("❌ Format CSV invalide. Attendu: " + nombreColonnesAttendues + 
                             " colonnes, Trouvé: " + premiereLigne.length);
            return false;
        }
        
        return true;
    }
    

    public static class LivreCSV {
        public String isbn;
        public String titre;
        public String auteur;
        public int exemplaires;
        public String description;
        
        @Override
        public String toString() {
            return "LivreCSV [isbn=" + isbn + ", titre=" + titre + ", auteur=" + auteur + 
                   ", exemplaires=" + exemplaires + "]";
        }
    }
    
    public static class AdherentCSV {
        public String id;
        public String nom;
        public String prenom;
        public String email;
        public String telephone;
        
        @Override
        public String toString() {
            return "AdherentCSV [id=" + id + ", nom=" + nom + ", prenom=" + prenom + 
                   ", email=" + email + ", telephone=" + telephone + "]";
        }
    }
    

    public static void main(String[] args) {
        System.out.println("=== TEST CSV IMPORTER ===");
        String testFile = "test_livres.csv";
        try (PrintWriter writer = new PrintWriter(testFile, "UTF-8")) {
            writer.println("ISBN;Titre;Auteur;Exemplaires;Description");
            writer.println("978-2070360024;Le Petit Prince;Antoine de Saint-Exupéry;5;Conte philosophique");
            writer.println("978-2253010692;L'Étranger;Albert Camus;3;Roman philosophique");
            writer.println("978-2070368228;Harry Potter;J.K. Rowling;7;Roman fantasy");
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        List<LivreCSV> livres = importerLivres(testFile);
        System.out.println("Livres importés : " + livres.size());
        for (LivreCSV livre : livres) {
            System.out.println("  - " + livre.titre + " par " + livre.auteur);
        }

        new File(testFile).delete();
    }
}
