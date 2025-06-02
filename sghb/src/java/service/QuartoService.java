/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.QuartoDAO;

import model.Quarto.StatusQuarto;
import dao.QuartoDAO;
import java.util.List;
import model.Quarto;

/**
 *
 * @author User
 */
public class QuartoService {

    private final QuartoDAO quartoDAO;

    public QuartoService() {
        this.quartoDAO = new QuartoDAO();
    }

    public List<Quarto> listarTodosQuartos() {
        return this.quartoDAO.listarTodos();
    }
    
    public boolean atualizarStatusQuarto(int idQuarto, StatusQuarto novoStatus) {
        // Aqui poderíamos ter lógicas de negócio no futuro.
        // Por agora, apenas chamamos o DAO.
        return this.quartoDAO.atualizarStatus(idQuarto, novoStatus);
    }
}

