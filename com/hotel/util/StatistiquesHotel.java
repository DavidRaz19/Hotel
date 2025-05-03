package com.hotel.util;

import com.hotel.dao.ChambreDAO;
import com.hotel.dao.FactureDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.model.Chambre;
import com.hotel.model.Reservation;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiquesHotel {
    private ReservationDAO reservationDAO;
    private ChambreDAO chambreDAO;
    private FactureDAO factureDAO;
    
    public StatistiquesHotel() {
        this.reservationDAO = new ReservationDAO();
        this.chambreDAO = new ChambreDAO();
        this.factureDAO = new FactureDAO();
    }
    
    /**
     * Calcule le taux d'occupation pour un mois donné
     * @param mois Mois (1-12)
     * @param annee Année
     * @return Taux d'occupation en pourcentage
     */
    public double calculerTauxOccupation(int mois, int annee) {
        // Nombre total de chambres
        List<Chambre> chambres = chambreDAO.getAll();
        int nombreTotalChambres = chambres.size();
        
        if (nombreTotalChambres == 0) return 0.0;
        
        // Nombre de jours dans le mois
        YearMonth yearMonth = YearMonth.of(annee, mois);
        int joursParMois = yearMonth.lengthOfMonth();
        
        // Nombre total de nuitées possibles
        int nuiteesPossibles = nombreTotalChambres * joursParMois;
        
        // Nombre de nuitées réservées
        LocalDate debutMois = LocalDate.of(annee, mois, 1);
        LocalDate finMois = debutMois.plusMonths(1).minusDays(1);
        
        List<Reservation> reservations = reservationDAO.getReservationsParPeriode(debutMois, finMois);
        
        int nuiteesReservees = 0;
        for (Reservation reservation : reservations) {
            LocalDate debut = reservation.getDateDebut();
            LocalDate fin = reservation.getDateFin();
            
            // Ajuster les dates pour ne considérer que la période du mois
            if (debut.isBefore(debutMois)) debut = debutMois;
            if (fin.isAfter(finMois)) fin = finMois;
            
            int duree = (int) debut.until(fin).getDays();
            nuiteesReservees += duree;
        }
        
        // Calcul du taux d'occupation
        return (double) nuiteesReservees / nuiteesPossibles * 100;
    }
    
    /**
     * Calcule le revenu total pour un mois donné
     * @param mois Mois (1-12)
     * @param annee Année
     * @return Revenu total
     */
    public double calculerRevenuMensuel(int mois, int annee) {
        return factureDAO.getTotalRevenueParMois(mois, annee);
    }
    
    /**
     * Récupère les revenus mensuels pour une année donnée
     * @param annee Année
     * @return Map avec le mois comme clé et le revenu comme valeur
     */
    public Map<String, Double> getRevenusMensuels(int annee) {
        Map<String, Double> revenus = new HashMap<>();
        
        for (int i = 1; i <= 12; i++) {
            Month mois = Month.of(i);
            double revenu = calculerRevenuMensuel(i, annee);
            revenus.put(mois.toString(), revenu);
        }
        
        return revenus;
    }
    
    /**
     * Récupère les taux d'occupation mensuels pour une année donnée
     * @param annee Année
     * @return Map avec le mois comme clé et le taux d'occupation comme valeur
     */
    public Map<String, Double> getTauxOccupationMensuels(int annee) {
        Map<String, Double> tauxOccupation = new HashMap<>();
        
        for (int i = 1; i <= 12; i++) {
            Month mois = Month.of(i);
            double taux = calculerTauxOccupation(i, annee);
            tauxOccupation.put(mois.toString(), taux);
        }
        
        return tauxOccupation;
    }
}
