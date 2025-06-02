<%-- 
    Document   : login
    Created on : 02/06/2025, 13:22:32
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SGHB - Login</title>
    <style>
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
        .login-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 300px; }
        .login-container h2 { text-align: center; margin-bottom: 1.5rem; }
        .form-group { margin-bottom: 1rem; }
        .form-group label { display: block; margin-bottom: 0.5rem; }
        .form-group input { width: 100%; padding: 0.5rem; border: 1px solid #ccc; border-radius: 4px; }
        .btn { width: 100%; padding: 0.7rem; border: none; background-color: #007bff; color: white; border-radius: 4px; cursor: pointer; font-size: 1rem; }
        .btn:hover { background-color: #0056b3; }
        .error { color: red; margin-bottom: 1rem; text-align: center; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>SGHB Login</h2>
        <%-- Exibe mensagens de erro --%>
        <% 
            String erro = (String) request.getAttribute("erro");
            if (erro != null) {
        %>
            <p class="error"><%= erro %></p>
        <% 
            }
        %>
        <%
            if ("sucesso".equals(request.getParameter("logout"))) {
        %>
            <p style="color:green; text-align:center;">Logout efetuado com sucesso!</p>
        <%
            }
        %>
        
        <form action="${pageContext.request.contextPath}/autenticar" method="post">
            <input type="hidden" name="acao" value="login">
            <div class="form-group">
                <label for="username">Usuário</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required>
            </div>
            <button type="submit" class="btn">Entrar</button>
        </form>
    </div>
</body>
</html>
