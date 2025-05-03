package com.hotel.dao;

import com.hotel.model.Facture;
import com.hotel.model.Reservation;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {
    private ReservationDAO reservationDAO = new ReservationDAO();

    public boolean ajouter(Facture facture) {
        String sql = "INSERT INTO Facture (reservationId, dateFacturation, montantTotal, statutPaiement) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, facture.getReservation().getId());
            pstmt.setString(2, facture.getDateFacturation().toString());
            pstmt.setDouble(3, facture.getMontantTotal());
            pstmt.setString(4, facture.getStatutPaiement());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        facture.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean marquerCommePaye(int id) {
        String sql = "UPDATE Facture SET statutPaiement = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "Payée");
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Facture getById(int id) {
        String sql = "SELECT * FROM Facture WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Facture facture = new Facture();
                    facture.setId(rs.getInt("id"));
                    
                    // Récupérer la réservation associée
                    Reservation reservation = reservationDAO.getById(rs.getInt("reservationId"));
                    facture.setReservation(reservation);
                    
                    facture.setDateFacturation(LocalDate.parse(rs.getString("dateFacturation")));
                    facture.setMontantTotal(rs.getDouble("montantTotal"));
                    facture.setStatutPaiement(rs.getString("statutPaiement"));
                    
                    return facture;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Facture getByReservationId(int reservationId) {
        String sql = "SELECT * FROM Facture WHERE reservationId = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Facture facture = new Facture();
                    facture.setId(rs.getInt("id"));
                    
                    // Récupérer la réservation associée
                    Reservation reservation = reservationDAO.getById(rs.getInt("reservationId"));
                    facture.setReservation(reservation);
                    
                    facture.setDateFacturation(LocalDate.parse(rs.getString("dateFacturation")));
                    facture.setMontantTotal(rs.getDouble("montantTotal"));
                    facture.setStatutPaiement(rs.getString("statutPaiement"));
                    
                    return facture;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Facture> getAll() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM Facture";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Facture facture = new Facture();
                facture.setId(rs.getInt("id"));
                
                // Récupérer la réservation associée
                Reservation reservation = reservationDAO.getById(rs.getInt("reservationId"));
                facture.setReservation(reservation);
                
                facture.setDateFacturation(LocalDate.parse(rs.getString("dateFacturation")));
                facture.setMontantTotal(rs.getDouble("montantTotal"));
                facture.setStatutPaiement(rs.getString("statutPaiement"));
                
                factures.add(facture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return factures;
    }
    
    public List<Facture> getFacturesNonPayees() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM Facture WHERE statutPaiement = 'Non payée'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Facture facture = new Facture();
                facture.setId(rs.getInt("id"));
                
                // Récupérer la réservation associée
                Reservation reservation = reservationDAO.getById(rs.getInt("reservationId"));
                facture.setReservation(reservation);
                
                facture.setDateFacturation(LocalDate.parse(rs.getString("dateFacturation")));
                facture.setMontantTotal(rs.getDouble("montantTotal"));
                facture.setStatutPaiement(rs.getString("statutPaiement"));
                
                factures.add(facture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return factures;
    }
    
    public double getTotalRevenueParMois(int mois, int annee) {
        String sql = "SELECT SUM(montantTotal) as revenuTotal FROM Facture WHERE statutPaiement = 'Payée' AND strftime('%m', dateFacturation) = ? AND strftime('%Y', dateFacturation) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String moisStr = mois < 10 ? "0" + mois : String.valueOf(mois);
            pstmt.setString(1, moisStr);
            pstmt.setString(2, String.valueOf(annee));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("revenuTotal");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
}
