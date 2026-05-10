<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Detalle del Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Detalle del Perfil</h1>
        </div>

        <c:if test="${empty matchSeleccionado}">
            <p class="empty-state">No se encontró el match solicitado.</p>
        </c:if>

        <c:if test="${not empty matchSeleccionado}">
            <div class="summary-grid">
                <div class="summary-card"><strong>Usuario</strong>${matchSeleccionado.usuarioMatch.nombre}</div>
                <div class="summary-card"><strong>Correo</strong>${matchSeleccionado.usuarioMatch.correo}</div>
                <div class="summary-card"><strong>Compatibilidad</strong>${matchSeleccionado.compatibilidad}</div>
                <c:if test="${not empty perfilDetalle}">
                    <div class="summary-card"><strong>Reputación</strong>${perfilDetalle.reputacion}</div>
                </c:if>
            </div>

            <c:if test="${not empty perfilDetalle}">
                <div class="grid-2">
                    <div class="panel">
                        <strong>Habilidades que ofrece</strong>
                        <ul class="list">
                            <c:forEach items="${perfilDetalle.habilidadesOfrece}" var="habilidad">
                                <li class="list-item">${habilidad}</li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="panel">
                        <strong>Habilidades que desea aprender</strong>
                        <ul class="list">
                            <c:forEach items="${perfilDetalle.habilidadesBusca}" var="habilidad">
                                <li class="list-item">${habilidad}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </c:if>

            <div class="actions footer-actions">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/sesion/agenda/${matchSeleccionado.id}">Contactar Usuario</a>
            </div>
        </c:if>

        <div class="actions footer-actions">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/match/lista">Volver atras</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/sesion/confirmada">Ir a sesion confirmada</a>
        </div>
    </div>
</div>

</body>
</html>
