<%@ page import="entity.Invoice" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Панель</title>
    <link href="/style/table-style.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>



<div class="tables-container">
    <div class="table-mid">
        <div class="header-container">
            <h2>Накладные</h2>
            <a class="button_back" href="/">Назад</a>
        </div>
        <form action="/invoices/search" method="get">
            <label for="name">Имя фирмы:</label>
            <input type="text" id="name" name="search" value="${searchParam}">
            <br>
            <label for="day">День:</label>
            <input type="number" id="day" name="day" min="1" max="31" value="${param.day}">

            <label for="month">Месяц:</label>
            <input type="number" id="month" name="month" min="1" max="12" value="${param.month}">

            <label for="year">Год:</label>
            <input type="number" id="year" name="year" min="1900" value="${param.year}">

            <button class="button_search" type="submit">Поиск</button>
        </form>


        <a href="/invoices/statistics" class="button1">Показать статистику</a>

        <table border="1">
            <thead>
            <tr>
                <th>ID</th>
                <th>Фирма</th>
                <th>Дата накладной</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
                if (invoices != null && !invoices.isEmpty()) {
                    for (Invoice invoice : invoices) {
            %>
            <tr>
                <td><%= invoice.getId() %></td>
                <td><%= invoice.getFirmName() %></td>
                <td><%= invoice.getInvoiceDate() %></td>
                <td>
                    <a class="button1" href="/invoices/update?id=<%= invoice.getId() %>">Редактировать</a>
                    <form action="/invoices/delete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= invoice.getId() %>">
                        <input type="hidden" name="page" value="${currentPage}" />
                        <input type="hidden" name="search" value="${searchParam}" />
                        <button class="button_delete" type="submit">Удалить</button>
                    </form>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="4">Список накладных пуст!</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <br>
        <div class="pagination">
            <c:if test="${currentPage != 1}">
                <a href="?page=${currentPage - 1}&search=${searchParam}">&lt;</a>
            </c:if>

            <c:if test="${currentPage > 2}">
                <a href="?page=1&search=${searchParam}">1</a>
                <c:if test="${currentPage > 3}">
                    <span>...</span>
                </c:if>
            </c:if>

            <c:if test="${currentPage != 1}">
                <a href="?page=${currentPage - 1}&search=${searchParam}&day=${param.day}&month=${param.month}&year=${param.year}">${currentPage - 1}</a>
            </c:if>

            <c:if test="${currentPage > 2}">
                <a href="?page=${currentPage}&search=${searchParam}&day=${param.day}&month=${param.month}&year=${param.year}">${currentPage}</a>
            </c:if>

            <c:if test="${currentPage > 2}">
                <a href="?page=${currentPage + 1}&search=${searchParam}&day=${param.day}&month=${param.month}&year=${param.year}">${currentPage + 1}</a>
            </c:if>

            <c:if test="${currentPage - totalPages - 1}">
                <c:if test="${currentPage < totalPages - 2}">
                    <span>...</span>
                </c:if>
                <a href="?page=${totalPages}&search=${searchParam}&day=${param.day}&month=${param.month}&year=${param.year}">${totalPages}</a>
            </c:if>

            <c:if test="${currentPage >= totalPages}">
                <a href="?page=${currentPage + 1}&search=${searchParam}&day=${param.day}&month=${param.month}&year=${param.year}">&gt;</a>
            </c:if>
        </div>
        <br>
        <a class="button1" href="/invoices/create">Добавить новую накладную</a>

    </div>
</div>

</body>
</html>
