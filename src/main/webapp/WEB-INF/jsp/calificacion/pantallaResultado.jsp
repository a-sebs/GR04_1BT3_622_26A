<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Resultado de calificación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skillswap.css" />
</head>
<body>
<div class="page-shell">
    <div class="card compact">
        <div class="card-header">
            <h1>Resultado de calificación</h1>
            <p>Tu valoración se registró con éxito.</p>
        </div>

        <div class="message message-success">
            La sesión ha sido evaluada correctamente y el resultado se mostrará aquí.
        </div>

        <div class="actions-row">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Volver al inicio</a>
        </div>
    </div>
</div>
</body>
</html>
