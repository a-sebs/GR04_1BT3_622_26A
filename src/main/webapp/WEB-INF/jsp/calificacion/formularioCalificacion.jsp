<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Formulario de Calificación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Mis Sesiones Finalizadas</h1>
            <p>Selecciona una sesión y registra tu calificación.</p>
        </div>

        <c:if test="${empty sesionesFinalizadas}">
            <p class="empty-state">No tienes sesiones finalizadas pendientes de calificación.</p>
            <div class="actions">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/sesion/confirmada">Volver al inicio</a>

            </div>
        </c:if>

        <c:if test="${not empty sesionesFinalizadas}">
            <div class="panel">
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
            </div>
        </c:if>

        <c:if test="${not empty sesionSeleccionada}">
            <div class="card-header">
                <h2>Calificar sesión ${sesionSeleccionada.id}</h2>
            </div>
            <c:if test="${not empty error}">
                <p class="message message-error">${error}</p>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/calificacion/registrar">
                <input type="hidden" name="idSesion" value="${sesionSeleccionada.id}" />

                <div class="field">
                    <label for="puntuacion">Puntuación (1-5):</label>
                    <select id="puntuacion" name="puntuacion" required>
                        <option value="">Seleccione</option>
                        <c:forEach begin="1" end="5" var="n">
                            <option value="${n}" <c:if test="${puntuacion == n}">selected</c:if>>${n}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field">
                    <label for="comentario">Comentario:</label>
                    <textarea id="comentario" name="comentario" rows="4" cols="60" required>${comentario}</textarea>
                </div>

                <div class="actions">
                    <button type="submit" class="btn btn-primary">Enviar Calificación</button>
                    <a class="btn btn-secondary" href="javascript:history.back()">Volver atras</a>
                </div>
            </form>
        </c:if>
    </div>
</div>

</body>
</html>
