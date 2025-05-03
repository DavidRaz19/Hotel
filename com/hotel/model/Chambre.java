package com.hotel.model;

public class Chambre {
    private int numero;
    private String type;
    private String statut;
    private double prix;
    
    public Chambre() {}
    
    public Chambre(int numero, String type, String statut, double prix) {
        this.numero = numero;
        this.type = type;
        this.statut = statut;
        this.prix = prix;
    }
    
    public int getNumero() {
        return numero;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public double getPrix() {
        return prix;
    }
    
    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    public String getDetails() {
        return "Chambre n°" + numero + " - Type: " + type + " - Prix: " + prix + "€ - Statut: " + statut;
    }
    
    @Override
    public String toString() {
        return "Chambre n°" + numero + " (" + type + ")";
    }
}
