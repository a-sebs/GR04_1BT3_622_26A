<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Sesion Confirmada</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Sesion iniciada correctamente</h1>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>
        <c:if test="${not empty notificacion}">
            <p class="message message-warning">${notificacion}</p>
        </c:if>

        <c:if test="${not empty sesionAgendada}">
            <h2>Detalle de la solicitud</h2>
            <div class="summary-grid">
                <div class="summary-card"><strong>Destinatario</strong>${destinatarioNombre}</div>
                <div class="summary-card"><strong>ID Match</strong>${sesionAgendada.idMatch}</div>
                <div class="summary-card"><strong>Fecha</strong>${sesionAgendada.fecha}</div>
                <div class="summary-card"><strong>Hora</strong>${sesionAgendada.hora}</div>
                <div class="summary-card"><strong>Estado</strong>${sesionAgendada.estado}</div>
            </div>
            <c:if test="${not empty mensajeIntroduccion}">
                <div class="panel">
                    <strong>Mensaje</strong>
                    <p>${mensajeIntroduccion}</p>
                </div>
            </c:if>
            <c:if test="${sesionAgendada.estado ne 'FINALIZADA' and sesionAgendada.estado ne 'CALIFICADA'}">
                <form method="post" action="${pageContext.request.contextPath}/sesion/finalizar/${sesionAgendada.id}">
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Marcar sesión como finalizada</button>
                    </div>
                </form>
            </c:if>
        </c:if>

        <div class="actions footer-actions">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/match/explorar">Explorar Matches</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/calificacion/sesiones-finalizadas">Mis Sesiones Finalizadas</a>

        </div>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Cerrar sesion</a>
        </div>
    </div>
</div>

</body>
</html>
