<%-- 
    Document   : formularioReserva
    Created on : 02/06/2025, 13:26:26
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nova Reserva - SGHB</title>
    <jsp:include page="_partials/estilos.jsp" />
</head>
<body>
    <jsp:include page="_partials/menu.jsp" />
    <div class="main-content">
        <h1>Adicionar Nova Reserva</h1>

        <%-- Mostra mensagem de erro se o Servlet enviar alguma (ex: datas inválidas) --%>
        <c:if test="${not empty erroFormulario}">
            <p class="mensagem-formulario-erro"><c:out value="${erroFormulario}"/></p>
        </c:if>

        <form action="${pageContext.request.contextPath}/recepcionista/reservas" method="post" class="form-container">
            <div class="form-group">
                <label for="idHospede">Hóspede:</label>
                <select id="idHospede" name="idHospede" required>
                    <option value="">-- Selecione um hóspede --</option>
                    <%-- A "listaHospedes" foi enviada pelo ReservaServlet --%>
                    <c:forEach var="hospede" items="${listaHospedes}">
                        <option value="${hospede.idHospede}" ${param.idHospede == hospede.idHospede ? 'selected' : ''}>
                            <c:out value="${hospede.nomeCompleto}" /> (Doc: <c:out value="${hospede.documentoIdentidadeNumero}" />)
                        </option>
                    </c:forEach>
                </select>
                 <c:if test="${empty listaHospedes}">
                    <small style="color:red;">Não há hóspedes registados. Adicione um hóspede primeiro.</small>
                </c:if>
            </div>
            
            <div class="form-group">
                <label for="dataCheckin">Data de Check-in:</label>
                <input type="date" id="dataCheckin" name="dataCheckin" value="${param.dataCheckin}" required>
            </div>
            
            <div class="form-group">
                <label for="dataCheckout">Data de Check-out:</label>
                <input type="date" id="dataCheckout" name="dataCheckout" value="${param.dataCheckout}" required>
            </div>
            
            <div class="form-group">
                <label for="idQuarto">Quarto Disponível:</label>
                <select id="idQuarto" name="idQuarto" required>
                    <option value="">-- Selecione um quarto --</option>
                    <%-- A "listaQuartos" (quartos disponíveis) foi enviada pelo ReservaServlet --%>
                    <c:forEach var="quarto" items="${listaQuartos}">
                        <option value="${quarto.idQuarto}" ${param.idQuarto == quarto.idQuarto ? 'selected' : ''}>
                           Quarto N.º <c:out value="${quarto.numeroQuarto}" />
                        </option>
                    </c:forEach>
                </select>
                <small>
                    Atenção: Esta lista mostra quartos que estavam disponíveis hoje. 
                    O sistema irá verificar a disponibilidade para as datas selecionadas ao salvar.
                    Numa versão futura, esta lista será atualizada dinamicamente.
                </small>
                 <c:if test="${empty listaQuartos && not empty listaHospedes}">
                    <small style="color:red;">Não há quartos marcados como "Disponível" neste momento.</small>
                </c:if>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn-novo" ${empty listaHospedes or empty listaQuartos ? 'disabled' : ''}>Guardar Reserva</button>
                <a href="${pageContext.request.contextPath}/recepcionista/reservas?acao=listar" class="btn-cancel">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>
