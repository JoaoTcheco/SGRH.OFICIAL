<%-- 
    Document   : quartos
    Created on : 02/06/2025, 13:27:17
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- Importa a JSTL --%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestão de Quartos - SGHB</title>
    <style>
        body { font-family: sans-serif; margin: 0; background-color: #f8f9fa; }
        .main-content { padding: 2rem; }
        .main-content h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 1rem; background: white; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .status-select { padding: 5px; border-radius: 4px; border: 1px solid #ccc; }
        .btn-update { padding: 6px 12px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .btn-update:hover { background-color: #0056b3; }
        .status-Disponível { color: green; font-weight: bold; }
        .status-Ocupado, .status-Reservado { color: red; font-weight: bold; }
        .status-Em_Manutenção, .status-A_Limpar { color: orange; font-weight: bold; }
    </style>
</head>
<body>

    <jsp:include page="_partials/menu.jsp" />

    <div class="main-content">
        <h1>Gestão de Quartos</h1>
        <p>Veja e atualize o estado de todos os quartos do hotel.</p>

        <table>
            <thead>
                <tr>
                    <th>Número do Quarto</th>
                    <th>Estado Atual</th>
                    <th>Alterar Estado</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="quarto" items="${listaQuartos}">
                    <tr>
                        <td>${quarto.numeroQuarto}</td>
                        <td>
                            <span class="status-${quarto.statusQuarto}">${quarto.statusQuarto.descricao}</span>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/recepcionista/quartos" method="post" style="display: flex; align-items: center; gap: 10px;">
                                <input type="hidden" name="idQuarto" value="${quarto.idQuarto}">
                                <select name="novoStatus" class="status-select">
                                    <c:forEach var="status" items="${Quarto.StatusQuarto.values()}">
                                        <option value="${status}" ${quarto.statusQuarto == status ? 'selected' : ''}>
                                            ${status.descricao}
                                        </option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="btn-update">Atualizar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</body>
</html>

