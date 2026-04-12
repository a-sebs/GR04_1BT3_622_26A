<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Agendar Intercambio</title>
</head>
<body>
<h1>Agendar sesión de intercambio</h1>

<c:if test="${not empty matchSeleccionado}">
    <p><strong>Match:</strong> ${matchSeleccionado.usuarioMatch.nombre}</p>
    <p><strong>Correo:</strong> ${matchSeleccionado.usuarioMatch.correo}</p>
</c:if>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<h2>Disponibilidad sugerida (horarios ocupados)</h2>
<ul>
    <c:forEach items="${horariosOcupados}" var="horario">
        <li>${horario}</li>
    </c:forEach>
    <c:if test="${empty horariosOcupados}">
        <li>No hay bloqueos registrados.</li>
    </c:if>
</ul>

<form method="post" action="${pageContext.request.contextPath}/sesion/agenda/${matchSeleccionado.id}">
    <div>
        <label for="fecha">Fecha:</label>
        <input id="fecha" name="fecha" type="date" value="${fecha}" required />
    </div>
    <div>
        <label for="hora">Hora:</label>
        <input id="hora" name="hora" type="time" value="${hora}" required />
    </div>
    <div>
        <label for="mensaje">Mensaje de introducción:</label><br/>
        <textarea id="mensaje" name="mensaje" rows="4" cols="50" required>${mensajeTexto}</textarea>
    </div>
    <button type="submit">Confirmar solicitud</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/match/detalle/${matchSeleccionado.id}">Volver al detalle del perfil</a>
</p>

</body>
</html>
