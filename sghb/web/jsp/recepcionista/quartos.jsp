<%-- 
    Document   : quartos
    Created on : 02/06/2025, 13:27:17
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- Importa a JSTL para usar tags como c:forEach --%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestão de Quartos - SGHB</title>
    <jsp:include page="_partials/estilos.jsp" /> <%-- Inclui os estilos comuns --%>
    <style>
        /* Estilos específicos para a tabela de quartos, se necessário */
        .status-select { /* Estilo para a caixa de seleção de estado */
            padding: 6px 10px; 
            border-radius: 4px; 
            border: 1px solid #ccc; 
            margin-right: 10px;
            min-width: 150px; /* Largura mínima para a caixa de seleção */
        }
        .form-atualizar-status { /* Para alinhar a caixa de seleção e o botão */
            display: flex;
            align-items: center;
        }
    </style>
</head>
<body>

    <jsp:include page="_partials/menu.jsp" /> <%-- Inclui o menu de navegação --%>

    <div class="main-content">
        <div class="header">
            <h1>Gestão de Quartos</h1>
            <%-- Não há botão de "Adicionar Novo" aqui, porque os quartos são geralmente fixos --%>
            <%-- A gestão de adicionar/remover quartos poderia ser uma funcionalidade do Técnico --%>
        </div>
        
        <%-- Mostra mensagens de erro se o QuartoServlet enviar alguma --%>
        <%
            String erroQuarto = request.getParameter("erro");
            if (erroQuarto != null) {
        %>
            <p class="mensagem-formulario-erro">
                <% if ("id_quarto_invalido".equals(erroQuarto)) { %>
                    Ocorreu um erro: ID do quarto inválido.
                <% } else if ("status_quarto_invalido".equals(erroQuarto)) { %>
                    Ocorreu um erro: Estado do quarto inválido.
                <% } else { %>
                    Ocorreu um erro desconhecido ao tentar atualizar o quarto.
                <% } %>
            </p>
        <%
            }
        %>

        <table>
            <thead>
                <tr>
                    <th>Número do Quarto</th>
                    <th>Estado Atual</th>
                    <th style="width: 300px;">Alterar Estado</th> <%-- Coluna um pouco mais larga para o formulário --%>
                </tr>
            </thead>
            <tbody>
                <%-- Usa a tag c:forEach da JSTL para repetir para cada quarto na lista --%>
                <%-- A "listaQuartos" foi enviada pelo QuartoServlet --%>
                <c:forEach var="quarto" items="${listaQuartos}">
                    <tr>
                        <td><c:out value="${quarto.numeroQuarto}" /></td> <%-- Mostra o número do quarto --%>
                        <td>
                            <%-- Mostra o estado do quarto com uma cor diferente dependendo do estado --%>
                            <%-- A classe CSS (ex: status-Disponível) é definida em estilos.jsp --%>
                            <span class="status-${quarto.statusQuarto.name()}"><c:out value="${quarto.statusQuarto.descricao}" /></span>
                        </td>
                        <td>
                            <%-- Pequeno formulário para mudar o estado deste quarto específico --%>
                            <%-- Envia os dados para o QuartoServlet usando o método POST --%>
                            <form action="${pageContext.request.contextPath}/recepcionista/quartos" method="post" class="form-atualizar-status">
                                <input type="hidden" name="idQuarto" value="${quarto.idQuarto}"> <%-- ID do quarto (escondido) --%>
                                
                                <select name="novoStatus" class="status-select">
                                    <%-- Repete para cada estado possível definido no Enum Quarto.StatusQuarto --%>
                                    <c:forEach var="status" items="${Quarto.StatusQuarto.values()}">
                                        <%-- Marca como 'selected' o estado atual do quarto --%>
                                        <option value="${status.name()}" ${quarto.statusQuarto == status ? 'selected' : ''}>
                                            <c:out value="${status.descricao}" />
                                        </option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="btn-action btn-edit">Atualizar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <%-- Se a lista de quartos estiver vazia --%>
                <c:if test="${empty listaQuartos}">
                    <tr>
                        <td colspan="3" style="text-align:center;">Ainda não há quartos registados no sistema.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

</body>
</html>


