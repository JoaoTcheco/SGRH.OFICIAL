/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Hospede;
import model.Quarto;
import model.Reserva;

/**
 * Classe para aceder aos dados das Reservas na base de dados.
 * Sabe como adicionar e listar reservas.
 */
public class ReservaDAO {

    /**
     * Adiciona uma nova reserva à base de dados.
     * @param reserva O objeto Reserva com os dados a serem guardados.
     * @return true se a reserva foi adicionada com sucesso, false se deu erro.
     */
    public boolean adicionar(Reserva reserva) {
        String sql = "INSERT INTO reservas (id_hospede, id_quarto, id_usuario_criacao, data_checkin, data_checkout, status_reserva) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getIdHospede());
            stmt.setInt(2, reserva.getIdQuarto());
            stmt.setInt(3, reserva.getIdUsuarioCriacao());
            // Para datas LocalDate, usamos setObject
            stmt.setObject(4, reserva.getDataCheckin()); 
            stmt.setObject(5, reserva.getDataCheckout());
            // Guardamos o nome do enum (ex: "CONFIRMADA") como texto
            stmt.setString(6, reserva.getStatusReserva().name()); 

            return stmt.executeUpdate() > 0; // Retorna true se alguma linha foi inserida
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar nova reserva: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca todas as reservas registadas, ordenadas pela data de check-in mais recente primeiro.
     * Também busca o nome do hóspede e o número do quarto associado a cada reserva
     * usando um JOIN SQL para facilitar a exibição na lista.
     * @return Uma lista de objetos Reserva.
     */
    public List<Reserva> listarTodas() {
        // Este SQL usa JOIN para juntar dados de 3 tabelas: reservas, hospedes, e quartos.
        String sql = "SELECT r.id_reserva, r.id_hospede, r.id_quarto, r.id_usuario_criacao, " +
                     "r.data_checkin, r.data_checkout, r.status_reserva, " +
                     "h.nome_completo AS nome_hospede, q.numero_quarto " + // Pega nome do hóspede e número do quarto
                     "FROM reservas r " +
                     "JOIN hospedes h ON r.id_hospede = h.id_hospede " + // Junta com a tabela hospedes
                     "JOIN quartos q ON r.id_quarto = q.id_quarto " +   // Junta com a tabela quartos
                     "ORDER BY r.data_checkin DESC"; // Ordena pela data de check-in
        
        List<Reserva> listaDeReservas = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("id_reserva"));
                reserva.setIdHospede(rs.getInt("id_hospede"));
                reserva.setIdQuarto(rs.getInt("id_quarto"));
                reserva.setIdUsuarioCriacao(rs.getInt("id_usuario_criacao"));
                // Converte a data do banco (java.sql.Date) para LocalDate
                reserva.setDataCheckin(rs.getDate("data_checkin").toLocalDate());
                reserva.setDataCheckout(rs.getDate("data_checkout").toLocalDate());
                reserva.setStatusReserva(Reserva.StatusReserva.valueOf(rs.getString("status_reserva")));

                // Cria e preenche o objeto Hospede associado (só com o nome, para a lista)
                Hospede hospedeAssociado = new Hospede();
                hospedeAssociado.setIdHospede(rs.getInt("id_hospede")); // Boa prática ter o ID também
                hospedeAssociado.setNomeCompleto(rs.getString("nome_hospede"));
                reserva.setHospede(hospedeAssociado);

                // Cria e preenche o objeto Quarto associado (só com o número, para a lista)
                Quarto quartoAssociado = new Quarto();
                quartoAssociado.setIdQuarto(rs.getInt("id_quarto")); // Boa prática ter o ID também
                quartoAssociado.setNumeroQuarto(rs.getString("numero_quarto"));
                reserva.setQuarto(quartoAssociado);

                listaDeReservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as reservas: " + e.getMessage());
        }
        return listaDeReservas;
    }
    
    // NOTA: Faltam métodos para buscar reserva por ID, atualizar ou cancelar uma reserva.
    // Estes seriam adicionados num sistema mais completo.
}


