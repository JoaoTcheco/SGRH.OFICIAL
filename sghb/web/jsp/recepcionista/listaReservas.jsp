<%-- 
    Document   : listaReservas
    Created on : 02/06/2025, 13:27:03
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Para formatar datas --%>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Reservas - SGHB</title>
    <jsp:include page="_partials/estilos.jsp" />
</head>
<body>
    <jsp:include page="_partials/menu.jsp" />
    <div class="main-content">
        <div class="header">
            <h1>Gestão de Reservas</h1>
            <a href="${pageContext.request.contextPath}/recepcionista/reservas?acao=novo" class="btn-novo">Adicionar Nova Reserva</a>
        </div>

        <%-- Mensagem de sucesso ao criar reserva --%>
        <c:if test="${param.reserva == 'sucesso'}">
            <p class="mensagem-formulario-sucesso">Nova reserva criada com sucesso!</p>
        </c:if>
        <c:if test="${not empty mensagemErroGeral}">
            <p class="mensagem-formulario-erro"><c:out value="${mensagemErroGeral}"/></p>
        </c:if>

        <table>
            <thead>
                <tr>
                    <th>Hóspede</th>
                    <th>Quarto</th>
                    <th>Check-in</th>
                    <th>Check-out</th>
                    <th>Estado da Reserva</th>
                    <%-- No futuro, poderíamos ter uma coluna para Ações (Ex: Cancelar, Check-in) --%>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="reserva" items="${listaReservas}">
                    <tr>
                        <%-- Mostra o nome do hóspede e o número do quarto (que vieram do JOIN no ReservaDAO) --%>
                        <td><c:out value="${reserva.hospede.nomeCompleto}" /></td>
                        <td><c:out value="${reserva.quarto.numeroQuarto}" /></td>
                        <%-- Formata a data para um formato mais legível (dd/MM/yyyy) --%>
                        <td><fmt:formatDate value="${reserva.dataCheckin}" pattern="dd/MM/yyyy" type="date"/></td>
                        <td><fmt:formatDate value="${reserva.dataCheckout}" pattern="dd/MM/yyyy" type="date"/></td>
                        <td><c:out value="${reserva.statusReserva.descricao}" /></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listaReservas}">
                    <tr>
                        <td colspan="5" style="text-align:center;">Ainda não há reservas registadas.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>
