<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Registro SkillSwap</title>
</head>
<body>
<h1>Crear cuenta en SkillSwap</h1>
<p>Complete sus datos personales para crear su usuario.</p>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<c:if test="${not empty mensaje}">
    <p style="color: green;">${mensaje}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/registro">
    <div>
        <label for="nombre">Nombre de usuario</label>
        <input id="nombre" name="nombre" type="text" value="${nombre}" required />
    </div>

    <div>
        <label for="password">Contraseña</label>
        <input id="password" name="password" type="password" required />
    </div>

    <div>
        <label for="correo">Correo electrónico</label>
        <input id="correo" name="correo" type="email" value="${correo}" required />
    </div>

    <button type="submit">Registrar cuenta</button>
</form>

<a href="${pageContext.request.contextPath}/login">Volver al login</a>

</body>
</html>
