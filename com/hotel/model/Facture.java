package com.hotel.model;

import java.time.LocalDate;

public class Facture {
    private int id;
    private Reservation reservation;
    private LocalDate dateFacturation;
    private double montantTotal;
    private String statutPaiement;
    
    public Facture() {
        this.dateFacturation = LocalDate.now();
    }
    
    public Facture(Reservation reservation) {
        this.reservation = reservation;
        this.dateFacturation = LocalDate.now();
        this.montantTotal = reservation.getPrixTotal();
        this.statutPaiement = "Non payée";
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Reservation getReservation() {
        return reservation;
    }
    
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
    public LocalDate getDateFacturation() {
        return dateFacturation;
    }
    
    public void setDateFacturation(LocalDate dateFacturation) {
        this.dateFacturation = dateFacturation;
    }
    
    public double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public String getStatutPaiement() {
        return statutPaiement;
    }
    
    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
}
