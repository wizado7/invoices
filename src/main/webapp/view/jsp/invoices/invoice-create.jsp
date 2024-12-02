<%@ page import="entity.Item" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Создание новой накладной</title>
    <link href="/style/table-style.css" rel="stylesheet" type="text/css" >
</head>
<body>
<div class="header-container">
    <h2>Создание новой накладной</h2>
    <a class="button_back" href="${pageContext.request.getHeader('Referer')}">Назад</a>
</div>
<div class="tables-container">
    <div class="table-mid">


        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <p class="error" style="color:red;"><%= error %></p>
        <%
            }
        %>

        <form action="/invoices/create" method="post">
            <label for="firmName">Название фирмы:</label>
            <input type="text" id="firmName" name="firmName"
                   value="<%= request.getAttribute("firmName") != null ? request.getAttribute("firmName") : "" %>" required>
            <br><br>

            <h2>Добавление товаров</h2>
            <table border="1">
                <thead>
                <tr>
                    <th>Название товара</th>
                    <th>Цена</th>
                    <th>Количество</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Item> items = (List<Item>) request.getAttribute("items");
                    if (items != null) {
                        for (Item item : items) {
                %>
                <tr>
                    <td><%= item.getName() %></td>
                    <td><%= item.getPrice() %></td>
                    <td>
                        <input type="number" name="item_<%= item.getId() %>" min="0" step="1" value="0">
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="3">Нет доступных товаров.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <br>
            <button class="button1" type="submit">Создать накладную</button>
        </form>
    </div>
</div>
<br>
</body>

</html>
