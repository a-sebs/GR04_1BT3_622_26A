<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Lista de Matches</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Lista de Matches</h1>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>
        <div class="panel">
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
                            <a href="${pageContext.request.contextPath}/match/detalle/${item.id}">Ver perfil</a>
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
        </div>

        <div class="actions footer-actions">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/match/explorar">Volver a explorar</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/sesion/confirmada">Volver atras</a>
        </div>
    </div>
</div>

</body>
</html>
