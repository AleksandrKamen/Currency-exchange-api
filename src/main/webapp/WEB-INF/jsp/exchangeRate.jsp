<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@ include file="headers/headerExchangeRates.jsp"  %>
<h1>Информация о выбранном курсе</h1>

<c:if test="${not empty requestScope.exchangeRate}">
ID:${requestScope.exchangeRate.id} Base Currency Name: <a href="currency?code=${requestScope.exchangeRate.baseCurrencyCode}">${requestScope.exchangeRate.baseCurrencyName}</a>
Target Currency Name:<a href="currency?code=${requestScope.exchangeRate.targetCurrencyCode}">${requestScope.exchangeRate.targetCurrencyName}</a>
Rate: ${requestScope.exchangeRate.rate}
</c:if>

<c:if test="${not empty requestScope.converted}">
    From Currency  ${requestScope.converted.fromCurrencyName},<a href="currency?code=${requestScope.converted.fromCurrencyCode}"> code: ${requestScope.converted.fromCurrencyCode}</a>
    To Currency ${requestScope.converted.toCurrencyName},<a href="currency?code=${requestScope.converted.toCurrencyCode}"> code: ${requestScope.converted.toCurrencyCode}</a>
    Rate:${requestScope.converted.rate}, Amount: ${requestScope.converted.amount} Result = ${requestScope.converted.result}

</c:if>


<c:if test="${not empty requestScope.errors}">
    <div style="color: red">
        <c:forEach var="error" items="${requestScope.errors}">
            <samp>${error.message}</samp>
        </c:forEach>

    </div>

</c:if>

</body>
</html>
