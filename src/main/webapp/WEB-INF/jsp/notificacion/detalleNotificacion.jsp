<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Detalle de notificacion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Detalle de la notificacion</h1>
        </div>

        <c:if test="${not empty notificacion}">
            <div class="summary-grid">
                <div class="summary-card"><strong>Mensaje</strong>${notificacion.mensaje}</div>
                <div class="summary-card"><strong>Fecha propuesta</strong>${notificacion.fechaPropuesta}</div>
                <div class="summary-card"><strong>Habilidades</strong>${notificacion.habilidades}</div>
            </div>
            <div class="actions">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/notificaciones">Volver a notificaciones</a>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/notificaciones/${notificacion.id}/sesion">Ver detalle de sesion</a>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>

