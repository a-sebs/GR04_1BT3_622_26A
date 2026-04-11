<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Detalle de perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Detalle de perfil</h1>
            <p>Resumen visual del perfil seleccionado dentro de SkillSwap.</p>
        </div>

        <div class="summary-grid">
            <div class="summary-card">
                <strong>Habilidades que ofrece</strong>
                <span class="muted">Aquí se mostrarán las habilidades disponibles del usuario.</span>
            </div>
            <div class="summary-card">
                <strong>Habilidades que busca</strong>
                <span class="muted">Aquí se mostrarán las habilidades que desea aprender.</span>
            </div>
        </div>

        <div class="empty-state">
            La información detallada se renderizará cuando el controlador envíe los datos del perfil.
        </div>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Volver al inicio</a>
        </div>
    </div>
</div>
</body>
</html>
