<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Добавить новый товар</title>
    <link href="/style/create.css" rel="stylesheet" type="text/css">
    <script src="/script/validate.js"></script>
</head>
<body>
<div class="header-container">
    <h2>Добавить новую фирму</h2>
    <a class="button_back" href="/firms">Назад</a>
</div>


<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p class="error"><%= error %></p>
<%
    }
%>

<form action="/firms/create" method="post">
    <label for="firmName">Название фирмы:</label>
    <input type="text" id="firmName" name="firmName" required>

    <label for="firmAddress">Aдрес:</label>
    <input type="text" id="firmAddress" name="firmAddress" required>

    <label for="firmPhone">Номер фирмы</label>
    <input
        type="tel"
        id="firmPhone"
        name="firmPhone"
        pattern="^\+7\d{10}$"
        oninput="validatePhone(this)"
        placeholder="+7XXXXXXXXXX"
        required
>

    <button type="submit">Создать</button>
</form>

<br>
<a href="/invoices">Вернуться назад</a>
</body>
</html>
