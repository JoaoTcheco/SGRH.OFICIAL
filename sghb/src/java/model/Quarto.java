/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 *
 * @author User
 */
/**
 * Molde para guardar as informações de um quarto do hotel.
 */
public class Quarto implements Serializable {

    /**
     * Enum para representar os possíveis estados de um quarto.
     * Usar um enum ajuda a evitar erros de escrita e torna o código mais claro.
     */
    public enum StatusQuarto {
        DISPONIVEL("Disponível"), // O quarto está livre para ser reservado
        OCUPADO("Ocupado"),       // Um hóspede está atualmente no quarto
        RESERVADO("Reservado"),   // O quarto está reservado para uma data futura
        EM_MANUTENCAO("Em Manutenção"), // O quarto não pode ser usado porque está em manutenção
        A_LIMPAR("A Limpar");     // O quarto está a ser limpo depois de um hóspede sair

        private final String descricao; // Descrição amigável do estado

        StatusQuarto(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    private int idQuarto; // Número de identificação único do quarto
    private String numeroQuarto; // Número do quarto (ex: "101", "20A")
    private StatusQuarto statusQuarto; // O estado atual do quarto (usa o enum acima)

    // Construtor vazio
    public Quarto() {
    }

    // --- Getters e Setters ---

    public int getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(int idQuarto) {
        this.idQuarto = idQuarto;
    }

    public String getNumeroQuarto() {
        return numeroQuarto;
    }

    public void setNumeroQuarto(String numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public StatusQuarto getStatusQuarto() {
        return statusQuarto;
    }

    public void setStatusQuarto(StatusQuarto statusQuarto) {
        this.statusQuarto = statusQuarto;
    }
}

