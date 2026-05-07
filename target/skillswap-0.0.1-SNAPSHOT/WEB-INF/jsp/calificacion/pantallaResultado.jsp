<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Resultado de Calificación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Calificación registrada</h1>
            <p>Tu evaluación se guardó correctamente.</p>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>

        <c:if test="${not empty calificacion}">
            <div class="summary-grid">
                <div class="summary-card"><strong>ID calificación</strong>${calificacion.id}</div>
                <div class="summary-card"><strong>ID sesión</strong>${calificacion.idSesion}</div>
                <div class="summary-card"><strong>Puntuación</strong>${calificacion.puntuacion}</div>
                <div class="summary-card"><strong>Usuario evaluado</strong>${idUsuarioEvaluado}</div>
                <div class="summary-card"><strong>Reputación actualizada</strong>${reputacionActualizada}</div>
            </div>
            <div class="panel">
                <strong>Comentario</strong>
                <p>${calificacion.comentario}</p>
            </div>
        </c:if>

        <div class="actions footer-actions">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/calificacion/sesiones-finalizadas">Volver a Mis Sesiones Finalizadas</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/match/explorar">Ir a Explorar Matches</a>
            <a class="btn btn-secondary" href="javascript:history.back()">Volver atras</a>
        </div>
    </div>
</div>

</body>
</html>
