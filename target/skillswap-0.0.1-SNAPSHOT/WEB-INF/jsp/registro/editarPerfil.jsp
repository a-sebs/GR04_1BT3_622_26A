<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Editar Perfil - SkillSwap</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Editar Perfil de Usuario</h1>
            <p>Actualice su nombre de usuario y correo electrónico.</p>
        </div>

        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>
        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/registro/actualizar">
            <input type="hidden" name="usuarioId" value="${usuario.id}" />

            <div class="field">
                <label for="nombre">Nombre de usuario</label>
                <input id="nombre" name="nombre" type="text" value="${usuario.nombre}" placeholder="Dejar en blanco para no cambiar" />
                <small class="field-hint">Solo caracteres alfanuméricos, sin espacios ni caracteres especiales.</small>
            </div>

            <div class="field">
                <label for="correo">Correo electrónico</label>
                <input id="correo" name="correo" type="email" value="${usuario.correo}" placeholder="Dejar en blanco para no cambiar" />
                <small class="field-hint">Debe cumplir con el formato de correo válido.</small>
            </div>

            <button type="submit" class="btn btn-primary">Guardar cambios</button>
        </form>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/sesion/confirmada">Volver al perfil</a>
        </div>
    </div>
</div>

</body>
</html>

