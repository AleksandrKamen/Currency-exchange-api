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
    <c:forEach var="exchangeRates" items="${requestScope.exchangeRates}">
        <li>
         ID:<a href="exchange?baseCode=${exchangeRates.baseCurrencyCode}&targetCode=${exchangeRates.targetCurrencyCode}">${exchangeRates.id}</a>
            <a href="currency?code=${exchangeRates.baseCurrencyCode}">  ${exchangeRates.baseCurrencyName}</a> ---
            <a href="currency?code=${exchangeRates.targetCurrencyCode}">${exchangeRates.targetCurrencyName}</a> = ${exchangeRates.rate}
                 <br>
            <br>
        </li>

    </c:forEach>
</ul>

<div style="position: absolute; top: 0; right: 0">
<%@ include file="headers/headerMenu.jsp"%>
<h2>Добавить/Обновить обменный курс</h2>
<form action="/exchangeRates" method="post" enctype="application/x-www-form-urlencoded">
    <label for="baseCurrency"> Base currency (code):
        <input type="text" name="baseCurrency" id="baseCurrency" maxlength="3" required>
    </label> <br>
    <label for="targetCurrency"> Target currency(code):
        <input type="text" name="targetCurrency" id="targetCurrency" maxlength="3" required>
    </label> <br>
    <label for="rate"> Rate:
        <input type="number" name="rate" id="rate" min="0.000001" max="100000" step="0.000001" required>
    </label> <br>
    <button type="submit">Send</button>

    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <samp>${error.message}</samp>
                <br>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${not empty requestScope.newExchangeRate}">
        <div style="color: green">
            Новый курс: ${requestScope.newExchangeRate.baseCurrencyCode}---
                ${requestScope.newExchangeRate.targetCurrencyCode}=${requestScope.newExchangeRate.rate}  успешно добавлен
        </div>
    </c:if>

    <c:if test="${not empty requestScope.update}">
        <div style="color: green">
            Курс: ${requestScope.update.baseCurrencyCode}---
                ${requestScope.update.targetCurrencyCode}=${requestScope.update.rate}  успешно обновлен
        </div>
    </c:if>

</form>
</div>

<div style="position: absolute; bottom: 0; right: 0">
    <h2>Произвести расчет курса</h2>
    <form action="/exchange" method="post" enctype="application/x-www-form-urlencoded">
        <label for="from"> Currency from (code):
            <input type="text" name="from" id="from" maxlength="3" required>
        </label> <br>
        <label for="to"> Currency to(code):
            <input type="text" name="to" id="to" maxlength="3" required>
        </label> <br>
        <label for="amount"> Amount:
            <input type="number" name="amount" id="amount" min="0.000001" max="100000" step="0.000001" required>
        </label> <br>
        <button type="submit">Calculate</button>

        <c:if test="${not empty requestScope.errors}">
            <div style="color: red">
                <c:forEach var="error" items="${requestScope.errors}">
                    <samp>${error.message}</samp>
                    <br>
                </c:forEach>
            </div>
        </c:if>

    </form>
</div>

</body>
</html>
