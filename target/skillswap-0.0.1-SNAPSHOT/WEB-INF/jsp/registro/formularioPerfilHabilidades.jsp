<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Perfil de Habilidades</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Perfil de habilidades</h1>
            <p>Seleccione las habilidades que ofrece y las que desea aprender.</p>
        </div>

        <c:if test="${not empty error}">
            <p class="message message-error">${error}</p>
        </c:if>
        <c:if test="${not empty mensaje}">
            <p class="message message-success">${mensaje}</p>
        </c:if>

        <form method="post">
            <div class="grid-2">
                <div class="checkbox-group panel">
                    <strong class="legend">Habilidades que ofrece</strong>
                    <div class="checkbox-grid">
                        <c:forEach items="${catalogoHabilidades}" var="habilidad">
                            <label class="checkbox-item">
                                <input type="checkbox" name="habilidadesOfrece" value="${habilidad}"
                                    <c:if test="${habilidadesOfreceSeleccionadas.contains(habilidad)}">checked</c:if> />
                                <span>${habilidad}</span>
                            </label>
                        </c:forEach>
                    </div>
                </div>

                <div class="checkbox-group panel">
                    <strong class="legend">Habilidades que busca aprender</strong>
                    <div class="checkbox-grid">
                        <c:forEach items="${catalogoHabilidades}" var="habilidad">
                            <label class="checkbox-item">
                                <input type="checkbox" name="habilidadesBusca" value="${habilidad}"
                                    <c:if test="${habilidadesBuscaSeleccionadas.contains(habilidad)}">checked</c:if> />
                                <span>${habilidad}</span>
                            </label>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-primary" formaction="${pageContext.request.contextPath}/registro/perfil/${usuarioId}/agregar">Agregar perfil</button>
                <button type="submit" class="btn btn-secondary" formaction="${pageContext.request.contextPath}/registro/perfil/${usuarioId}/editar">Guardar cambios</button>
            </div>
        </form>

        <form method="post" action="${pageContext.request.contextPath}/registro/perfil/${usuarioId}/eliminar">
            <div class="footer-actions">
                <button type="submit" class="btn btn-danger">Eliminar perfil</button>
                <a class="inline-link" href="${pageContext.request.contextPath}/sesion/confirmada">Volver</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>


