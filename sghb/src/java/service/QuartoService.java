/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Quarto.StatusQuarto;
import dao.QuartoDAO;

import java.util.List;
import model.Quarto;

/**
 * Esta classe contém a lógica de negócio para os Quartos.
 * Ela usa o QuartoDAO para buscar ou salvar dados na base de dados.
 * É como um "gerente" para as operações relacionadas com quartos.
 */
public class QuartoService {

    private final QuartoDAO quartoDAO; // O QuartoService tem uma "ligação" com o QuartoDAO

    // Construtor: quando um QuartoService é criado, ele automaticamente cria um QuartoDAO.
    public QuartoService() {
        this.quartoDAO = new QuartoDAO();
    }

    /**
     * Pede ao QuartoDAO para buscar todos os quartos da base de dados.
     * @return Uma lista (List) de todos os objetos Quarto.
     */
    public List<Quarto> listarTodosQuartos() {
        return this.quartoDAO.listarTodos();
    }
    
    /**
     * Pede ao QuartoDAO para atualizar o estado (status) de um quarto específico.
     * @param idQuarto O ID do quarto que queremos mudar.
     * @param novoStatus O novo estado para o quarto (ex: StatusQuarto.OCUPADO).
     * @return true se a atualização na base de dados deu certo, false se deu algum erro.
     */
    public boolean atualizarStatusQuarto(int idQuarto, StatusQuarto novoStatus) {
        // No futuro, poderíamos adicionar aqui mais regras de negócio.
        // Por exemplo: "Não se pode mudar o estado para 'Disponível' se o quarto ainda tiver uma reserva ativa".
        // Por agora, o QuartoService apenas passa o pedido para o QuartoDAO.
        return this.quartoDAO.atualizarStatus(idQuarto, novoStatus);
    }
}

