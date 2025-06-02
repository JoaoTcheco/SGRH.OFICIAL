<%-- 
    Document   : dashboard
    Created on : 02/06/2025, 13:25:50
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Painel Principal - SGHB</title>
    <%-- Inclui os estilos CSS comuns que definimos em _partials/estilos.jsp --%>
    <jsp:include page="_partials/estilos.jsp" />
    <style>
        /* Estilos específicos para o dashboard */
        .dashboard-bemvindo {
            padding: 30px;
            background-color: #e9ecef; /* Fundo cinza claro para a mensagem de boas-vindas */
            border-radius: 8px;
            text-align: center;
            margin-bottom: 30px;
        }
        .dashboard-bemvindo h1 {
            color: #007bff; /* Azul para o título principal */
            margin-bottom: 10px;
        }
        .dashboard-bemvindo p {
            font-size: 1.1rem;
            color: #495057;
        }
        .atalhos-rapidos {
            display: flex; /* Alinha os cartões lado a lado */
            justify-content: space-around; /* Espaço igual à volta dos cartões */
            flex-wrap: wrap; /* Se não couberem, vão para a linha de baixo */
        }
        .cartao-atalho {
            background-color: #ffffff;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            width: calc(33% - 40px); /* Para 3 cartões por linha, com algum espaço */
            margin: 10px;
            text-align: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .cartao-atalho:hover {
            transform: translateY(-5px); /* Efeito de levantar o cartão */
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        .cartao-atalho h3 {
            margin-top: 0;
            color: #007bff;
        }
        .cartao-atalho p {
            font-size: 0.95rem;
            color: #6c757d;
            margin-bottom: 15px;
        }
        .cartao-atalho .btn-atalho {
            display: inline-block;
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 0.9rem;
        }
         .cartao-atalho .btn-atalho:hover {
            background-color: #0056b3;
        }

        /* Ajuste para ecrãs mais pequenos (menos de 768px) */
        @media (max-width: 768px) {
            .cartao-atalho {
                width: calc(50% - 30px); /* 2 cartões por linha */
            }
        }
        @media (max-width: 500px) {
            .cartao-atalho {
                width: 100%; /* 1 cartão por linha */
                margin-left: 0;
                margin-right: 0;
            }
        }

    </style>
</head>
<body>
    
    <%-- Inclui o menu de navegação que definimos em _partials/menu.jsp --%>
    <jsp:include page="_partials/menu.jsp" />

    <div class="main-content">
        <div class="dashboard-bemvindo">
            <h1>Bem-vindo ao SGHB!</h1>
            <p>O seu Sistema de Gestão Hoteleira Básico. Use o menu acima para começar.</p>
        </div>

        <h2>Atalhos Rápidos</h2>
        <div class="atalhos-rapidos">
            <div class="cartao-atalho">
                <h3>Gerir Quartos</h3>
                <p>Veja o estado atual de todos os quartos e faça atualizações.</p>
                <a href="${pageContext.request.contextPath}/recepcionista/quartos" class="btn-atalho">Ver Quartos</a>
            </div>
            <div class="cartao-atalho">
                <h3>Gerir Hóspedes</h3>
                <p>Adicione novos hóspedes ou edite informações dos existentes.</p>
                <a href="${pageContext.request.contextPath}/recepcionista/hospedes?acao=listar" class="btn-atalho">Ver Hóspedes</a>
            </div>
            <div class="cartao-atalho">
                <h3>Gerir Reservas</h3>
                <p>Crie novas reservas ou veja a lista das reservas atuais.</p>
                <a href="${pageContext.request.contextPath}/recepcionista/reservas?acao=listar" class="btn-atalho">Ver Reservas</a>
            </div>
        </div>
        
        <%-- No futuro, aqui poderíamos adicionar mais informações úteis:
             - Número de quartos disponíveis hoje
             - Próximas chegadas (check-ins)
             - Próximas partidas (check-outs)
        --%>
        
    </div>

</body>
</html>
