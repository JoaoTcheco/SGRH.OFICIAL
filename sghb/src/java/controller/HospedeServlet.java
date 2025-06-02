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
import model.Hospede;
import service.HospedeService;

/**
 * Este Servlet lida com todos os pedidos relacionados com Hóspedes (CRUD).
 * Responde ao URL "/recepcionista/hospedes".
 */
@WebServlet(name = "HospedeServlet", urlPatterns = {"/recepcionista/hospedes"})
public class HospedeServlet extends HttpServlet {
    private final HospedeService hospedeService = new HospedeService();

    /**
     * Lida com pedidos HTTP GET.
     * Usado para: listar hóspedes, mostrar formulário para novo/editar, e remover.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // O parâmetro "acao" diz o que o utilizador quer fazer.
        String acao = request.getParameter("acao");
        if (acao == null || acao.isEmpty()) {
            acao = "listar"; // Se não houver ação, a ação padrão é listar.
        }

        try {
            switch (acao) {
                case "novo": // Mostrar formulário para adicionar novo hóspede
                    mostrarFormularioNovo(request, response);
                    break;
                case "editar": // Mostrar formulário para editar um hóspede existente
                    mostrarFormularioEdicao(request, response);
                    break;
                case "remover": // Apagar um hóspede
                    processarRemover(request, response);
                    break;
                case "listar": // Mostrar a lista de todos os hóspedes
                default:
                    processarListar(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Erro no doGet do HospedeServlet: " + e.getMessage());
            // Idealmente, redirecionar para uma página de erro amigável.
            request.setAttribute("mensagemErroGeral", "Ocorreu um problema: " + e.getMessage());
            processarListar(request, response); // Tenta mostrar a lista mesmo com erro noutra ação
        }
    }

    /**
     * Lida com pedidos HTTP POST.
     * Usado para salvar um hóspede novo ou as alterações de um hóspede existente.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // O formulário de adicionar e editar hóspede envia os dados por POST.
        processarSalvar(request, response);
    }

    /**
     * Pega a lista de todos os hóspedes e manda para a página JSP.
     */
    private void processarListar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Hospede> listaDeHospedes = hospedeService.listarTodos();
        request.setAttribute("listaHospedes", listaDeHospedes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/recepcionista/listaHospedes.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Prepara para mostrar o formulário para adicionar um novo hóspede.
     * (Neste caso, o formulário estará vazio).
     */
    private void mostrarFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Não precisamos de passar nenhum objeto Hospede, porque é para um novo.
        // O JSP vai tratar disso.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/recepcionista/formularioHospede.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Prepara para mostrar o formulário para editar um hóspede existente.
     * Primeiro, busca o hóspede na base de dados pelo ID.
     */
    private void mostrarFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idHospede = Integer.parseInt(request.getParameter("id"));
        Hospede hospedeParaEditar = hospedeService.buscarPorId(idHospede);
        // Coloca o hóspede encontrado no 'request' para que o formulário JSP possa mostrar os dados dele.
        request.setAttribute("hospede", hospedeParaEditar); 
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/recepcionista/formularioHospede.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Pega os dados do formulário (novo ou editado) e salva na base de dados.
     */
    private void processarSalvar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Pega todos os dados que vieram do formulário.
        String idParam = request.getParameter("idHospede"); // Este campo só existe se estivermos a editar
        String nome = request.getParameter("nomeCompleto");
        String documento = request.getParameter("documentoIdentidadeNumero");
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");

        Hospede hospede = new Hospede();
        hospede.setNomeCompleto(nome);
        hospede.setDocumentoIdentidadeNumero(documento);
        hospede.setTelefone(telefone);
        hospede.setEmail(email);

        try {
            if (idParam == null || idParam.isEmpty()) {
                // Se não há ID, é um hóspede NOVO.
                hospedeService.adicionar(hospede);
            } else {
                // Se há ID, estamos a ATUALIZAR um hóspede existente.
                hospede.setIdHospede(Integer.parseInt(idParam));
                hospedeService.atualizar(hospede);
            }
            // Depois de salvar, manda de volta para a lista de hóspedes.
            response.sendRedirect(request.getContextPath() + "/recepcionista/hospedes?acao=listar");
        } catch (Exception e) {
            System.err.println("Erro ao salvar hóspede: " + e.getMessage());
            // Numa aplicação real, seria bom guardar os dados que o utilizador meteu
            // e mostrar o formulário de novo com uma mensagem de erro.
            response.sendRedirect(request.getContextPath() + "/recepcionista/hospedes?acao=listar&erroSalvar=true");
        }
    }

    /**
     * Remove um hóspede da base de dados.
     */
    private void processarRemover(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idHospede = Integer.parseInt(request.getParameter("id"));
            hospedeService.remover(idHospede);
            // Depois de remover, manda de volta para a lista de hóspedes.
            response.sendRedirect(request.getContextPath() + "/recepcionista/hospedes?acao=listar&removido=sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao remover hóspede: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/recepcionista/hospedes?acao=listar&erroRemover=true");
        }
    }
}

