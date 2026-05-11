<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Recuperar Contraseña - SkillSwap</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Recuperar Contraseña</h1>
            <p>Ingrese su correo electrónico para verificar su identidad.</p>
        </div>

        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>
        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login/verificar">
            <div class="field">
                <label for="correo">Correo Electrónico</label>
                <input id="correo" name="correo" type="email" required />
            </div>
            <button type="submit" class="btn btn-primary">Verificar correo</button>
        </form>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Cancelar</a>
        </div>
    </div>
</div>

</body>
</html>

