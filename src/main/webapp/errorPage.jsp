<%@ page import="java.util.Arrays" %><%--
  Created by IntelliJ IDEA.
  User: ruaaaxs
  Date: 14.12.2021
  Time: 23:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Ошибка</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <div class="row align-items-center">
        <div class="col-4">
        </div>
        <div class="col-4 m-3 justify-items-center">
            <form action="http://localhost:8080/collectionPage.jsp" method="get">
                <button type="submit" class="btn m-3 btn-secondary">На главную</button>
            </form>
        </div>
        <div class="col-4">
        </div>
    </div>
    <% Exception e = (Exception) request.getAttribute("error");%>
    <div class="row align-items-center my-3">
        Ошибка: <%=e.getClass().getName()%>
    </div>
    <div class="row align-items-center my-3">
        Сообщение: <%=e.getMessage()%>
    </div>
    <div class="row align-items-center my-3">
        <c:forEach var="i" items="<%=e.getStackTrace()%>">
            <c:out value="${i}"/></c:forEach>
    </div>
</div>
</body>
</html>
