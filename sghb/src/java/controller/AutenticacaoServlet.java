/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import model.Usuario;
import util.PasswordUtils;

/**
 * Este Servlet é responsável por todas as ações relacionadas com autenticação:
 * - Login (entrar no sistema)
 * - Redefinir senha (no primeiro login)
 * - Logout (sair do sistema)
 * A anotação @WebServlet("/autenticar") diz que este Servlet responde ao URL "/autenticar".
 */
@WebServlet(name = "AutenticacaoServlet", urlPatterns = {"/autenticar"})
public class AutenticacaoServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO(); // Cria um objeto DAO para usar sempre

    /**
     * Lida com pedidos HTTP POST. Usamos POST para login e redefinição de senha
     * porque são ações que enviam dados sensíveis.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pegamos o parâmetro "acao" para saber o que o utilizador quer fazer.
        String acao = request.getParameter("acao");

        if (acao == null || acao.isEmpty()) {
            // Se não houver ação, é um erro. Manda de volta para o login.
            response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp?erro=AcaoNula");
            return;
        }

        // Um switch para tratar cada ação possível.
        switch (acao) {
            case "login":
                processarLogin(request, response);
                break;
            case "redefinirSenha":
                processarRedefinirSenha(request, response);
                break;
            default:
                // Se a ação não for conhecida, manda de volta para o login.
                response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp?erro=AcaoInvalida");
        }
    }
    
    /**
     * Lida com pedidos HTTP GET. Usamos GET para o logout, que é uma ação simples.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if ("logout".equals(acao)) {
            processarLogout(request, response);
        } else {
            // Se não for logout, manda para o login (não deveria acontecer muito por GET).
            response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp");
        }
    }

    /**
     * Lógica para processar o pedido de login.
     */
    private void processarLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String usernameInserido = request.getParameter("username");
        String senhaInserida = request.getParameter("senha");

        // Tenta buscar o utilizador na base de dados pelo username.
        Usuario usuarioDoBanco = usuarioDAO.buscarPorUsername(usernameInserido);

        // Verifica se o utilizador existe E se a senha está correta.
        if (usuarioDoBanco != null && PasswordUtils.verificarSenha(senhaInserida, usuarioDoBanco.getSenhaHash())) {
            // Se tudo estiver certo, cria uma sessão para o utilizador.
            HttpSession sessao = request.getSession(); // Pega a sessão atual ou cria uma nova.
            // Guarda o objeto Usuario na sessão. Assim, outras páginas saberão quem está logado.
            sessao.setAttribute("usuarioLogado", usuarioDoBanco);

            // Verifica se é o primeiro login.
            if (usuarioDoBanco.isPrimeiroLogin()) {
                // Se for, manda para a página de redefinir senha.
                response.sendRedirect(request.getContextPath() + "/jsp/publico/redefinirSenha.jsp");
            } else {
                // Se não for o primeiro login, manda para o painel principal do recepcionista.
                response.sendRedirect(request.getContextPath() + "/jsp/recepcionista/dashboard.jsp"); 
            }
        } else {
            // Se o utilizador não existe ou a senha está errada.
            request.setAttribute("erro", "Nome de utilizador ou senha inválidos. Tente de novo.");
            // Reencaminha de volta para a página de login, mostrando a mensagem de erro.
            // Usamos forward() aqui para que a mensagem de erro no 'request' chegue à página JSP.
            request.getRequestDispatcher("/jsp/publico/login.jsp").forward(request, response);
        }
    }

    /**
     * Lógica para processar o pedido de redefinição de senha.
     */
    private void processarRedefinirSenha(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession sessao = request.getSession(false); // Pega a sessão, mas não cria uma nova.
        
        // Verifica se há uma sessão válida e um utilizador logado.
        // Isto é importante para segurança, para garantir que só quem está no processo de primeiro login pode mudar a senha aqui.
        if (sessao == null || sessao.getAttribute("usuarioLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp?erro=SessaoInvalidaParaRedefinir");
            return;
        }

        String novaSenha = request.getParameter("novaSenha");
        String confirmaSenha = request.getParameter("confirmaSenha");

        // Validação simples: as senhas devem ser iguais e não podem estar vazias.
        if (novaSenha == null || novaSenha.isEmpty() || !novaSenha.equals(confirmaSenha)) {
            request.setAttribute("erro", "As senhas não são iguais ou estão em branco.");
            request.getRequestDispatcher("/jsp/publico/redefinirSenha.jsp").forward(request, response);
            return;
        }

        // Pega o objeto Usuario da sessão.
        Usuario usuarioDaSessao = (Usuario) sessao.getAttribute("usuarioLogado");
        
        // Cria o hash da nova senha.
        String novaSenhaHash = PasswordUtils.hashSenha(novaSenha);
        
        // Tenta atualizar a senha na base de dados.
        boolean sucessoNaAtualizacao = usuarioDAO.atualizarSenha(usuarioDaSessao.getIdUsuario(), novaSenhaHash);

        if (sucessoNaAtualizacao) {
            // Se deu certo:
            // 1. Atualiza o objeto Usuario na sessão para marcar que já não é o primeiro login.
            usuarioDaSessao.setPrimeiroLogin(false);
            usuarioDaSessao.setSenhaHash(novaSenhaHash); // Atualiza o hash na sessão também, por consistência
            sessao.setAttribute("usuarioLogado", usuarioDaSessao);
            // 2. Manda para o painel principal.
            response.sendRedirect(request.getContextPath() + "/jsp/recepcionista/dashboard.jsp");
        } else {
            // Se deu erro ao atualizar no banco.
            request.setAttribute("erro", "Não foi possível atualizar a senha. Por favor, tente mais tarde.");
            request.getRequestDispatcher("/jsp/publico/redefinirSenha.jsp").forward(request, response);
        }
    }
    
    /**
     * Lógica para processar o pedido de logout.
     */
    private void processarLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sessao = request.getSession(false); // Pega a sessão existente
        if (sessao != null) {
            sessao.removeAttribute("usuarioLogado"); // Remove o atributo do utilizador
            sessao.invalidate(); // Invalida (destrói) a sessão completamente.
        }
        // Manda de volta para a página de login com uma mensagem de sucesso.
        response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp?logout=sucesso");
    }
}

