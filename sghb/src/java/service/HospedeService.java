/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.HospedeDAO;
import java.util.List;
import model.Hospede;

/**
 * Contém a lógica de negócio para as operações relacionadas com Hóspedes.
 * Funciona como um intermediário entre os Servlets (Controllers) e o HospedeDAO.
 */
public class HospedeService {

    // O HospedeService tem uma "ligação" com o HospedeDAO.
    private final HospedeDAO hospedeDAO = new HospedeDAO();

    /**
     * Pede ao HospedeDAO para adicionar um novo hóspede à base de dados.
     * @param hospede O objeto Hospede com os dados a serem guardados.
     * @return O objeto Hospede com o ID preenchido, ou null se der erro.
     */
    public Hospede adicionar(Hospede hospede) {
        // Poderia ter validações aqui antes de chamar o DAO.
        // Ex: verificar se o nome não está vazio, se o documento é válido, etc.
        return hospedeDAO.adicionar(hospede);
    }

    /**
     * Pede ao HospedeDAO uma lista com todos os hóspedes registados.
     * @return Uma lista de objetos Hospede.
     */
    public List<Hospede> listarTodos() {
        return hospedeDAO.listarTodos();
    }

    /**
     * Pede ao HospedeDAO para buscar um hóspede específico pelo seu ID.
     * @param id O ID do hóspede a ser procurado.
     * @return Um objeto Hospede se encontrado, ou null caso contrário.
     */
    public Hospede buscarPorId(int id) {
        return hospedeDAO.buscarPorId(id);
    }

    /**
     * Pede ao HospedeDAO para atualizar os dados de um hóspede existente.
     * @param hospede O objeto Hospede com os dados atualizados.
     * @return true se a atualização correu bem, false se deu erro.
     */
    public boolean atualizar(Hospede hospede) {
        // Poderia ter validações aqui.
        return hospedeDAO.atualizar(hospede);
    }

    /**
     * Pede ao HospedeDAO para apagar um hóspede da base de dados.
     * @param id O ID do hóspede a ser removido.
     * @return true se a remoção correu bem, false se deu erro.
     */
    public boolean remover(int id) {
        // Poderia ter regras aqui, ex: "Não apagar hóspede se tiver reserva ativa".
        return hospedeDAO.remover(id);
    }
}

