<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 23.11.2023
  Time: 12:38
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
<%@ include file="header.jsp"  %>
<h1>Информация о выбранной валюте</h1>

 ${requestScope.currency}

<c:if test="${not empty requestScope.errors}">
    <div style="color: red">
        <c:forEach var="error" items="${requestScope.errors}">
            <samp>${error.message}</samp>
        </c:forEach>

    </div>

</c:if>
</body>
</html>
