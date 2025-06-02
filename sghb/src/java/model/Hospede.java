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
 * Molde para guardar as informações de um hóspede do hotel.
 */
public class Hospede implements Serializable {

    private int idHospede; // Número de identificação único do hóspede
    private String nomeCompleto; // Nome completo do hóspede
    private String documentoIdentidadeNumero; // Número do documento (ex: BI, Passaporte)
    private String telefone; // Número de telefone do hóspede
    private String email; // Endereço de email do hóspede (opcional)

    // Construtor vazio
    public Hospede() {
    }

    // --- Getters e Setters ---

    public int getIdHospede() {
        return idHospede;
    }

    public void setIdHospede(int idHospede) {
        this.idHospede = idHospede;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getDocumentoIdentidadeNumero() {
        return documentoIdentidadeNumero;
    }

    public void setDocumentoIdentidadeNumero(String documentoIdentidadeNumero) {
        this.documentoIdentidadeNumero = documentoIdentidadeNumero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


