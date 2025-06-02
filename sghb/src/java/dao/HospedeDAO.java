/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Usado para pegar o ID gerado automaticamente
import java.util.ArrayList;
import java.util.List;
import model.Hospede;

/**
 * Classe para aceder aos dados dos Hóspedes na base de dados.
 * Contém métodos para Criar, Ler, Atualizar e Apagar (CRUD) hóspedes.
 */
public class HospedeDAO {

    /**
     * Adiciona um novo hóspede à base de dados.
     * @param hospede O objeto Hospede com os dados a serem guardados.
     * @return O objeto Hospede com o ID preenchido (gerado pela base de dados), ou null se der erro.
     */
    public Hospede adicionar(Hospede hospede) {
        String sql = "INSERT INTO hospedes (nome_completo, documento_identidade_numero, telefone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection();
             // Statement.RETURN_GENERATED_KEYS diz para a base de dados nos devolver o ID que ela gerou.
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, hospede.getNomeCompleto());
            stmt.setString(2, hospede.getDocumentoIdentidadeNumero());
            stmt.setString(3, hospede.getTelefone());
            stmt.setString(4, hospede.getEmail()); // Pode ser null se o hóspede não tiver email

            int linhasAfetadas = stmt.executeUpdate(); // Executa o INSERT

            if (linhasAfetadas > 0) {
                // Se o INSERT deu certo, tentamos pegar o ID gerado.
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        hospede.setIdHospede(rs.getInt(1)); // Pega o ID da primeira coluna
                    }
                }
                return hospede; // Devolve o hóspede com o ID
            } else {
                return null; // Se não afetou linhas, algo deu errado
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar novo hóspede: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca todos os hóspedes registados na base de dados.
     * Ordena os hóspedes pelo nome completo.
     * @return Uma lista de objetos Hospede.
     */
    public List<Hospede> listarTodos() {
        String sql = "SELECT * FROM hospedes ORDER BY nome_completo ASC";
        List<Hospede> listaDeHospedes = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Hospede hospede = new Hospede();
                hospede.setIdHospede(rs.getInt("id_hospede"));
                hospede.setNomeCompleto(rs.getString("nome_completo"));
                hospede.setDocumentoIdentidadeNumero(rs.getString("documento_identidade_numero"));
                hospede.setTelefone(rs.getString("telefone"));
                hospede.setEmail(rs.getString("email"));
                listaDeHospedes.add(hospede);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os hóspedes: " + e.getMessage());
        }
        return listaDeHospedes;
    }

    /**
     * Busca um hóspede específico pelo seu ID.
     * @param id O ID do hóspede a ser procurado.
     * @return Um objeto Hospede se encontrado, ou null caso contrário.
     */
    public Hospede buscarPorId(int id) {
        String sql = "SELECT * FROM hospedes WHERE id_hospede = ?";
        Hospede hospedeEncontrado = null;
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Preenche o '?' com o ID
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hospedeEncontrado = new Hospede();
                    hospedeEncontrado.setIdHospede(rs.getInt("id_hospede"));
                    hospedeEncontrado.setNomeCompleto(rs.getString("nome_completo"));
                    hospedeEncontrado.setDocumentoIdentidadeNumero(rs.getString("documento_identidade_numero"));
                    hospedeEncontrado.setTelefone(rs.getString("telefone"));
                    hospedeEncontrado.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar hóspede por ID: " + e.getMessage());
        }
        return hospedeEncontrado;
    }

    /**
     * Atualiza os dados de um hóspede existente na base de dados.
     * @param hospede O objeto Hospede com os dados atualizados. O ID deve estar preenchido.
     * @return true se a atualização correu bem, false se deu erro.
     */
    public boolean atualizar(Hospede hospede) {
        String sql = "UPDATE hospedes SET nome_completo = ?, documento_identidade_numero = ?, telefone = ?, email = ? WHERE id_hospede = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hospede.getNomeCompleto());
            stmt.setString(2, hospede.getDocumentoIdentidadeNumero());
            stmt.setString(3, hospede.getTelefone());
            stmt.setString(4, hospede.getEmail());
            stmt.setInt(5, hospede.getIdHospede()); // O ID do hóspede a ser atualizado
            
            return stmt.executeUpdate() > 0; // Retorna true se alguma linha foi afetada
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar dados do hóspede: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove (apaga) um hóspede da base de dados pelo seu ID.
     * CUIDADO: Esta ação não pode ser desfeita facilmente!
     * @param id O ID do hóspede a ser removido.
     * @return true se a remoção correu bem, false se deu erro.
     */
    public boolean remover(int id) {
        // Antes de apagar um hóspede, seria bom verificar se ele tem reservas ativas.
        // Para um sistema básico, vamos apagar diretamente.
        String sql = "DELETE FROM hospedes WHERE id_hospede = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; // Retorna true se alguma linha foi afetada
        } catch (SQLException e) {
            // Um erro comum aqui é se o hóspede estiver ligado a uma reserva (foreign key constraint).
            System.err.println("Erro ao remover hóspede: " + e.getMessage());
            return false;
        }
    }
}

