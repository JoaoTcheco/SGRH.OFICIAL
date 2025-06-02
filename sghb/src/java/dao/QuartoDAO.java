/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.sun.jdi.connect.spi.Connection;
import java.util.ArrayList;
import java.util.List;
import model.Quarto;

import model.Quarto.StatusQuarto; // Importa o Enum
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author User
 */
public class QuartoDAO {

    // NOVO MÉTODO: Listar todos os quartos
    public List<Quarto> listarTodos() {
        String sql = "SELECT * FROM quartos ORDER BY numero_quarto ASC";
        List<Quarto> quartos = new ArrayList<>();

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Quarto quarto = new Quarto();
                quarto.setIdQuarto(rs.getInt("id_quarto"));
                quarto.setNumeroQuarto(rs.getString("numero_quarto"));
                
                // Converte a String do banco para o Enum
                quarto.setStatusQuarto(StatusQuarto.valueOf(rs.getString("status_quarto").toUpperCase().replace(" ", "_")));
                
                quartos.add(quarto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar quartos: " + e.getMessage());
        }
        return quartos;
    }

    // NOVO MÉTODO: Atualizar o status de um quarto
    public boolean atualizarStatus(int idQuarto, StatusQuarto novoStatus) {
        String sql = "UPDATE quartos SET status_quarto = ? WHERE id_quarto = ?";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus.name()); // Salva o nome do Enum no banco
            stmt.setInt(2, idQuarto);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do quarto: " + e.getMessage());
            return false;
        }
    }
}

