<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Actualizar Contraseña - SkillSwap</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Actualizar Contraseña</h1>
            <p>Ingrese su nueva contraseña.</p>
        </div>

        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>
        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login/guardar-password">
            <input type="hidden" name="usuarioId" value="${usuarioId}" />

            <div class="field">
                <label for="password">Nueva Contraseña</label>
                <input id="password" name="password" type="password" required />
                <small class="field-hint">Mínimo 8 caracteres.</small>
            </div>
            <button type="submit" class="btn btn-primary">Guardar contraseña</button>
        </form>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Cancelar</a>
        </div>
    </div>
</div>

</body>
</html>

