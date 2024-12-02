<%@ page import="entity.Item" %>
<%@ page import="entity.InvoiceItem" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Редактирование накладной</title>
    <link href="/style/table-style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="header-container">
    <h2>Редактирование накладной</h2>
    <a class="button_back" href="${pageContext.request.getHeader('Referer')}">Назад</a>
</div>
<div class="tables-container">
    <div class="table-mid">
<form action="/invoices/update" method="post">

    <input type="hidden" name="invoiceId" value="<%= ((entity.Invoice) request.getAttribute("invoice")).getId() %>">


    <label for="firmName">Название фирмы:</label>
    <input type="text" id="firmName" name="firmName" value="<%= ((entity.Firm) request.getAttribute("firm")).getName() %>" required>
    <br><br>

    <h2>Изменение количества товаров</h2>
    <table border="1">
        <thead>
        <tr>
            <th>Название товара</th>
            <th>Цена</th>
            <th>Текущее количество</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Item> items = (List<Item>) request.getAttribute("items");
            List<InvoiceItem> invoiceItems = (List<InvoiceItem>) request.getAttribute("invoiceItems");

            if (items != null && invoiceItems != null) {
                for (Item item : items) {
                    InvoiceItem matchedItem = invoiceItems.stream()
                            .filter(ii -> ii.getItemId() == item.getId())
                            .findFirst()
                            .orElse(null);

                    int quantity = matchedItem != null ? matchedItem.getQuantity() : 0;
        %>
        <tr>
            <td><%= item.getName() %></td>
            <td><%= item.getPrice() %></td>
            <td>
                <input type="number" name="item_<%= item.getId() %>" min="0" step="1" value="<%= quantity %>">
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
    <form action="/invoices/update" method="post">
        <input type="hidden" name="page" value="${currentPage}"/>
        <input type="hidden" name="search" value="${searchParam}"/>
        <input type="hidden" name="day" value="${param.day}">
        <input type="hidden" name="month" value="${param.month}">
        <input type="hidden" name="year" value="${param.year}">
        <button class="button1" type="submit">Сохранить изменения</button>
    </form>
</form>
    </div>
</div>
<br>
</body>
</html>
