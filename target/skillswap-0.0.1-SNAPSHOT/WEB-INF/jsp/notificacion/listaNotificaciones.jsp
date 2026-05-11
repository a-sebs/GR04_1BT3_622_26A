<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Notificaciones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Notificaciones pendientes</h1>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="message message-warning">${error}</p>
        </c:if>

        <c:choose>
            <c:when test="${empty notificaciones}">
                <p>No hay notificaciones pendientes.</p>
            </c:when>
            <c:otherwise>
                <div class="summary-grid">
                    <c:forEach items="${notificaciones}" var="item">
                        <div class="summary-card">
                            <strong>${item.mensaje}</strong>
                            <p>${item.fechaPropuesta}</p>
                            <div class="actions">
                                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/notificaciones/${item.id}">Ver detalle</a>
                                <form method="post" action="${pageContext.request.contextPath}/notificaciones/${item.id}/leida">
                                    <button type="submit" class="btn btn-primary">Marcar leida</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>

