<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Búsqueda de matches</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card">
        <div class="card-header">
            <h1>Buscar matches</h1>
            <p>Encuentra personas con habilidades complementarias dentro de SkillSwap.</p>
        </div>

        <div class="empty-state">
            Aquí se mostrará el panel de búsqueda cuando el controlador envíe los criterios y resultados.
        </div>

        <div class="footer-actions">
            <a class="inline-link" href="${pageContext.request.contextPath}/login">Volver al inicio</a>
        </div>
    </div>
</div>
</body>
</html>
