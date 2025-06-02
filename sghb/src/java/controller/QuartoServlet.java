/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import model.Quarto;
import service.QuartoService;

/**
 *
 * @author User
 */
@WebServlet(name = "QuartoServlet", urlPatterns = {"/recepcionista/quartos"})
public class QuartoServlet extends HttpServlet {
    private final QuartoService quartoService = new QuartoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Quarto> listaQuartos = quartoService.listarTodosQuartos();
        request.setAttribute("listaQuartos", listaQuartos);
        
        // Encaminha para a página JSP que vai exibir os quartos
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/recepcionista/quartos.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idQuarto = Integer.parseInt(request.getParameter("idQuarto"));
            String novoStatusStr = request.getParameter("novoStatus");
            
            // Converte a String para o nosso Enum StatusQuarto
            Quarto.StatusQuarto novoStatus = Quarto.StatusQuarto.valueOf(novoStatusStr);

            quartoService.atualizarStatusQuarto(idQuarto, novoStatus);

            // Redireciona de volta para a lista de quartos para ver a alteração
            response.sendRedirect(request.getContextPath() + "/recepcionista/quartos");
            
        } catch (NumberFormatException e) {
            // Lida com erro de conversão do ID
            response.sendRedirect(request.getContextPath() + "/recepcionista/quartos?erro=id_invalido");
        } catch (IllegalArgumentException e) {
            // Lida com erro de conversão do status
            response.sendRedirect(request.getContextPath() + "/recepcionista/quartos?erro=status_invalido");
        }
    }
}

