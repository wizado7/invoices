<%@ page import="entity.Invoice" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Item" %>
<%@ page import="entity.Firm" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Фирмы</title>
    <link href="/style/table-style.css" rel="stylesheet" type="text/css">
    <script src="/script/validate.js"></script>
</head>
<body>

<div class="table-mid">
    <div class="header-container">
        <h2>Фирмы</h2>
        <a class="button_back" href="/">Назад</a>
    </div>
    <div class="search-container">
        <form action="/firms/search" method="get">
            <label for="firmName">Название фирмы:</label>
            <input type="text" id="firmName" name="firmName">

            <label for="firmAddress">Адрес:</label>
            <input type="text" id="firmAddress" name="firmAddress">

            <label for="firmPhone">Номер фирмы</label>
            <input
                    type="tel"
                    id="firmPhone"
                    name="firmPhone"
                    pattern="^\+7\d{10}$"
                    oninput="validatePhone(this)"
                    placeholder="+7XXXXXXXXXX"
            >

            <button class="button1" type="submit">Поиск</button>
        </form>
    </div>
    <table border="1">
        <thead>
        <tr>
            <th>ID</th>
            <th>Фирма</th>
            <th>Адрес</th>
            <th>Телефон</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Firm> firms = (List<Firm>) request.getAttribute("firms");
            if (firms != null && !firms.isEmpty()) {
                for (Firm firm : firms) {
        %>
        <tr>
            <td><%= firm.getId() %></td>
            <td><%= firm.getName() %></td>
            <td><%= firm.getAddress() %></td>
            <td><%= firm.getPhone() %></td>
            <td>
                <a class="button1" href="/firms/update?id=<%= firm.getId() %>">Редактировать</a>
                <form action="/firms/delete" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= firm.getId() %>">
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
    <a class="button1" href="/firms/create">Добавить новую фирму</a>
</div>
</div>


</body>
</html>
