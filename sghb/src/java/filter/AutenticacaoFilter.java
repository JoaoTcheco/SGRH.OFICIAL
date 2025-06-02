/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter; 

import model.Usuario;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;




/**
 * Este filtro é como um "segurança" para as páginas internas do recepcionista.
 * Ele verifica se o utilizador já fez login antes de o deixar aceder a essas páginas.
 * A anotação @WebFilter diz ao servidor para aplicar este filtro a todos os URLs
 * que começam com "/jsp/recepcionista/".
 */
@WebFilter(filterName = "AutenticacaoFilter", urlPatterns = {"/jsp/recepcionista/*"})
public class AutenticacaoFilter implements Filter {

    /**
     * Este é o método principal do filtro.É chamado sempre que alguém tenta aceder
 a uma página que corresponde ao urlPatterns definido em @WebFilter.
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws jakarta.servlet.ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Convertemos os objetos ServletRequest e ServletResponse para os seus tipos HTTP,
        // que têm mais funcionalidades para trabalhar com web.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Tentamos pegar a sessão do utilizador.
        // O 'false' em getSession(false) significa: "pegue a sessão se ela existir, mas não crie uma nova se não existir".
        HttpSession session = httpRequest.getSession(false);

        // Verificamos se o utilizador está logado:
        // 1. A sessão deve existir (session != null)
        // 2. Dentro da sessão, deve haver um atributo chamado "usuarioLogado" (que guardámos no AutenticacaoServlet)
        boolean estaLogado = (session != null && session.getAttribute("usuarioLogado") != null);
        
        if (estaLogado) {
            // Se está logado, pegamos o objeto Usuario da sessão.
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            
            // Uma verificação extra de segurança:
            // Se for o primeiro login do utilizador, ele DEVE mudar a senha.
            // Então, se ele tentar aceder a outra página do recepcionista sem ser
            // a de redefinir senha, nós o mandamos para lá.
            if (usuario.isPrimeiroLogin()) {
                 // Redireciona para a página de redefinir senha.
                 // httpRequest.getContextPath() dá o caminho base da aplicação (ex: "/sghb")
                 httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/publico/redefinirSenha.jsp");
            } else {
                // Se está logado E não é o primeiro login, o utilizador pode continuar para a página que pediu.
                // chain.doFilter(request, response) diz: "Deixe o pedido seguir o seu caminho normal".
                chain.doFilter(request, response);
            }
        } else {
            // Se não está logado, não pode aceder às páginas do recepcionista.
            // Mandamos o utilizador de volta para a página de login.
            // Adicionamos um parâmetro "?erro=AcessoNegado" para que a página de login possa mostrar uma mensagem.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/publico/login.jsp?erro=AcessoNegado");
        }
    }

    /**
     * Método chamado quando o filtro é iniciado pelo servidor.Podemos deixar vazio se não precisarmos de nenhuma configuração inicial.
     * @param filterConfig
     * @throws jakarta.servlet.ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // System.out.println("Filtro de Autenticação iniciado!");
    }

    /**
     * Método chamado quando o filtro é destruído (ex: quando o servidor para).
     * Podemos deixar vazio se não precisarmos de limpar nada.
     */
    @Override
    public void destroy() {
        // System.out.println("Filtro de Autenticação destruído!");
    }
}


