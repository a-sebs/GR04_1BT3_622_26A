<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Reportar Usuario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Reportar Usuario</h1>
            <p>Por favor, indica el motivo del reporte.</p>
        </div>

        <c:if test="${not empty mensajeError}">
            <p class="message message-error">${mensajeError}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/reporte/guardar">
            <input type="hidden" name="idUsuarioReportado" value="${usuarioReportado.id}">
            
            <div class="field">
                <label for="motivo">Motivo</label>
                <select id="motivo" name="motivo" required>
                    <option value="" disabled selected>Seleccione un motivo</option>
                    <option value="Spam">Spam</option>
                    <option value="Comportamiento Inapropiado">Comportamiento Inapropiado</option>
                    <option value="Acoso">Acoso</option>
                    <option value="Identidad Falsa">Identidad Falsa</option>
                </select>
            </div>
            
            <div class="field">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" maxlength="250" rows="4"></textarea>
            </div>
            
            <div class="actions">
                <button type="submit" class="btn btn-primary">Enviar Reporte</button>
            </div>
        </form>

        <div class="actions footer-actions">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/match/lista">Cancelar</a>
        </div>
    </div>
</div>
</body>
</html>
