<%--
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
    <title>Создание записи</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"></script>
    <script type="text/javascript">
        function sendEvent(method) {
            let data = {
                id: document.getElementById('id').value,
                name: document.getElementById('name').value,
                present: document.getElementById('present').value,
                eventType: document.getElementById('eventType').value,
                description: document.getElementById('description').value,
                dateTime: document.getElementById('dateTime').value
            }
            fetch('http://localhost:8080/event',
                {
                    method: method,
                    redirect: 'follow',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
            document.location.href = 'http://localhost:8080/event/byTypes?types=ANY'
        }
    </script>
</head>
<body>
<ul class="nav">
    <li class="nav-item">
        <a class="nav-link" aria-current="page" href="http://localhost:8080/collectionPage.jsp">Список записей</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" href="#">Создать запись</a>
    </li>
</ul>
<div class="container">
    <div class="input-group mb-3">
        <label class="input-group-text">Тип события</label>
        <select class="form-select m-2" aria-label="Default select example" id="eventType" name="eventType">
            <option selected value="BIRTHDAY">День рождения</option>
            <option value="MEETING">Встреча</option>
        </select>
    </div>
    <div class="input-group mb-3">
        <input type="text" class="form-control" placeholder="Имя" aria-label="Имя" aria-describedby="name-addon"
               id="name" name="name">
        <span class="input-group-text" id="name-addon">Имя собеседника/именинника</span>
    </div>
    <label for="dateTime" class="form-label"></label>
    <div class="input-group mb-3">
        <span class="input-group-text" id="basic-addon3">Дата и время</span>
        <input type="datetime-local" class="form-control" name="dateTime" id="dateTime" aria-describedby="date-addon">
    </div>

    <div class="input-group mb-3">
        <input type="text" class="form-control" placeholder="Описание" aria-label="Описание"
               aria-describedby="description-addon"
               id="description" name="description">
        <span class="input-group-text" id="description-addon">Описание</span>
    </div>
    <div class="input-group mb-3">
        <input type="text" class="form-control" placeholder="Подарок" aria-label="Подарок"
               aria-describedby="present-addon"
               id="present" name="present">
        <span class="input-group-text" id="present-addon">Подарок</span>
    </div>
    <div class="input-group mb-3">
        <input type="number" min="0" class="form-control" placeholder="Id" aria-label="Id"
               aria-describedby="present-addon"
               id="id" name="id">
        <span class="input-group-text" id="id-addon">Id</span>
    </div>
    <div class="input-group mb-3">
        <button class="btn btn-primary m-4" value="POST" onclick="sendEvent(this.value)">Создать</button>
        <button class="btn btn-primary m-4" value="PUT" onclick="sendEvent(this.value)">Обновить</button>
    </div>


</div>

</body>
</html>
