package com.hotel.dao;

import com.hotel.model.Chambre;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChambreDAO {

    public boolean ajouter(Chambre chambre) {
        String sql = "INSERT INTO Chambre (numero, type, statut, prix) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, chambre.getNumero());
            pstmt.setString(2, chambre.getType());
            pstmt.setString(3, chambre.getStatut());
            pstmt.setDouble(4, chambre.getPrix());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean modifier(Chambre chambre) {
        String sql = "UPDATE Chambre SET type = ?, statut = ?, prix = ? WHERE numero = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, chambre.getType());
            pstmt.setString(2, chambre.getStatut());
            pstmt.setDouble(3, chambre.getPrix());
            pstmt.setInt(4, chambre.getNumero());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean supprimer(int numero) {
        String sql = "DELETE FROM Chambre WHERE numero = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, numero);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Chambre getById(int numero) {
        String sql = "SELECT * FROM Chambre WHERE numero = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, numero);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Chambre chambre = new Chambre();
                    chambre.setNumero(rs.getInt("numero"));
                    chambre.setType(rs.getString("type"));
                    chambre.setStatut(rs.getString("statut"));
                    chambre.setPrix(rs.getDouble("prix"));
                    return chambre;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Chambre> getAll() {
        List<Chambre> chambres = new ArrayList<>();
        String sql = "SELECT * FROM Chambre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Chambre chambre = new Chambre();
                chambre.setNumero(rs.getInt("numero"));
                chambre.setType(rs.getString("type"));
                chambre.setStatut(rs.getString("statut"));
                chambre.setPrix(rs.getDouble("prix"));
                chambres.add(chambre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chambres;
    }
    
    public List<Chambre> getChambresDisponibles() {
        List<Chambre> chambres = new ArrayList<>();
        String sql = "SELECT * FROM Chambre WHERE statut = 'Disponible'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Chambre chambre = new Chambre();
                chambre.setNumero(rs.getInt("numero"));
                chambre.setType(rs.getString("type"));
                chambre.setStatut(rs.getString("statut"));
                chambre.setPrix(rs.getDouble("prix"));
                chambres.add(chambre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chambres;
    }
    
    public void updateStatut(int numero, String statut) {
        String sql = "UPDATE Chambre SET statut = ? WHERE numero = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, statut);
            pstmt.setInt(2, numero);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
