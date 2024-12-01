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

        <br>
        <a class="button1" href="/invoices/create">Добавить новую накладную</a>

    </div>
</div>

</body>
</html>
