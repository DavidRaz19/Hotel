package com.hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private int id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Chambre chambre;
    private Client client;
    private double prixTotal;
    private String statut;
    private LocalDate dateCreation;
    
    public Reservation() {
        this.dateCreation = LocalDate.now();
    }
    
    public Reservation(LocalDate dateDebut, LocalDate dateFin, Chambre chambre, Client client) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.chambre = chambre;
        this.client = client;
        this.statut = "Confirmée";
        this.dateCreation = LocalDate.now();
        this.prixTotal = calculerPrix();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public Chambre getChambre() {
        return chambre;
    }
    
    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public double getPrixTotal() {
        return prixTotal;
    }
    
    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public LocalDate getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public int getDuree() {
        return (int) ChronoUnit.DAYS.between(dateDebut, dateFin);
    }
    
    public double calculerPrix() {
        return chambre.getPrix() * getDuree();
    }
    
    @Override
    public String toString() {
        return "Réservation #" + id + " - " + client.getNomComplet() + " - Chambre " + chambre.getNumero();
    }
}
