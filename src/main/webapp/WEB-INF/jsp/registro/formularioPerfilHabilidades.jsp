<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Perfil de Habilidades</title>
</head>
<body>
<h1>Perfil de habilidades</h1>
<p>Seleccione las habilidades que ofrece y las que desea aprender.</p>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<c:if test="${not empty mensaje}">
    <p style="color: green;">${mensaje}</p>
</c:if>

<form method="post">
    <div>
        <p>Habilidades que ofrece:</p>
        <c:forEach items="${catalogoHabilidades}" var="habilidad">
            <label>
                <input type="checkbox" name="habilidadesOfrece" value="${habilidad}"
                    <c:if test="${habilidadesOfreceSeleccionadas.contains(habilidad)}">checked</c:if> />
                    ${habilidad}
            </label><br/>
        </c:forEach>
    </div>

    <div>
        <p>Habilidades que busca aprender:</p>
        <c:forEach items="${catalogoHabilidades}" var="habilidad">
            <label>
                <input type="checkbox" name="habilidadesBusca" value="${habilidad}"
                    <c:if test="${habilidadesBuscaSeleccionadas.contains(habilidad)}">checked</c:if> />
                    ${habilidad}
            </label><br/>
        </c:forEach>
    </div>

    <button type="submit" formaction="${pageContext.request.contextPath}/registro/perfil/${usuarioId}/agregar">Agregar perfil</button>
    <button type="submit" formaction="${pageContext.request.contextPath}/registro/perfil/${usuarioId}/editar">Guardar cambios (Editar)</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/registro/perfil/${usuarioId}/eliminar">
    <button type="submit">Eliminar perfil</button>
</form>

</body>
</html>


