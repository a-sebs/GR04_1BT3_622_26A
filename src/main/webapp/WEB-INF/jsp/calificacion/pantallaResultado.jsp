<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Resultado de Calificación</title>
</head>
<body>
<h1>Calificación registrada</h1>

<c:if test="${not empty mensaje}">
    <p style="color: green;">${mensaje}</p>
</c:if>

<c:if test="${not empty calificacion}">
    <p><strong>ID calificación:</strong> ${calificacion.id}</p>
    <p><strong>ID sesión:</strong> ${calificacion.idSesion}</p>
    <p><strong>Puntuación:</strong> ${calificacion.puntuacion}</p>
    <p><strong>Comentario:</strong> ${calificacion.comentario}</p>
    <p><strong>Usuario evaluado:</strong> ${idUsuarioEvaluado}</p>
    <p><strong>Reputación actualizada:</strong> ${reputacionActualizada}</p>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/calificacion/sesiones-finalizadas">Volver a Mis Sesiones Finalizadas</a>
</p>

<p>
    <a href="${pageContext.request.contextPath}/match/explorar">Ir a Explorar Matches</a>
</p>

</body>
</html>
