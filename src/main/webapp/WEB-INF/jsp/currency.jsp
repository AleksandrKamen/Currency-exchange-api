<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
</head>

<body>
<%@ include file="headers/headerCurrencies.jsp"  %>
<h1>Информация о выбранной валюте</h1>

 ID:${requestScope.currency.id} name: ${requestScope.currency.name} code:${requestScope.currency.code} sign: ${requestScope.currency.sign}

<c:if test="${not empty requestScope.errors}">
    <div style="color: red">
        <c:forEach var="error" items="${requestScope.errors}">
            <samp>${error.message}</samp>
        </c:forEach>
    </div>

</c:if>
</body>
</html>
