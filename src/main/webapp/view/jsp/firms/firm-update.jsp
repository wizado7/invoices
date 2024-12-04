<%@ page import="entity.Item" %>
<%@ page import="entity.Firm" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Обновление товара</title>
    <link href="/style/create.css" rel="stylesheet" type="text/css">
    <link href="/style/table-style.css" rel="stylesheet" type="text/css">
    <script src="/script/validate.js"></script>
</head>
<body>
<div class="header-container">
    <h2>Редактирование фирмы</h2>
    <a class="button_back" href="/firms">Назад</a>
</div>

<%
    Firm firm = (Firm) request.getAttribute("firm");
    if (firm == null) {
%>
<p>Ошибка: товар не найден</p>
<%
} else {
%>
<form action="/items/update" method="post">
    <input type="hidden" name="id" value="<%= firm.getId() %>">
    <p>Текущий ID: <%= firm.getId() %></p>

    <label for="name">Имя фирмы:</label>
    <input type="text" id="name" name="name" value="<%= firm.getName() %>" required>
    <br><br>

    <label for="address">Адрес:</label>
    <input type="text" id="address" name="address" value="<%= firm.getAddress() %>" required>


    <label for="firmPhone">Номер фирмы</label>
    <input
            type="tel"
            id="firmPhone"
            name="firmPhone"
            value="<%= firm.getPhone() %>"
            pattern="^\+7\d{10}$"
            oninput="validatePhone(this)"
            placeholder="+7XXXXXXXXXX"
            required
    >
    <br><br>

    <button class="button1" type="submit">Обновить</button>
</form>
<%
    }
%>

<br>

</body>
</html>
