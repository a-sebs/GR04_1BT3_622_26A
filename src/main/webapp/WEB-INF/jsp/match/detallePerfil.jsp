<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Detalle del Perfil</title>
</head>
<body>
<h1>Detalle del Perfil</h1>

<c:if test="${empty matchSeleccionado}">
    <p>No se encontró el match solicitado.</p>
</c:if>

<c:if test="${not empty matchSeleccionado}">
    <p><strong>Usuario:</strong> ${matchSeleccionado.usuarioMatch.nombre}</p>
    <p><strong>Correo:</strong> ${matchSeleccionado.usuarioMatch.correo}</p>
    <p><strong>Compatibilidad:</strong> ${matchSeleccionado.compatibilidad}</p>

    <c:if test="${not empty perfilDetalle}">
        <p><strong>Reputación:</strong> ${perfilDetalle.reputacion}</p>

        <p><strong>Habilidades que ofrece:</strong></p>
        <ul>
            <c:forEach items="${perfilDetalle.habilidadesOfrece}" var="habilidad">
                <li>${habilidad}</li>
            </c:forEach>
        </ul>

        <p><strong>Habilidades que desea aprender:</strong></p>
        <ul>
            <c:forEach items="${perfilDetalle.habilidadesBusca}" var="habilidad">
                <li>${habilidad}</li>
            </c:forEach>
        </ul>
    </c:if>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/match/lista">Volver a la lista de matches</a>
</p>

</body>
</html>
