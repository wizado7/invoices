<%@ page import="entity.Item" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Обновление товара</title>
    <link href="/style/create.css" rel="stylesheet" type="text/css">
    <link href="/style/table-style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="header-container">
    <h2>Обновление товара</h2>
    <a class="button_back" href="/items">Назад</a>
</div>

<%
    Item item = (Item) request.getAttribute("item");
    if (item == null) {
%>
<p>Ошибка: товар не найден</p>
<%
} else {
%>
<form action="/items/update" method="post">
    <input type="hidden" name="id" value="<%= item.getId() %>">
    <p>Текущий ID: <%= item.getId() %></p>

    <label for="name">Название товара:</label>
    <input type="text" id="name" name="name" value="<%= item.getName() %>" required>
    <br><br>

    <label for="price">Цена:</label>
    <input type="number" id="price" name="price" step="0.01" value="<%= item.getPrice() %>" required>
    <br><br>

    <button class="button1" type="submit">Обновить</button>
</form>
<%
    }
%>

<br>

</body>
</html>
