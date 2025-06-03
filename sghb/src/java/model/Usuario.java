/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable; // Ajuda a guardar o objeto em sessão

/**
 * Molde para guardar as informações de um utilizador do sistema (recepcionista, técnico).
 * Implementa Serializable para que objetos Usuario possam ser guardados na sessão HTTP.
 */
public class Usuario implements Serializable {

    private int idUsuario; // Número de identificação único do utilizador
    private String nomeCompleto; // Nome completo do utilizador
    private String cargo; // Cargo do utilizador (ex: "Recepcionista")
    private String username; // Nome que o utilizador usa para entrar no sistema
    private String senhaHash; // A senha do utilizador, guardada de forma segura (não a senha real!)
    private boolean primeiroLogin; // Indica se é a primeira vez que o utilizador entra (true) ou não (false)
    private boolean ativo; // Indica se a conta do utilizador está ativa (true) ou desativada (false)

    // Construtor vazio (opcional, mas boa prática)
    public Usuario() {
    }

    // --- Getters e Setters ---
    // Métodos para buscar (get) e definir (set) cada informação do utilizador.

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public boolean isPrimeiroLogin() {
        return primeiroLogin;
    }

    public void setPrimeiroLogin(boolean primeiroLogin) {
        this.primeiroLogin = primeiroLogin;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
