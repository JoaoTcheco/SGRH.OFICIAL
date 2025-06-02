<%-- 
    Document   : formularioHospede
    Created on : 02/06/2025, 13:26:07
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%-- O título da página muda se estamos a adicionar ou a editar --%>
    <title>
        <c:if test="${not empty hospede}">Editar Hóspede</c:if> <%-- Se o objeto 'hospede' existir (veio do Servlet), estamos a editar --%>
        <c:if test="${empty hospede}">Adicionar Novo Hóspede</c:if> <%-- Se não existir, estamos a adicionar um novo --%>
        - SGHB
    </title>
    <jsp:include page="_partials/estilos.jsp" />
</head>
<body>
    <jsp:include page="_partials/menu.jsp" />
    <div class="main-content">
        <h1>
            <c:if test="${not empty hospede}">Editar Dados do Hóspede</c:if>
            <c:if test="${empty hospede}">Registar Novo Hóspede</c:if>
        </h1>

        <%-- Formulário para adicionar/editar. Envia os dados para o HospedeServlet por POST --%>
        <form action="${pageContext.request.contextPath}/recepcionista/hospedes" method="post" class="form-container">
            
            <%-- Se estivermos a editar, incluímos o ID do hóspede num campo escondido --%>
            <%-- Assim, o Servlet sabe qual hóspede deve atualizar --%>
            <c:if test="${not empty hospede}">
                <input type="hidden" name="idHospede" value="<c:out value='${hospede.idHospede}' />" />
            </c:if>

            <div class="form-group">
                <label for="nomeCompleto">Nome Completo:</label>
                <%-- O 'value' do campo é preenchido com os dados do hóspede se estivermos a editar --%>
                <input type="text" id="nomeCompleto" name="nomeCompleto" value="<c:out value='${hospede.nomeCompleto}' />" required>
            </div>
            <div class="form-group">
                <label for="documentoIdentidadeNumero">Número do Documento de Identidade:</label>
                <input type="text" id="documentoIdentidadeNumero" name="documentoIdentidadeNumero" value="<c:out value='${hospede.documentoIdentidadeNumero}' />" required>
            </div>
            <div class="form-group">
                <label for="telefone">Telefone:</label>
                <input type="tel" id="telefone" name="telefone" value="<c:out value='${hospede.telefone}' />">
            </div>
            <div class="form-group">
                <label for="email">Email (opcional):</label>
                <input type="email" id="email" name="email" value="<c:out value='${hospede.email}' />">
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn-novo">Guardar Dados</button>
                <%-- Botão para cancelar e voltar para a lista de hóspedes --%>
                <a href="${pageContext.request.contextPath}/recepcionista/hospedes?acao=listar" class="btn-cancel">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>

