<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Title</title>
</head>
<body>

<h1>Доступные Валюты</h1>
<ul>
    <c:forEach var="currencies" items="${requestScope.currencies}">
        <li>
         <a href="currency?code=${currencies.code}">${currencies.name}</a>
        <br>
        </li>
    </c:forEach>
</ul>

<div style="position: absolute; top: 0; right: 0">
    <%@ include file="headers/headerMenu.jsp"%>
<h2>Добавить валюту</h2>
<form action="/currencies" method="post" enctype="application/x-www-form-urlencoded">
    <label for="name"> Currency name:
        <input type="text" name="name" id="name" maxlength="50" required>
    </label> <br>
    <label for="code"> Code:
        <input type="text" name="code" id="code" minlength="3" maxlength="3" required>
    </label> <br>
    <label for="sign"> Sign:
        <input type="text" name="sign" id="sign" maxlength="3" required>
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

    <c:if test="${not empty requestScope.newCurrency}">
        <div style="color: green">
           Валюта: ${requestScope.newCurrency.name} успешно добавлена
        </div>
    </c:if>

 </form>
</div>
</body>

</html>
