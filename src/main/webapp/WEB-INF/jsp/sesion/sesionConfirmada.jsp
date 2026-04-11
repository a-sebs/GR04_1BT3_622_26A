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
<c:if test="${not empty notificacion}">
    <p style="color: blue;">${notificacion}</p>
</c:if>

<c:if test="${not empty sesionAgendada}">
    <h2>Detalle de la solicitud</h2>
    <p><strong>Destinatario:</strong> ${destinatarioNombre}</p>
    <p><strong>ID Match:</strong> ${sesionAgendada.idMatch}</p>
    <p><strong>Fecha:</strong> ${sesionAgendada.fecha}</p>
    <p><strong>Hora:</strong> ${sesionAgendada.hora}</p>
    <p><strong>Estado:</strong> ${sesionAgendada.estado}</p>
    <c:if test="${not empty mensajeIntroduccion}">
        <p><strong>Mensaje:</strong> ${mensajeIntroduccion}</p>
    </c:if>
    <c:if test="${sesionAgendada.estado ne 'FINALIZADA' and sesionAgendada.estado ne 'CALIFICADA'}">
        <form method="post" action="${pageContext.request.contextPath}/sesion/finalizar/${sesionAgendada.id}">
            <button type="submit">Marcar sesión como finalizada</button>
        </form>
    </c:if>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/match/explorar">Explorar Matches</a>
</p>

<p>
    <a href="${pageContext.request.contextPath}/calificacion/sesiones-finalizadas">Mis Sesiones Finalizadas</a>
</p>

<a href="${pageContext.request.contextPath}/login">Cerrar sesion</a>

</body>
</html>
