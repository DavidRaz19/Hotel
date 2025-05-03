package com.hotel.util;

import com.hotel.model.Facture;
import com.hotel.model.Reservation;
import java.time.format.DateTimeFormatter;

public class FactureGenerator {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Génère le contenu d'une facture au format texte
     * @param facture La facture à imprimer
     * @return Le contenu texte de la facture
     */
    public static String genererFactureTexte(Facture facture) {
        Reservation reservation = facture.getReservation();
        
        StringBuilder sb = new StringBuilder();
        sb.append("======== FACTURE N° ").append(facture.getId()).append(" ========\n\n");
        sb.append("Date de facturation: ").append(facture.getDateFacturation().format(DATE_FORMAT)).append("\n\n");
        sb.append("CLIENT:\n");
        sb.append("Nom: ").append(reservation.getClient().getNomComplet()).append("\n");
        sb.append("Email: ").append(reservation.getClient().getEmail()).append("\n");
        if (reservation.getClient().getTelephone() != null && !reservation.getClient().getTelephone().isEmpty()) {
            sb.append("Téléphone: ").append(reservation.getClient().getTelephone()).append("\n");
        }
        sb.append("\nDÉTAILS DE LA RÉSERVATION:\n");
        sb.append("Numéro de réservation: ").append(reservation.getId()).append("\n");
        sb.append("Chambre: N° ").append(reservation.getChambre().getNumero()).append(" - Type: ").append(reservation.getChambre().getType()).append("\n");
        sb.append("Date d'arrivée: ").append(reservation.getDateDebut().format(DATE_FORMAT)).append("\n");
        sb.append("Date de départ: ").append(reservation.getDateFin().format(DATE_FORMAT)).append("\n");
        sb.append("Durée du séjour: ").append(reservation.getDuree()).append(" nuit(s)\n\n");
        sb.append("DÉTAIL DES FRAIS:\n");
        sb.append("Prix par nuit: ").append(String.format("%.2f", reservation.getChambre().getPrix())).append(" €\n");
        sb.append("Nombre de nuits: ").append(reservation.getDuree()).append("\n");
        sb.append("\nMONTANT TOTAL: ").append(String.format("%.2f", facture.getMontantTotal())).append(" €\n\n");
        sb.append("Statut: ").append(facture.getStatutPaiement()).append("\n\n");
        sb.append("Merci d'avoir choisi notre hôtel!\n");
        
        return sb.toString();
    }
    
    /**
     * Dans une application réelle, cette méthode générerait un PDF
     * en utilisant une bibliothèque comme iText ou PDFBox
     * @param facture La facture à convertir en PDF
     * @param cheminFichier Le chemin où sauvegarder le PDF
     */
    public static void genererFacturePDF(Facture facture, String cheminFichier) {
        // Implémentation avec une bibliothèque PDF comme iText ou PDFBox
        // Pour l'exemple, on simule juste la génération
        String contenuFacture = genererFactureTexte(facture);
        System.out.println("Facture PDF générée et enregistrée à: " + cheminFichier);
        System.out.println("Contenu de la facture:\n" + contenuFacture);
    }
}
