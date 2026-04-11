<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Sesion Confirmada</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Sesión iniciada correctamente</h1>
            <p>Tu acceso a SkillSwap está listo.</p>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>

        <div class="actions-row">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/login">Cerrar sesión</a>
        </div>
    </div>
</div>

</body>
</html>
