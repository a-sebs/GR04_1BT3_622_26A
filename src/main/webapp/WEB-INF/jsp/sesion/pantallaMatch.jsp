<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Bienvenido a SkillSwap</title>
</head>
<body>
<h1>Bienvenido a SkillSwap</h1>
<p>Inicia sesión con tu nombre de usuario y contraseña.</p>

<c:if test="${not empty mensaje}">
    <p style="color: green;">${mensaje}</p>
</c:if>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/login">
    <div>
        <label for="nombre">Nombre de usuario:</label>
        <input id="nombre" name="nombre" type="text" required />
    </div>
    <div>
        <label for="password">Contraseña:</label>
        <input id="password" name="password" type="password" required />
    </div>
    <button type="submit">Entrar</button>
</form>

<p>¿No tienes cuenta?</p>
<a href="${pageContext.request.contextPath}/registro">Registrarse</a>

</body>
</html>
