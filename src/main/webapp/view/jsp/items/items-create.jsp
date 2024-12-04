<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Добавить новый товар</title>
    <link href="/style/create.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="header-container">
    <h2>Добавить новый товар</h2>
    <a class="button_back" href="/items">Назад</a>
</div>


<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p class="error"><%= error %></p>
<%
    }
%>

<form action="/items/create" method="post">
    <label for="itemName">Название товара:</label>
    <input type="text" id="itemName" name="itemName" required>

    <label for="itemPrice">Цена:</label>
    <input type="number" id="itemPrice" name="itemPrice" step="0.01" required>

    <button type="submit">Создать</button>
</form>

<br>
<a href="/invoices">Вернуться к списку товаров</a>
</body>
</html>
