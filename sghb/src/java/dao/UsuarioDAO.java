/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe para aceder aos dados dos Utilizadores na base de dados.
 * Sabe como buscar e atualizar informações na tabela 'usuarios'.
 */
public class UsuarioDAO {

    /**
     * Procura um utilizador na base de dados pelo seu nome de utilizador (username).
     * Só retorna o utilizador se ele estiver ativo.
     * @param username O nome de utilizador a ser procurado.
     * @return Um objeto Usuario com os dados encontrados, ou null se não encontrar ninguém.
     */
    public Usuario buscarPorUsername(String username) {
        // O comando SQL para buscar o utilizador.
        // O '?' é um espaço que será preenchido depois (para segurança).
        String sql = "SELECT * FROM usuarios WHERE username = ? AND ativo = TRUE";
        Usuario usuarioEncontrado = null;

        // O 'try-with-resources' garante que a conexão e o PreparedStatement são fechados no fim,
        // mesmo que aconteça um erro. É uma boa prática.
        try (Connection conn = ConexaoBD.getConnection(); // Pega uma ligação à base de dados
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara o comando SQL
            
            stmt.setString(1, username); // Preenche o primeiro '?' com o username que recebemos
            
            // Executa o comando SQL e guarda o resultado (que é uma tabela) no ResultSet.
            ResultSet rs = stmt.executeQuery();

            // Se o ResultSet tiver pelo menos uma linha, significa que encontrámos o utilizador.
            if (rs.next()) {
                usuarioEncontrado = new Usuario(); // Cria um novo objeto Usuario
                // Preenche o objeto Usuario com os dados da base de dados
                usuarioEncontrado.setIdUsuario(rs.getInt("id_usuario"));
                usuarioEncontrado.setNomeCompleto(rs.getString("nome_completo"));
                usuarioEncontrado.setCargo(rs.getString("cargo"));
                usuarioEncontrado.setUsername(rs.getString("username"));
                usuarioEncontrado.setSenhaHash(rs.getString("senha_hash"));
                usuarioEncontrado.setPrimeiroLogin(rs.getBoolean("primeiro_login"));
                usuarioEncontrado.setAtivo(rs.getBoolean("ativo"));
            }

        } catch (SQLException e) {
            // Se der algum erro ao falar com a base de dados, mostra uma mensagem.
            System.err.println("Erro ao buscar utilizador por username: " + e.getMessage());
            // Numa aplicação real, seria melhor usar um sistema de logs mais avançado.
        }
        return usuarioEncontrado; // Devolve o utilizador encontrado (ou null se não encontrou)
    }

    /**
     * Atualiza a senha de um utilizador na base de dados.
     * Também marca 'primeiro_login' como false, porque o utilizador já mudou a senha.
     * @param idUsuario O ID do utilizador cuja senha vai ser mudada.
     * @param novaSenhaHash A nova senha, já transformada em código seguro (hash).
     * @return true se a atualização correu bem, false se deu algum erro.
     */
    public boolean atualizarSenha(int idUsuario, String novaSenhaHash) {
        String sql = "UPDATE usuarios SET senha_hash = ?, primeiro_login = FALSE WHERE id_usuario = ?";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaSenhaHash); // Preenche o primeiro '?' com a nova senha (hash)
            stmt.setInt(2, idUsuario);      // Preenche o segundo '?' com o ID do utilizador
            
            // O executeUpdate() diz quantas linhas da tabela foram afetadas pelo comando.
            int linhasAfetadas = stmt.executeUpdate();
            // Se pelo menos uma linha foi afetada, a atualização deu certo.
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar senha do utilizador: " + e.getMessage());
            return false; // Retorna false se deu erro
        }
    }
    
    // NOTA: No futuro, podemos adicionar aqui métodos para o Técnico criar,
    // editar ou apagar contas de recepcionistas.
}


