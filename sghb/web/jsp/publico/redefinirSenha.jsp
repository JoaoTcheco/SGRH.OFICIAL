<%-- 
    Document   : redefinirSenha
    Created on : 02/06/2025, 13:22:54
    Author     : User
--%>


<%@page import="model.Usuario"%><%-- Importa a classe Usuario para podermos usar os seus dados --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Medida de segurança importante:
    // Esta página só deve ser acedida se:
    // 1. Houver um utilizador guardado na sessão (ou seja, ele fez login).
    // 2. E esse utilizador estiver marcado como 'primeiroLogin = true'.
    // Se alguma destas condições não for verdadeira, mandamos o utilizador de volta para a página de login.
    Usuario usuarioParaRedefinir = (Usuario) session.getAttribute("usuarioLogado");
    if (usuarioParaRedefinir == null || !usuarioParaRedefinir.isPrimeiroLogin()) {
        response.sendRedirect(request.getContextPath() + "/jsp/publico/login.jsp");
        return; // Importante: para a execução desta página aqui para não mostrar o resto.
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SGHB - Redefinir Senha</title>
    <%-- Reutilizamos os mesmos estilos da página de login para manter um visual parecido --%>
    <style>
        body { font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; height: 90vh; background-color: #f0f2f5; margin: 0; }
        .login-container { background: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); width: 350px; } /* Caixa um pouco mais larga */
        .login-container h2 { text-align: center; margin-bottom: 15px; color: #333; }
        .login-container p.info { text-align: center; margin-bottom: 20px; color: #555; font-size: 0.95em;}
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; color: #555; font-weight: bold; }
        .form-group input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .btn { width: 100%; padding: 12px; border: none; background-color: #28a745; /* Botão verde para salvar */ color: white; border-radius: 4px; cursor: pointer; font-size: 16px; transition: background-color 0.3s ease; }
        .btn:hover { background-color: #218838; /* Verde mais escuro */ }
        .mensagem-erro { color: red; margin-bottom: 15px; text-align: center; font-size: 0.9em; }
    </style>
</head>
<body>
    <div class="login-container">
        <%-- Mostra o nome do utilizador que está a redefinir a senha --%>
        <h2>Olá, <%= usuarioParaRedefinir.getNomeCompleto() %>!</h2>
        <p class="info">Como esta é a sua primeira vez a entrar no sistema, por favor, crie uma nova senha por motivos de segurança.</p>
        
        <%-- Mostra mensagens de erro, se o Servlet enviar alguma --%>
        <% 
            String erroRedefinir = (String) request.getAttribute("erro");
            if (erroRedefinir != null) {
        %>
            <p class="mensagem-erro"><%= erroRedefinir %></p>
        <% 
            }
        %>

        <%-- Formulário para redefinir a senha --%>
        <form action="${pageContext.request.contextPath}/autenticar" method="post">
            <%-- Campo escondido para dizer ao Servlet que a ação é "redefinirSenha" --%>
            <input type="hidden" name="acao" value="redefinirSenha">
            
            <div class="form-group">
                <label for="novaSenha">Nova Senha:</label>
                <input type="password" id="novaSenha" name="novaSenha" required>
            </div>
            
            <div class="form-group">
                <label for="confirmaSenha">Confirme a Nova Senha:</label>
                <input type="password" id="confirmaSenha" name="confirmaSenha" required>
            </div>
            
            <button type="submit" class="btn">Guardar Nova Senha</button>
        </form>
    </div>
</body>
</html>


