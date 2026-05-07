<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Bienvenido a SkillSwap</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Bienvenido a SkillSwap</h1>
            <p>Inicia sesión con tu nombre de usuario y contraseña.</p>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="field">
                <label for="nombre">Nombre de usuario</label>
                <input id="nombre" name="nombre" type="text" required />
            </div>
            <div class="field">
                <label for="password">Contraseña</label>
                <input id="password" name="password" type="password" required />
            </div>
            <button type="submit" class="btn btn-primary">Entrar</button>
        </form>

        <div class="footer-actions">
            <p class="muted">¿No tienes cuenta?</p>
            <a class="inline-link" href="${pageContext.request.contextPath}/registro">Registrarse</a>
        </div>
    </div>
</div>

</body>
</html>
