<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 27.11.2023
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Информация о выбранном курсе</h1>

ID:${requestScope.exchangeRate.id} Base Currency Name: <a href="currency?code=${requestScope.exchangeRate.baseCurrencyCode}">${requestScope.exchangeRate.baseCurrencyName}</a>
Target Currency Name:<a href="currency?code=${requestScope.exchangeRate.targetCurrencyCode}">${requestScope.exchangeRate.targetCurrencyName}</a>
Rate: ${requestScope.exchangeRate.rate}

<c:if test="${not empty requestScope.errors}">
    <div style="color: red">
        <c:forEach var="error" items="${requestScope.errors}">
            <samp>${error.message}</samp>
        </c:forEach>

    </div>

</c:if>

</body>
</html>
