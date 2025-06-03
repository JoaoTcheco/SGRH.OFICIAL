<%-- 
    Document   : listaHospedes
    Created on : 02/06/2025, 13:26:51
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Hóspedes - SGHB</title>
    <jsp:include page="_partials/estilos.jsp" />
</head>
<body>
    <jsp:include page="_partials/menu.jsp" />
    <div class="main-content">
        <div class="header">
            <h1>Gestão de Hóspedes</h1>
            <%-- Botão para ir para a página de adicionar novo hóspede --%>
            <a href="${pageContext.request.contextPath}/recepcionista/hospedes?acao=novo" class="btn-novo">Adicionar Novo Hóspede</a>
        </div>

        <%-- Mensagens de feedback (sucesso ou erro) após ações --%>
        <c:if test="${param.removido == 'sucesso'}">
            <p class="mensagem-formulario-sucesso">Hóspede removido com sucesso!</p>
        </c:if>
        <c:if test="${param.erroRemover == 'true'}">
            <p class="mensagem-formulario-erro">Não foi possível remover o hóspede. Verifique se ele tem reservas associadas.</p>
        </c:if>
         <c:if test="${param.erroSalvar == 'true'}">
            <p class="mensagem-formulario-erro">Ocorreu um erro ao tentar salvar o hóspede.</p>
        </c:if>
         <c:if test="${not empty mensagemErroGeral}">
            <p class="mensagem-formulario-erro"><c:out value="${mensagemErroGeral}"/></p>
        </c:if>

        <table>
            <thead>
                <tr>
                    <th>Nome Completo</th>
                    <th>Nº Documento</th>
                    <th>Telefone</th>
                    <th>Email</th>
                    <th style="width: 180px;">Ações</th> <%-- Coluna para os botões de Editar/Remover --%>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="hospede" items="${listaHospedes}">
                    <tr>
                        <td><c:out value="${hospede.nomeCompleto}" /></td>
                        <td><c:out value="${hospede.documentoIdentidadeNumero}" /></td>
                        <td><c:out value="${hospede.telefone}" /></td>
                        <td><c:out value="${hospede.email}" /></td>
                        <td>
                            <%-- Link para editar o hóspede. Chama o HospedeServlet com acao=editar e o ID do hóspede --%>
                            <a href="${pageContext.request.contextPath}/recepcionista/hospedes?acao=editar&id=${hospede.idHospede}" class="btn-action btn-edit">Editar</a>
                            <%-- Link para remover o hóspede. Chama o HospedeServlet com acao=remover e o ID --%>
                            <%-- O 'onclick' mostra uma mensagem de confirmação antes de apagar --%>
                            <a href="${pageContext.request.contextPath}/recepcionista/hospedes?acao=remover&id=${hospede.idHospede}" 
                               onclick="return confirm('Tem a certeza que quer apagar este hóspede: ${hospede.nomeCompleto}? Esta ação não pode ser desfeita.')" 
                               class="btn-action btn-delete">Apagar</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listaHospedes}">
                    <tr>
                        <td colspan="5" style="text-align:center;">Ainda não há hóspedes registados.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>
