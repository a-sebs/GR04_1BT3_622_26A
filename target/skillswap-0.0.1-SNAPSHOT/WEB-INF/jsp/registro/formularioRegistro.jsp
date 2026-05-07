<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Registro SkillSwap</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Crear cuenta en SkillSwap</h1>
            <p>Complete sus datos personales para crear su usuario.</p>
        </div>

        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>
        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/registro">
            <div class="field">
                <label for="nombre">Nombre de usuario</label>
                <input id="nombre" name="nombre" type="text" value="${nombre}" required />
            </div>

            <div class="field">
                <label for="password">Contraseña</label>
                <input id="password" name="password" type="password" required />
            </div>

            <div class="field">
                <label for="correo">Correo electrónico</label>
                <input id="correo" name="correo" type="email" value="${correo}" required />
            </div>

            <button type="submit" class="btn btn-primary">Registrar cuenta</button>
        </form>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Volver al login</a>
        </div>
    </div>
</div>

</body>
</html>
