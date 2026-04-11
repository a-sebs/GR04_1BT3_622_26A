<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Agenda de sesiones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Agenda de sesiones</h1>
            <p>Consulta y organiza tus encuentros dentro de SkillSwap.</p>
        </div>

        <div class="summary-grid">
            <div class="summary-card">
                <strong>Próxima sesión</strong>
                <span class="muted">Aún no hay sesiones programadas.</span>
            </div>
            <div class="summary-card">
                <strong>Estado</strong>
                <span class="muted">Esta vista quedará lista cuando el controlador envíe los datos.</span>
            </div>
        </div>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Volver al inicio</a>
        </div>
    </div>
</div>
</body>
</html>
