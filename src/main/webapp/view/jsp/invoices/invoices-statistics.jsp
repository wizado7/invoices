<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Статистика по накладным</title>
    <link href="/style/table-style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="header-container">
    <h2>Статистика по накладным</h2>
    <a class="button_back" href="${pageContext.request.getHeader('Referer')}">Назад</a>
</div>

<table border="1">
    <tr>
        <th>Общее количество накладных</th>
        <td><%= request.getAttribute("totalInvoices") %></td>
    </tr>
    <tr>
        <th>Общее количество фирм</th>
        <td><%= request.getAttribute("totalFirms") %></td>
    </tr>
    <tr>
        <th>Общая сумма по всем накладным</th>
        <td><%= request.getAttribute("totalAmount") %> руб.</td>
    </tr>
</table>

<br>
</body>
</html>
