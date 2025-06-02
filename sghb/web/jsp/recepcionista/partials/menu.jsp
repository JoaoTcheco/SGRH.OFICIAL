<%-- 
    Document   : menu
    Created on : 02/06/2025, 13:24:11
    Author     : User
--%>

<%@page import="model.Usuario"%> <%-- Importa a classe Usuario para podermos pegar o nome do utilizador logado --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Tenta pegar o objeto Usuario que foi guardado na sessão quando o utilizador fez login.
    Usuario usuarioLogadoNoMenu = (Usuario) session.getAttribute("usuarioLogado");
    
    // Prepara o nome a ser mostrado no menu.
    String nomeParaMostrarNoMenu = "Utilizador"; // Valor padrão se não encontrar o nome
    if (usuarioLogadoNoMenu != null && usuarioLogadoNoMenu.getNomeCompleto() != null && !usuarioLogadoNoMenu.getNomeCompleto().isEmpty()) {
        // Se encontrou o utilizador e ele tem nome, pega só o primeiro nome.
        String[] partesDoNome = usuarioLogadoNoMenu.getNomeCompleto().split(" ");
        if (partesDoNome.length > 0) {
            nomeParaMostrarNoMenu = partesDoNome[0]; 
        }
    }
%>
<style>
    /* Estilos para a barra de navegação (menu) */
    .navbar {
        background-color: #343a40; /* Cor de fundo escura, tipo carvão */
        padding: 0 25px; /* Espaçamento dos lados para o conteúdo não colar nas bordas */
        display: flex; /* Usa flexbox para alinhar itens lado a lado */
        justify-content: space-between; /* Coloca o logo/links na esquerda e info do user na direita */
        align-items: center; /* Alinha os itens verticalmente ao centro da barra */
        height: 60px; /* Altura fixa para a barra de menu */
        box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* Sombra leve abaixo da barra */
    }
    .navbar .nav-logo a { /* Estilo para o logo ou nome do sistema no menu */
        color: white;
        text-decoration: none; /* Tira o sublinhado do link */
        font-size: 1.5rem; /* Tamanho de letra grande para destacar */
        font-weight: bold; /* Letra a negrito */
    }
    .navbar .nav-links { /* Div que agrupa os links de navegação */
        display: flex; /* Alinha os links lado a lado */
    }
    .navbar .nav-links a { /* Estilo para cada link do menu (Dashboard, Quartos, etc.) */
        color: #f8f9fa; /* Cor de texto clara, quase branca */
        text-decoration: none;
        margin: 0 15px; /* Espaço entre os links */
        font-size: 1rem; /* Tamanho de letra normal */
        padding: 20px 0; /* Aumenta a área clicável verticalmente para facilitar o clique */
        transition: color 0.2s ease, border-bottom-color 0.2s ease; /* Efeito suave ao mudar cor */
        border-bottom: 2px solid transparent; /* Borda transparente em baixo para efeito hover */
    }
    .navbar .nav-links a:hover,
    .navbar .nav-links a.active { /* Estilo quando o rato está por cima ou se for o link da página atual */
        color: #ffffff; /* Cor fica totalmente branca */
        border-bottom-color: #007bff; /* Mostra uma linha azul em baixo */
    }
    .navbar .user-info { /* Div que agrupa a saudação e o link de logout */
        display: flex;
        align-items: center;
        color: #adb5bd; /* Cor cinzenta clara para a saudação */
    }
    .navbar .user-info span { /* Para o texto "Olá, [Nome]" */
        margin-right: 15px; /* Espaço antes do link de logout */
    }
    .navbar .user-info a { /* Para o link de "Logout" */
        color: #ffc107; /* Cor amarela para destacar o logout */
        text-decoration: none;
        font-weight: bold;
        transition: color 0.2s ease;
    }
    .navbar .user-info a:hover {
        color: #e0a800; /* Amarelo mais escuro */
    }
</style>

<div class="navbar">
    <div class="nav-logo">
        <%-- Link para o nome do sistema, leva ao dashboard --%>
        <a href="${pageContext.request.contextPath}/jsp/recepcionista/dashboard.jsp">SGHB</a>
    </div>
    <div class="nav-links">
        <%-- Links para as diferentes secções do sistema --%>
        <%-- pageContext.request.requestURI é o URL da página atual. Usamos para saber qual link marcar como "active" --%>
        <a href="${pageContext.request.contextPath}/jsp/recepcionista/dashboard.jsp" 
           class="${pageContext.request.requestURI.endsWith('dashboard.jsp') ? 'active' : ''}">Painel</a>
        <a href="${pageContext.request.contextPath}/recepcionista/quartos" 
           class="${pageContext.request.requestURI.contains('/quartos') ? 'active' : ''}">Quartos</a>
        <a href="${pageContext.request.contextPath}/recepcionista/hospedes?acao=listar" 
           class="${pageContext.request.requestURI.contains('/hospedes') ? 'active' : ''}">Hóspedes</a>
        <a href="${pageContext.request.contextPath}/recepcionista/reservas?acao=listar" 
           class="${pageContext.request.requestURI.contains('/reservas') ? 'active' : ''}">Reservas</a>
    </div>
    <div class="user-info">
        <span>Olá, <%= nomeParaMostrarNoMenu %></span>
        <%-- Link para fazer logout, chama o AutenticacaoServlet com a ação "logout" --%>
        <a href="${pageContext.request.contextPath}/autenticar?acao=logout">Sair</a>
    </div>
</div>



