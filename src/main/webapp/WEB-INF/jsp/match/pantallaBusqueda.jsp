<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Explorar Matches</title>
</head>
<body>
<h1>Explorar Matches</h1>
<p>Encuentra usuarios con coincidencias entre habilidades para aprender y enseñar.</p>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/match/buscar">
    <div>
        <label for="filtroHabilidad">Filtro avanzado por habilidad (opcional):</label>
        <input id="filtroHabilidad" name="filtroHabilidad" type="text" value="${filtroHabilidad}" />
    </div>

    <p>Catálogo de habilidades disponibles:</p>
    <ul>
        <c:forEach items="${catalogoHabilidades}" var="habilidad">
            <li>${habilidad}</li>
        </c:forEach>
    </ul>

    <button type="submit">Buscar matches</button>
</form>

</body>
</html>
