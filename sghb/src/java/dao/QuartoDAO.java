/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Quarto.StatusQuarto; // Importa o Enum StatusQuarto
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Quarto;

/**
 * Classe para aceder aos dados dos Quartos na base de dados.
 * Sabe como buscar e atualizar informações na tabela 'quartos'.
 */
public class QuartoDAO {

    /**
     * Busca todos os quartos registados na base de dados.
     * Ordena os quartos pelo número para facilitar a visualização.
     * @return Uma lista (List) de objetos Quarto. Se não houver quartos, devolve uma lista vazia.
     */
    public List<Quarto> listarTodos() {
        String sql = "SELECT * FROM quartos ORDER BY numero_quarto ASC";
        List<Quarto> listaDeQuartos = new ArrayList<>(); // Cria uma lista vazia para guardar os quartos

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // Executa a busca

            // Enquanto houver linhas no resultado (rs.next() for true)...
            while (rs.next()) {
                Quarto quarto = new Quarto(); // Cria um objeto Quarto para cada linha
                // Preenche o objeto com os dados da linha atual
                quarto.setIdQuarto(rs.getInt("id_quarto"));
                quarto.setNumeroQuarto(rs.getString("numero_quarto"));
                
                // Converte o texto do estado do quarto (ex: "DISPONÍVEL") para o nosso Enum StatusQuarto
                // O replace(" ", "_") é para casos como "Em Manutenção" virar "EM_MANUTENCAO"
                String statusDoBanco = rs.getString("status_quarto");
                if (statusDoBanco != null) {
                    quarto.setStatusQuarto(StatusQuarto.valueOf(statusDoBanco.toUpperCase().replace(" ", "_")));
                }
                
                listaDeQuartos.add(quarto); // Adiciona o quarto à lista
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os quartos: " + e.getMessage());
        }
        return listaDeQuartos; // Devolve a lista de quartos
    }

    /**
     * Atualiza o estado (status) de um quarto específico na base de dados.
     * @param idQuarto O ID do quarto que vai ser atualizado.
     * @param novoStatus O novo estado para o quarto (ex: StatusQuarto.OCUPADO).
     * @return true se a atualização correu bem, false se deu algum erro.
     */
    public boolean atualizarStatus(int idQuarto, StatusQuarto novoStatus) {
        String sql = "UPDATE quartos SET status_quarto = ? WHERE id_quarto = ?";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Usamos novoStatus.name() para guardar o nome do enum (ex: "OCUPADO") como texto na base de dados.
            stmt.setString(1, novoStatus.name()); 
            stmt.setInt(2, idQuarto);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; // Se afetou alguma linha, deu certo
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o estado do quarto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista os quartos que estão disponíveis num determinado período.
     * Um quarto está disponível se o seu estado for 'Disponível' E
     * se não tiver nenhuma reserva confirmada que cruze com as datas pedidas.
     * @param dataCheckin A data de entrada desejada.
     * @param dataCheckout A data de saída desejada.
     * @return Uma lista de quartos disponíveis.
     */
    public List<Quarto> listarDisponiveis(LocalDate dataCheckin, LocalDate dataCheckout) {
        // Este SQL é um pouco mais complexo:
        // 1. Pega quartos com status 'Disponível'.
        // 2. E (AND) verifica se NÃO EXISTE (NOT EXISTS) nenhuma reserva para esse quarto
        //    que NÃO esteja cancelada E que se sobreponha com as datas pedidas.
        // A lógica de sobreposição é: (checkin_da_reserva < dataCheckout_nova) E (checkout_da_reserva > dataCheckin_nova)
        String sql = "SELECT * FROM quartos q " +
                     "WHERE q.status_quarto = 'Disponível' " + // Ou q.status_quarto = 'A_LIMPAR' dependendo da regra
                     "AND NOT EXISTS (" +
                     "  SELECT 1 FROM reservas r " +
                     "  WHERE r.id_quarto = q.id_quarto " +
                     "  AND r.status_reserva <> 'CANCELADA' " + // Ignora reservas canceladas
                     "  AND (r.data_checkin < ? AND r.data_checkout > ?)" + // Verifica sobreposição
                     ") ORDER BY q.numero_quarto ASC";
        
        List<Quarto> quartosDisponiveis = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Atenção à ordem dos parâmetros para a lógica de sobreposição
            stmt.setObject(1, dataCheckout); // data_checkout_nova
            stmt.setObject(2, dataCheckin);  // data_checkin_nova

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Quarto quarto = new Quarto();
                    quarto.setIdQuarto(rs.getInt("id_quarto"));
                    quarto.setNumeroQuarto(rs.getString("numero_quarto"));
                    String statusDoBanco = rs.getString("status_quarto");
                    if (statusDoBanco != null) {
                         quarto.setStatusQuarto(StatusQuarto.valueOf(statusDoBanco.toUpperCase().replace(" ", "_")));
                    }
                    quartosDisponiveis.add(quarto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar quartos disponíveis: " + e.getMessage());
        }
        return quartosDisponiveis;
    }
}


