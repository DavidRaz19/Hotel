package com.hotel.dao;

import com.hotel.model.Chambre;
import com.hotel.model.Client;
import com.hotel.model.Reservation;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private ChambreDAO chambreDAO = new ChambreDAO();
    private ClientDAO clientDAO = new ClientDAO();

    public boolean ajouter(Reservation reservation) {
        String sql = "INSERT INTO Reservation (dateDebut, dateFin, numeroChambre, clientId, prixTotal, statut, dateCreation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, reservation.getDateDebut().toString());
            pstmt.setString(2, reservation.getDateFin().toString());
            pstmt.setInt(3, reservation.getChambre().getNumero());
            pstmt.setInt(4, reservation.getClient().getId());
            pstmt.setDouble(5, reservation.getPrixTotal());
            pstmt.setString(6, reservation.getStatut());
            pstmt.setString(7, reservation.getDateCreation().toString());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setId(generatedKeys.getInt(1));
                    }
                }
                
                // Mettre à jour le statut de la chambre
                chambreDAO.updateStatut(reservation.getChambre().getNumero(), "Occupée");
                
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean modifier(Reservation reservation) {
        String sql = "UPDATE Reservation SET dateDebut = ?, dateFin = ?, numeroChambre = ?, clientId = ?, prixTotal = ?, statut = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reservation.getDateDebut().toString());
            pstmt.setString(2, reservation.getDateFin().toString());
            pstmt.setInt(3, reservation.getChambre().getNumero());
            pstmt.setInt(4, reservation.getClient().getId());
            pstmt.setDouble(5, reservation.getPrixTotal());
            pstmt.setString(6, reservation.getStatut());
            pstmt.setInt(7, reservation.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean annuler(int id) {
        // Récupérer la réservation
        Reservation reservation = getById(id);
        if (reservation == null) return false;
        
        String sql = "UPDATE Reservation SET statut = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "Annulée");
            pstmt.setInt(2, id);
            
            boolean success = pstmt.executeUpdate() > 0;
            
            if (success) {
                // Libérer la chambre
                chambreDAO.updateStatut(reservation.getChambre().getNumero(), "Disponible");
            }
            
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean terminer(int id) {
        // Récupérer la réservation
        Reservation reservation = getById(id);
        if (reservation == null) return false;
        
        String sql = "UPDATE Reservation SET statut = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "Terminée");
            pstmt.setInt(2, id);
            
            boolean success = pstmt.executeUpdate() > 0;
            
            if (success) {
                // Libérer la chambre
                chambreDAO.updateStatut(reservation.getChambre().getNumero(), "Disponible");
            }
            
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Reservation getById(int id) {
        String sql = "SELECT * FROM Reservation WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setDateDebut(LocalDate.parse(rs.getString("dateDebut")));
                    reservation.setDateFin(LocalDate.parse(rs.getString("dateFin")));
                    
                    // Récupérer la chambre associée
                    Chambre chambre = chambreDAO.getById(rs.getInt("numeroChambre"));
                    reservation.setChambre(chambre);
                    
                    // Récupérer le client associé
                    Client client = clientDAO.getById(rs.getInt("clientId"));
                    reservation.setClient(client);
                    
                    reservation.setPrixTotal(rs.getDouble("prixTotal"));
                    reservation.setStatut(rs.getString("statut"));
                    reservation.setDateCreation(LocalDate.parse(rs.getString("dateCreation")));
                    
                    return reservation;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setDateDebut(LocalDate.parse(rs.getString("dateDebut")));
                reservation.setDateFin(LocalDate.parse(rs.getString("dateFin")));
                
                // Récupérer la chambre associée
                Chambre chambre = chambreDAO.getById(rs.getInt("numeroChambre"));
                reservation.setChambre(chambre);
                
                // Récupérer le client associé
                Client client = clientDAO.getById(rs.getInt("clientId"));
                reservation.setClient(client);
                
                reservation.setPrixTotal(rs.getDouble("prixTotal"));
                reservation.setStatut(rs.getString("statut"));
                reservation.setDateCreation(LocalDate.parse(rs.getString("dateCreation")));
                
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public List<Reservation> getReservationsActives() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE statut = 'Confirmée'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setDateDebut(LocalDate.parse(rs.getString("dateDebut")));
                reservation.setDateFin(LocalDate.parse(rs.getString("dateFin")));
                
                // Récupérer la chambre associée
                Chambre chambre = chambreDAO.getById(rs.getInt("numeroChambre"));
                reservation.setChambre(chambre);
                
                // Récupérer le client associé
                Client client = clientDAO.getById(rs.getInt("clientId"));
                reservation.setClient(client);
                
                reservation.setPrixTotal(rs.getDouble("prixTotal"));
                reservation.setStatut(rs.getString("statut"));
                reservation.setDateCreation(LocalDate.parse(rs.getString("dateCreation")));
                
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public List<Reservation> getReservationsParClient(int clientId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE clientId = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setDateDebut(LocalDate.parse(rs.getString("dateDebut")));
                    reservation.setDateFin(LocalDate.parse(rs.getString("dateFin")));
                    
                    // Récupérer la chambre associée
                    Chambre chambre = chambreDAO.getById(rs.getInt("numeroChambre"));
                    reservation.setChambre(chambre);
                    
                    // Récupérer le client associé
                    Client client = clientDAO.getById(rs.getInt("clientId"));
                    reservation.setClient(client);
                    
                    reservation.setPrixTotal(rs.getDouble("prixTotal"));
                    reservation.setStatut(rs.getString("statut"));
                    reservation.setDateCreation(LocalDate.parse(rs.getString("dateCreation")));
                    
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public List<Reservation> getReservationsParPeriode(LocalDate debut, LocalDate fin) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE dateDebut >= ? AND dateFin <= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, debut.toString());
            pstmt.setString(2, fin.toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setDateDebut(LocalDate.parse(rs.getString("dateDebut")));
                    reservation.setDateFin(LocalDate.parse(rs.getString("dateFin")));
                    
                    // Récupérer la chambre associée
                    Chambre chambre = chambreDAO.getById(rs.getInt("numeroChambre"));
                    reservation.setChambre(chambre);
                    
                    // Récupérer le client associé
                    Client client = clientDAO.getById(rs.getInt("clientId"));
                    reservation.setClient(client);
                    
                    reservation.setPrixTotal(rs.getDouble("prixTotal"));
                    reservation.setStatut(rs.getString("statut"));
                    reservation.setDateCreation(LocalDate.parse(rs.getString("dateCreation")));
                    
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
}
