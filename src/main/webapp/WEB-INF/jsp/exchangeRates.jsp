<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 23.11.2023
  Time: 18:11
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
<h1>Доступные обменные курсы</h1>

<ul>
    <c:forEach var="exchangeRates" items="${requestScope.currencies}">
        <li>
           ID: ${exchangeRates.id},${exchangeRates.baseCurrencyId},${exchangeRates.targetCurrencyId},${exchangeRates.rate},
<%--            <a href="currency?code=${currencies.code}">${currencies.name}</a>--%>
            <br>
            <br>
        </li>

    </c:forEach>
</ul>

<h2>Добавить обменный курс</h2>
<form action="/exchangeRates" method="post" enctype="application/x-www-form-urlencoded">
    <label for="baseCurrency"> Base currency:
        <input type="text" name="baseCurrency" id="baseCurrency" required>
    </label> <br>
    <label for="targetCurrency"> Target currency:
        <input type="text" name="targetCurrency" id="targetCurrency" required>
    </label> <br>
    <label for="rate"> Rate:
        <input type="text" name="rate" id="rate" required>
    </label> <br>
    <button type="submit">Send</button>

    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <samp>${error.message}</samp>
            </c:forEach>

        </div>

    </c:if>

    <c:if test="${not empty requestScope.newCurrency}">
        <div style="color: green">
            Новый курс успешно добавлен: ${requestScope.newExchangeRate.baseCurrency}->${requestScope.newExchangeRate.targetCurrency}=${requestScope.newExchangeRate.rate}
        </div>
    </c:if>


</form>
</div>



</body>
</html>