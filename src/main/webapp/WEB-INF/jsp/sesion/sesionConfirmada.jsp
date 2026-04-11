<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Sesion Confirmada</title>
</head>
<body>
<h1>Sesion iniciada correctamente</h1>

<c:if test="${not empty mensaje}">
    <p style="color: green;">${mensaje}</p>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/match/explorar">Explorar Matches</a>
</p>

<a href="${pageContext.request.contextPath}/login">Cerrar sesion</a>

</body>
</html>
