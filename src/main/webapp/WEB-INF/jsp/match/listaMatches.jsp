<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Lista de matches</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Lista de matches</h1>
            <p>Visualiza los resultados de compatibilidad generados para SkillSwap.</p>
        </div>

        <ul class="list">
            <li class="list-item">
                <strong>Sin resultados cargados todavía</strong>
                <div class="muted">Cuando el controlador envíe matches, aparecerán aquí en formato de lista.</div>
            </li>
        </ul>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Volver al inicio</a>
        </div>
    </div>
</div>
</body>
</html>
