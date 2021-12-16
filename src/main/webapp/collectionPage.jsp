<%@ page import="java.util.Collection" %>
<%@ page import="ru.vsu.plannerbackendv2.dao.entity.Event" %>
<%@ page import="ru.vsu.plannerbackendv2.dao.entity.EventType" %>
<%@ page import="ru.vsu.plannerbackendv2.dao.entity.Birthday" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: ruaaaxs
  Date: 14.12.2021
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Просмотр записей</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"></script>
    <script type="text/javascript">
        function deleteEvent(val) {
            fetch('http://localhost:8080/event?id=' + val,
                {
                    redirect: 'follow',
                    method: 'DELETE'
                });
            document.location.href = 'http://localhost:8080/event/byTypes?types=ANY'
        }
    </script>
</head>
<body>
<ul class="nav">
    <li class="nav-item">
        <a class="nav-link" aria-current="page" href="#">Список записей</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" href="http://localhost:8080/createEvent.jsp">Создать запись</a>
    </li>
</ul>
<div class="container p-3 m-3">
    <div class="row justify-content-center">
        <div class="col-2">
            <div class="row m-2 justify-items-center align-items-center">
                <form action="http://localhost:8080/event/byTypes" method="get" id="formTypes" name="formTypes">
                    <select class="form-select m-2" aria-label="Default select example" id="types" name="types">
                        <option selected value="ANY">Все события</option>
                        <option value="BIRTHDAY">Дни рождения</option>
                        <option value="MEETING">Встречи</option>
                    </select>
                    <button type="submit" class="btn m-3 btn-primary" form="formTypes">Найти по типу события</button>
                </form>
            </div>
            <div class="row m-2 justify-items-center align-items-center">
                <form method="get" action="http://localhost:8080/event/byDate" id="formDate" name="formDate"
                      class="text-center">
                    <label for="date" class="m-2">Дата</label>
                    <input id="date" name="date" class="m-2" type="date">
                    <select class="form-select" aria-label="Default select example" id="operation"
                            name="operation">
                        <option selected value="EQUAL">В выбранную дату</option>
                        <option value="BEFORE">До выбранной даты</option>
                        <option value="AFTER">После выбранной даты</option>
                    </select>
                    <button type="submit" class="btn m-3 btn-primary" form="formDate">Найти по дате</button>
                </form>
            </div>
            <div class="row m-2 justify-items-center align-items-center">
                <form method="get" action="http://localhost:8080/event/byName" id="formName" name="formName"
                      class="text-center">
                    <label for="name" class="m-2">Имя</label>
                    <input id="name" name="name" class="m-2" type="text">
                    <button type="submit" class="btn m-3 btn-primary" form="formName">Найти по имени</button>
                </form>
            </div>
        </div>
        <div class="col-1">
        </div>
        <div class="col-9">
            <table class="table">
                <thead>
                <tr>
                    <th>Событие</th>
                    <th>Дата</th>
                    <th>Имя собеседника/именинника</th>
                    <th>Описание</th>
                    <th>Подарок</th>
                    <th>Удаление</th>
                </tr>
                </thead>
                <tbody>
                <% Collection<Event> events = new ArrayList<>();
                    try {
                        events = (Collection<Event>) request.getAttribute("res");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (events == null) {
                        events = new ArrayList<>();
                    }
                    for (Event item : events) {%>
                <tr>
                    <td><%=item.getEventType()%>
                    </td>
                    <td><%=item.getDateTime()%>
                    </td>
                    <td><%=item.getName()%>
                    </td>
                    <td><%=item.getDescription()%>
                    </td>
                    <td><%=item.getEventType().equals(EventType.BIRTHDAY) ? ((Birthday) item).getPresent() : "-"%>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger" onclick="deleteEvent(this.value)"
                                value="<%=item.getId()%>">Удалить
                        </button>
                    </td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
