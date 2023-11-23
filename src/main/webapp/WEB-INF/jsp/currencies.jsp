<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 22.11.2023
  Time: 18:21
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
<h1>Доступные Валюты</h1>
<ul>
    <c:forEach var="currencies" items="${requestScope.currencies}">
        <li>
        <br>
        ${currencies}
        <br>
        </li>

    </c:forEach>
</ul>




</body>
</html>
