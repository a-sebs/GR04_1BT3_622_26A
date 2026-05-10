<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Agendar Intercambio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Agendar sesión de intercambio</h1>
        </div>

        <c:if test="${not empty matchSeleccionado}">
            <div class="summary-grid">
                <div class="summary-card"><strong>Match</strong>${matchSeleccionado.usuarioMatch.nombre}</div>
                <div class="summary-card"><strong>Correo</strong>${matchSeleccionado.usuarioMatch.correo}</div>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>

        <div class="panel">
            <h2>Disponibilidad sugerida (horarios ocupados)</h2>
            <ul class="list">
                <c:forEach items="${horariosOcupados}" var="horario">
                    <li class="list-item">${horario}</li>
                </c:forEach>
                <c:if test="${empty horariosOcupados}">
                    <li class="list-item">No hay bloqueos registrados.</li>
                </c:if>
            </ul>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/sesion/agenda/${matchSeleccionado.id}">
            <div class="field">
                <label for="fecha">Fecha:</label>
                <input id="fecha" name="fecha" type="date" value="${fecha}" required />
            </div>
            <div class="field">
                <label for="hora">Hora:</label>
                <input id="hora" name="hora" type="time" value="${hora}" required />
            </div>
            <div class="field">
                <label for="mensaje">Mensaje de introducción:</label>
                <textarea id="mensaje" name="mensaje" rows="4" cols="50" required>${mensajeTexto}</textarea>
            </div>
            <div class="actions">
                <button type="submit" class="btn btn-primary">Enviar solicitud de contacto</button>
            </div>
        </form>

        <div class="actions footer-actions">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/match/detalle/${matchSeleccionado.id}">Volver atras</a>
        </div>
    </div>
</div>

</body>
</html>
