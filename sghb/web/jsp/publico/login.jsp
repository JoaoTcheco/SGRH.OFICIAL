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
    <title>SGHB - Entrar no Sistema</title>
    <style>
        /* Estilos básicos para a página de login ficar mais apresentável */
        body { 
            font-family: Arial, sans-serif; /* Tipo de letra comum */
            display: flex; /* Ajuda a centrar o conteúdo */
            justify-content: center; /* Centra horizontalmente */
            align-items: center; /* Centra verticalmente */
            height: 90vh; /* Faz a altura ser quase o ecrã todo */
            background-color: #f0f2f5; /* Cor de fundo suave */
            margin: 0; /* Tira margens padrão do corpo da página */
        }
        .login-container { 
            background: #ffffff; /* Caixa de login com fundo branco */
            padding: 30px; /* Espaço dentro da caixa */
            border-radius: 8px; /* Cantos arredondados */
            box-shadow: 0 4px 10px rgba(0,0,0,0.1); /* Sombra suave */
            width: 320px; /* Largura da caixa */
        }
        .login-container h2 { 
            text-align: center; /* Título centrado */
            margin-bottom: 25px; /* Espaço abaixo do título */
            color: #333; /* Cor do título */
        }
        .form-group { 
            margin-bottom: 20px; /* Espaço entre os campos do formulário */
        }
        .form-group label { 
            display: block; /* Faz a etiqueta ocupar a linha toda */
            margin-bottom: 8px; /* Espaço abaixo da etiqueta */
            color: #555; /* Cor da etiqueta */
            font-weight: bold; /* Letra a negrito */
        }
        .form-group input { 
            width: 100%; /* Campo de texto ocupa a largura toda */
            padding: 10px; /* Espaço dentro do campo */
            border: 1px solid #ccc; /* Borda cinzenta clara */
            border-radius: 4px; /* Cantos arredondados do campo */
            box-sizing: border-box; /* Para o padding não aumentar a largura total */
        }
        .btn { 
            width: 100%; /* Botão ocupa a largura toda */
            padding: 12px; /* Espaço dentro do botão */
            border: none; /* Sem borda */
            background-color: #007bff; /* Cor de fundo azul */
            color: white; /* Cor do texto branca */
            border-radius: 4px; /* Cantos arredondados */
            cursor: pointer; /* Rato vira uma mãozinha */
            font-size: 16px; /* Tamanho do texto do botão */
            transition: background-color 0.3s ease; /* Efeito suave ao mudar de cor */
        }
        .btn:hover { 
            background-color: #0056b3; /* Azul mais escuro quando o rato está por cima */
        }
        .mensagem-erro { 
            color: red; /* Cor vermelha para mensagens de erro */
            margin-bottom: 15px; 
            text-align: center; 
            font-size: 0.9em; /* Texto um pouco mais pequeno */
        }
        .mensagem-sucesso { 
            color: green; /* Cor verde para mensagens de sucesso */
            margin-bottom: 15px; 
            text-align: center; 
            font-size: 0.9em; 
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>SGHB - Acesso</h2>

        <%-- Secção para mostrar mensagens de erro que vêm do Servlet --%>
        <% 
            // Pega a mensagem de erro que o AutenticacaoServlet pode ter enviado
            String erroLogin = (String) request.getAttribute("erro");
            if (erroLogin != null) { // Se houver uma mensagem de erro...
        %>
            <p class="mensagem-erro"><%= erroLogin %></p> <%-- Mostra a mensagem --%>
        <% 
            }

            // Verifica se há outros parâmetros de erro no URL (vindos de redirecionamentos)
            String erroParametroUrl = request.getParameter("erro");
            if ("AcaoNula".equals(erroParametroUrl) || "AcaoInvalida".equals(erroParametroUrl)) {
        %>
             <p class="mensagem-erro">Ocorreu um problema inesperado. Por favor, tente novamente.</p>
        <%
            } else if ("AcessoNegado".equals(erroParametroUrl)) {
        %>
             <p class="mensagem-erro">Acesso negado. Precisa de fazer login para entrar nessa página.</p>
        <%
            } else if ("SessaoInvalidaParaRedefinir".equals(erroParametroUrl) || "SessaoExpirada".equals(erroParametroUrl)) {
        %>
             <p class="mensagem-erro">A sua sessão terminou ou é inválida. Por favor, faça login outra vez.</p>
        <%
            }
        %>

        <%-- Secção para mostrar mensagem de sucesso quando o utilizador faz logout --%>
        <%
            if ("sucesso".equals(request.getParameter("logout"))) {
        %>
            <p class="mensagem-sucesso">Sessão terminada com sucesso!</p>
        <%
            }
        %>
        
        <%-- Formulário de login --%>
        <%-- 'action' diz para onde os dados do formulário vão ser enviados (o nosso AutenticacaoServlet) --%>
        <%-- 'method="post"' diz que os dados vão escondidos no pedido (mais seguro para senhas) --%>
        <form action="${pageContext.request.contextPath}/autenticar" method="post">
            <%-- Campo escondido (hidden) para dizer ao Servlet qual é a ação que queremos fazer ("login") --%>
            <input type="hidden" name="acao" value="login">
            
            <div class="form-group">
                <label for="username">Nome de Utilizador:</label>
                <input type="text" id="username" name="username" required> <%-- 'required' significa que este campo é obrigatório --%>
            </div>
            
            <div class="form-group">
                <label for="senha">Senha:</label>
                <input type="password" id="senha" name="senha" required> <%-- 'type="password"' faz os caracteres aparecerem como bolinhas --%>
            </div>
            
            <button type="submit" class="btn">Entrar</button>
        </form>
    </div>
</body>
</html>
