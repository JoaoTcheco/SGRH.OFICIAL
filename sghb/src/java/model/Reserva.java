/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDate; // Para trabalhar com datas (check-in, check-out)

/**
 * Molde para guardar as informações de uma reserva de quarto.
 */
public class Reserva implements Serializable {
    
    /**
     * Enum para representar os possíveis estados de uma reserva.
     */
    public enum StatusReserva {
        CONFIRMADA("Confirmada"), // A reserva está confirmada
        CANCELADA("Cancelada"),   // A reserva foi cancelada
        CONCLUIDA("Concluída"),   // O hóspede já fez check-out
        CHECKED_IN("Checked-In"); // O hóspede já fez check-in e está no hotel

        private final String descricao;

        StatusReserva(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    private int idReserva; // Número de identificação único da reserva
    private int idHospede; // ID do hóspede que fez a reserva
    private int idQuarto; // ID do quarto que foi reservado
    private int idUsuarioCriacao; // ID do recepcionista que criou a reserva
    private LocalDate dataCheckin; // Data de entrada do hóspede
    private LocalDate dataCheckout; // Data de saída do hóspede
    private StatusReserva statusReserva; // O estado atual da reserva
    
    // Estes campos são para facilitar mostrar informações na lista de reservas
    private Hospede hospede; // O objeto Hóspede completo (para pegar o nome)
    private Quarto quarto;   // O objeto Quarto completo (para pegar o número do quarto)

    // Construtor vazio
    public Reserva() {
    }

    // --- Getters e Setters ---

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdHospede() {
        return idHospede;
    }

    public void setIdHospede(int idHospede) {
        this.idHospede = idHospede;
    }

    public int getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(int idQuarto) {
        this.idQuarto = idQuarto;
    }

    public int getIdUsuarioCriacao() {
        return idUsuarioCriacao;
    }

    public void setIdUsuarioCriacao(int idUsuarioCriacao) {
        this.idUsuarioCriacao = idUsuarioCriacao;
    }

    public LocalDate getDataCheckin() {
        return dataCheckin;
    }

    public void setDataCheckin(LocalDate dataCheckin) {
        this.dataCheckin = dataCheckin;
    }

    public LocalDate getDataCheckout() {
        return dataCheckout;
    }

    public void setDataCheckout(LocalDate dataCheckout) {
        this.dataCheckout = dataCheckout;
    }

    public StatusReserva getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(StatusReserva statusReserva) {
        this.statusReserva = statusReserva;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public void setHospede(Hospede hospede) {
        this.hospede = hospede;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }
}


