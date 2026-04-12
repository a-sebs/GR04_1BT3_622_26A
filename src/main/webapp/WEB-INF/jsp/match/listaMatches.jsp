<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Lista de Matches</title>
</head>
<body>
<h1>Lista de Matches</h1>

<c:if test="${not empty mensaje}">
    <p style="color: green;">${mensaje}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/match/buscar">
    <label for="filtroHabilidad">Búsqueda personalizada:</label>
    <input id="filtroHabilidad" name="filtroHabilidad" type="text" value="${filtroHabilidad}" />
    <button type="submit">Aplicar filtro</button>
</form>

<table border="1" cellpadding="6" cellspacing="0">
    <thead>
    <tr>
        <th>Usuario</th>
        <th>Compatibilidad</th>
        <th>Estado</th>
        <th>Acción</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${matches}" var="item">
        <tr>
            <td>${item.usuarioMatch.nombre}</td>
            <td>${item.compatibilidad}</td>
            <td>${item.estado}</td>
            <td>
                <a href="${pageContext.request.contextPath}/match/detalle/${item.id}">Ver detalle del perfil</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty matches}">
        <tr>
            <td colspan="4">No hay resultados para mostrar.</td>
        </tr>
    </c:if>
    </tbody>
</table>

<p>
    <a href="${pageContext.request.contextPath}/match/explorar">Volver a explorar</a>
</p>

</body>
</html>
