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

import model.Quarto;
import service.QuartoService;

import java.io.IOException;
import java.util.List;

/**
 * Este Servlet lida com os pedidos relacionados com Quartos.
 * Responde ao URL "/recepcionista/quartos".
 */
@WebServlet(name = "QuartoServlet", urlPatterns = {"/recepcionista/quartos"})
public class QuartoServlet extends HttpServlet {
    // Cria uma instância do QuartoService para usar.
    private final QuartoService quartoService = new QuartoService();

    /**
     * Lida com pedidos HTTP GET.
     * Usado principalmente para mostrar a lista de quartos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pede ao serviço a lista de todos os quartos.
        List<Quarto> todosOsQuartos = quartoService.listarTodosQuartos();
        // Coloca a lista no 'request' para que a página JSP possa aceder a ela.
        request.setAttribute("listaQuartos", todosOsQuartos);
        
        // Encaminha o pedido para a página JSP que vai mostrar os quartos.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/recepcionista/quartos.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Lida com pedidos HTTP POST.
     * Usado para atualizar o estado de um quarto.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Pega os dados enviados pelo formulário da página quartos.jsp
            int idDoQuarto = Integer.parseInt(request.getParameter("idQuarto"));
            String novoStatusComoTexto = request.getParameter("novoStatus");
            
            // Converte o texto do novo estado (ex: "OCUPADO") para o nosso Enum Quarto.StatusQuarto.
            Quarto.StatusQuarto novoStatusEnum = Quarto.StatusQuarto.valueOf(novoStatusComoTexto);

            // Pede ao serviço para atualizar o estado do quarto.
            quartoService.atualizarStatusQuarto(idDoQuarto, novoStatusEnum);

            // Depois de atualizar, redireciona de volta para a lista de quartos
            // para que o utilizador veja a alteração.
            response.sendRedirect(request.getContextPath() + "/recepcionista/quartos");
            
        } catch (NumberFormatException e) {
            // Se o idQuarto não for um número válido.
            System.err.println("Erro ao converter ID do quarto: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/recepcionista/quartos?erro=id_quarto_invalido");
        } catch (IllegalArgumentException e) {
            // Se o novoStatus não for um valor válido do nosso Enum.
            System.err.println("Erro ao converter estado do quarto: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/recepcionista/quartos?erro=status_quarto_invalido");
        } catch (Exception e) {
            // Qualquer outro erro.
            System.err.println("Erro geral ao atualizar estado do quarto: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/recepcionista/quartos?erro=desconhecido");
        }
    }
}

