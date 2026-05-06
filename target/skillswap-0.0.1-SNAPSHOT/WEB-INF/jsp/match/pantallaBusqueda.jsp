<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Explorar Matches</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Explorar Matches</h1>
            <p>Encuentra usuarios con coincidencias entre habilidades para aprender y enseñar.</p>
        </div>

        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/match/buscar">
            <div class="field">
                <label for="filtroHabilidad">Catalogo de habilidades disponibles:</label>
                <select id="filtroHabilidad" name="filtroHabilidad">
                    <option value="">Seleccione una habilidad</option>
                    <c:forEach items="${catalogoHabilidades}" var="habilidad">
                        <option value="${habilidad}" <c:if test="${filtroHabilidad == habilidad}">selected</c:if>>${habilidad}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="field">
                <label for="filtroNombreUsuario">Filtro avanzado por habilidad (opcional):</label>
                <input id="filtroNombreUsuario" name="filtroNombreUsuario" type="text" list="usuariosDisponibles" value="${filtroNombreUsuario}" placeholder="Nombre del usuario" />
                <datalist id="usuariosDisponibles">
                    <c:forEach items="${usuariosDisponibles}" var="nombreUsuario">
                        <option value="${nombreUsuario}"></option>
                    </c:forEach>
                </datalist>
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-primary">Buscar matches</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/sesion/confirmada">Volver atras</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>
