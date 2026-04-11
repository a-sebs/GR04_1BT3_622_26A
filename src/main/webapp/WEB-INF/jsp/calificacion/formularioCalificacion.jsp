<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Formulario de Calificación</title>
</head>
<body>
<h1>Mis Sesiones Finalizadas</h1>

<c:if test="${empty sesionesFinalizadas}">
    <p>No tienes sesiones finalizadas pendientes de calificación.</p>
</c:if>

<c:if test="${not empty sesionesFinalizadas}">
    <table border="1" cellpadding="6" cellspacing="0">
        <thead>
        <tr>
            <th>ID Sesión</th>
            <th>ID Match</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Acción</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${sesionesFinalizadas}" var="sesion">
            <tr>
                <td>${sesion.id}</td>
                <td>${sesion.idMatch}</td>
                <td>${sesion.fecha}</td>
                <td>${sesion.hora}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/calificacion/formulario/${sesion.id}">Calificar sesión</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${not empty sesionSeleccionada}">
    <h2>Calificar sesión ${sesionSeleccionada.id}</h2>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/calificacion/registrar">
        <input type="hidden" name="idSesion" value="${sesionSeleccionada.id}" />

        <div>
            <label for="puntuacion">Puntuación (1-5):</label>
            <select id="puntuacion" name="puntuacion" required>
                <option value="">Seleccione</option>
                <c:forEach begin="1" end="5" var="n">
                    <option value="${n}" <c:if test="${puntuacion == n}">selected</c:if>>${n}</option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="comentario">Comentario:</label><br/>
            <textarea id="comentario" name="comentario" rows="4" cols="60" required>${comentario}</textarea>
        </div>

        <button type="submit">Enviar Calificación</button>
    </form>
</c:if>

</body>
</html>
