<%-- 
    Document   : menu
    Created on : 02/06/2025, 13:24:11
    Author     : User
--%>

<%@page import="model.Usuario"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Pega o usuário da sessão para exibir o nome
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    String nomeUsuario = (usuarioLogado != null) ? usuarioLogado.getNomeCompleto().split(" ")[0] : "Usuário";
%>
<style>
    .navbar {
        background-color: #343a40;
        padding: 1rem;
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: white;
    }
    .navbar a {
        color: white;
        text-decoration: none;
        margin: 0 15px;
        font-size: 1.1rem;
    }
    .navbar a:hover {
        text-decoration: underline;
    }
    .navbar .user-info {
        display: flex;
        align-items: center;
    }
</style>

<div class="navbar">
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/jsp/recepcionista/dashboard.jsp">Dashboard</a>
        href="${pageContext.request.contextPath}/recepcionista/quartos">Quartos</a> <%-- Link a ser implementado --%>
        <a href="#reservas">Reservas</a> <%-- Link a ser implementado --%>
        <a href="#hospedes">Hóspedes</a> <%-- Link a ser implementado --%>
    </div>
    <div class="user-info">
        <span>Olá, <%= nomeUsuario %></span>
        <a href="${pageContext.request.contextPath}/autenticar?acao=logout">Logout</a>
    </div>
</div>

