<%@ page import="entity.Invoice" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Item" %>
<%@ page import="entity.Firm" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Панель</title>
    <link href="/style/table-style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="tables-container">
    <div class="table-mid">
        <div class="header-container">
            <h2>Товары</h2>
            <a class="button_back" href="/">Назад</a>
        </div>

        <div class="search-container">
            <form action="/items/search" method="get">
                <label for="itemName">Название товара:</label>
                <input type="text" id="itemName" name="itemName">

                <label for="itemPrice">Цена:</label>
                <input type="number" id="itemPrice" name="itemPrice" step="0.01">

                <button class="button1" type="submit">Поиск</button>
            </form>
        </div>


        <table border="1">
            <thead>
            <tr>
                <th>ID</th>
                <th>Товар</th>
                <th>Цена</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Item> items = (List<Item>) request.getAttribute("items");
                if (items != null && !items.isEmpty()) {
                    for (Item item : items) {
            %>
            <tr>
                <td><%= item.getId() %></td>
                <td><%= item.getName() %></td>
                <td><%= item.getPrice() %></td>
                <td>
                    <a class="button1" href="/items/update?id=<%= item.getId() %>">Редактировать</a>
                    <form action="/items/delete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= item.getId() %>">
                        <button class="button_delete" type="submit">Удалить</button>
                    </form>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="4">Список товаров пуст!</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <a class="button1" href="/items/create">Добавить новый товар</a>
    </div>
</div>




</body>
</html>
