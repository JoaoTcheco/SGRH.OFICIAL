<%-- 
    Document   : index
    Created on : 02/06/2025, 14:30:57
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>

<%-- Esta página não mostra nada, só manda o utilizador logo para a página de login. --%>
<jsp:forward page="/jsp/publico/login.jsp" />
