/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.QuartoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate; // Para trabalhar com as datas
import java.util.List;
import model.Hospede;
import model.Quarto;
import model.Reserva;
import model.Usuario;
import service.HospedeService;
import service.ReservaService;

/**
 * Este Servlet lida com os pedidos relacionados com Reservas.
 * Responde ao URL "/recepcionista/reservas".
 */
@WebServlet(name = "ReservaServlet", urlPatterns = {"/recepcionista/reservas"})
public class ReservaServlet extends HttpServlet {
    // Cria instâncias dos serviços que vai precisar.
    private final ReservaService reservaService = new ReservaService();
    private final HospedeService hospedeService = new HospedeService();
    private final QuartoDAO quartoDAO = new QuartoDAO(); // Usamos o DAO de Quarto diretamente para listar disponíveis

    /**
     * Lida com pedidos HTTP GET.
     * Usado para: listar reservas e mostrar o formulário para nova reserva.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if (acao == null || acao.isEmpty()) {
            acao = "listar"; // Ação padrão é listar
        }

        try {
            switch (acao) {
                case "novo": // Mostrar formulário para criar nova reserva
                    mostrarFormularioNovaReserva(request, response);
                    break;
                case "listar": // Mostrar a lista de todas as reservas
                default:
                    processarListarReservas(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Erro no doGet do ReservaServlet: " + e.getMessage());
            request.setAttribute("mensagemErroGeral", "Ocorreu um problema: " + e.getMessage());
            processarListarReservas(request, response); 
        }
    }

    /**
     * Lida com pedidos HTTP POST.
     * Usado para salvar uma nova reserva.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processarSalvarReserva(request, response);
    }

    /**
     * Pega a lista de todas as reservas e manda para a página JSP.
     */
    private void processarListarReservas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Reserva> listaDeReservas = reservaService.listarTodas();
        request.setAttribute("listaReservas", listaDeReservas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/recepcionista/listaReservas.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Prepara os dados necessários (lista de hóspedes e quartos disponíveis)
     * e mostra o formulário para criar uma nova reserva.
     */
    private void mostrarFormularioNovaReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Precisa da lista de todos os hóspedes para o dropdown no formulário.
        List<Hospede> todosOsHospedes = hospedeService.listarTodos();
        request.setAttribute("listaHospedes", todosOsHospedes);
        
        // Precisa da lista de quartos disponíveis para o dropdown.
        // Para simplificar, vamos pegar os quartos disponíveis para "hoje" e "amanhã".
        // Numa aplicação real, esta lista deveria ser atualizada dinamicamente (com AJAX)
        // depois que o utilizador escolhe as datas de check-in e check-out.
        LocalDate hoje = LocalDate.now();
        LocalDate amanha = hoje.plusDays(1); // Uma data de checkout padrão
        List<Quarto> quartosDisponiveis = quartoDAO.listarDisponiveis(hoje, amanha);
        request.setAttribute("listaQuartos", quartosDisponiveis);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/recepcionista/formularioReserva.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Pega os dados do formulário de nova reserva e tenta salvar na base de dados.
     */
    private void processarSalvarReserva(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Pega os dados do formulário
            int idHospedeSelecionado = Integer.parseInt(request.getParameter("idHospede"));
            int idQuartoSelecionado = Integer.parseInt(request.getParameter("idQuarto"));
            LocalDate dataCheckin = LocalDate.parse(request.getParameter("dataCheckin")); // Converte texto para data
            LocalDate dataCheckout = LocalDate.parse(request.getParameter("dataCheckout"));

            // Pega o utilizador que está logado (o recepcionista) da sessão.
            // Precisamos do ID dele para guardar quem fez a reserva.
            Usuario recepcionistaLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
            if (recepcionistaLogado == null) {
                // Isto não deveria acontecer se o Filtro de Autenticação estiver a funcionar bem.
                response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp?erro=SessaoExpirada");
                return;
            }

            // Validação básica das datas
            if (dataCheckout.isBefore(dataCheckin) || dataCheckout.isEqual(dataCheckin)) {
                request.setAttribute("erroFormulario", "A data de Check-out deve ser depois da data de Check-in.");
                // Precisamos recarregar os dados para o formulário
                mostrarFormularioNovaReserva(request, response); // Reutiliza o método para mostrar o form com erro
                return;
            }
            
            // Cria um novo objeto Reserva e preenche com os dados.
            Reserva novaReserva = new Reserva();
            novaReserva.setIdHospede(idHospedeSelecionado);
            novaReserva.setIdQuarto(idQuartoSelecionado);
            novaReserva.setDataCheckin(dataCheckin);
            novaReserva.setDataCheckout(dataCheckout);
            novaReserva.setIdUsuarioCriacao(recepcionistaLogado.getIdUsuario());
            novaReserva.setStatusReserva(Reserva.StatusReserva.CONFIRMADA); // Nova reserva é confirmada por defeito
            
            // Pede ao ReservaService para tentar criar a reserva.
            boolean reservaCriada = reservaService.criarReserva(novaReserva);

            if (reservaCriada) {
                // Se deu certo, manda de volta para a lista de reservas.
                response.sendRedirect(request.getContextPath() + "/recepcionista/reservas?acao=listar&reserva=sucesso");
            } else {
                // Se deu erro ao criar a reserva (ex: quarto já não estava disponível, erro no banco).
                request.setAttribute("erroFormulario", "Não foi possível criar a reserva. Verifique a disponibilidade do quarto para as datas ou tente mais tarde.");
                mostrarFormularioNovaReserva(request, response);
            }

        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter ID de hóspede ou quarto: " + e.getMessage());
            request.setAttribute("erroFormulario", "Hóspede ou quarto inválido.");
            try { mostrarFormularioNovaReserva(request, response); } catch (Exception ex) { /* ignora */ }
        } catch (Exception e) { // Captura outros erros (ex: data inválida)
            System.err.println("Erro geral ao salvar reserva: " + e.getMessage());
            request.setAttribute("erroFormulario", "Ocorreu um erro inesperado: " + e.getMessage());
            try { mostrarFormularioNovaReserva(request, response); } catch (Exception ex) { /* ignora */ }
        }
    }
}

