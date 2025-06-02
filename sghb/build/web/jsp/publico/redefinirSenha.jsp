<%-- 
    Document   : redefinirSenha
    Created on : 02/06/2025, 13:22:54
    Author     : User
--%>


<%@page import="model.Usuario"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Proteção: redireciona se não houver usuário na sessão ou se não for o primeiro login
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    if (usuario == null || !usuario.isPrimeiroLogin()) {
        response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SGHB - Redefinir Senha</title>
    <%-- Pode reutilizar o mesmo estilo do login.jsp --%>
    <style>
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
        .login-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 350px; }
        .login-container h2 { text-align: center; margin-bottom: 1.5rem; }
        .form-group { margin-bottom: 1rem; }
        .form-group label { display: block; margin-bottom: 0.5rem; }
        .form-group input { width: 100%; padding: 0.5rem; border: 1px solid #ccc; border-radius: 4px; }
        .btn { width: 100%; padding: 0.7rem; border: none; background-color: #28a745; color: white; border-radius: 4px; cursor: pointer; font-size: 1rem; }
        .btn:hover { background-color: #218838; }
        .error { color: red; margin-bottom: 1rem; text-align: center; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Olá, <%= usuario.getNomeCompleto() %>!</h2>
        <p style="text-align: center; margin-bottom: 1rem;">Por segurança, você deve redefinir sua senha.</p>
        
        <% 
            String erro = (String) request.getAttribute("erro");
            if (erro != null) {
        %>
            <p class="error"><%= erro %></p>
        <% 
            }
        %>

        <form action="${pageContext.request.contextPath}/autenticar" method="post">
            <input type="hidden" name="acao" value="redefinirSenha">
            <div class="form-group">
                <label for="novaSenha">Nova Senha</label>
                <input type="password" id="novaSenha" name="novaSenha" required>
            </div>
            <div class="form-group">
                <label for="confirmaSenha">Confirme a Nova Senha</label>
                <input type="password" id="confirmaSenha" name="confirmaSenha" required>
            </div>
            <button type="submit" class="btn">Salvar Nova Senha</button>
        </form>
    </div>
</body>
</html>

