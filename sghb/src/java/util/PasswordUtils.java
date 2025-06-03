/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import org.mindrot.jbcrypt.BCrypt; // Importamos a biblioteca para proteger senhas

/**
 * Classe com funções para ajudar a proteger as senhas dos utilizadores.
 * Usa um método chamado BCrypt, que é muito seguro.
 */
public class PasswordUtils {

    /**
     * Transforma uma senha normal (texto puro) num código seguro (hash).
     * Este código é o que guardamos na base de dados, não a senha original.
     * @param senhaPura A senha que o utilizador escreveu.
     * @return Um código (hash) gerado a partir da senha.
     */
    public static String hashSenha(String senhaPura) {
        // O BCrypt.gensalt() adiciona um "sal" aleatório para tornar o hash ainda mais seguro.
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    /**
     * Verifica se uma senha que o utilizador escreveu é igual à senha segura (hash)
     * que está guardada na base de dados.
     * @param senhaPura A senha que o utilizador escreveu para entrar.
     * @param senhaHash O código (hash) da senha que está guardado na base de dados.
     * @return true se as senhas combinam, false se não combinam.
     */
    public static boolean verificarSenha(String senhaPura, String senhaHash) {
        // O BCrypt.checkpw compara a senha pura com o hash.
        return BCrypt.checkpw(senhaPura, senhaHash);
    }
}

