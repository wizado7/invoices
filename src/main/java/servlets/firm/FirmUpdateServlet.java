package servlets.firm;

import Interfaces.DAL.FirmDAO;
import config.ConnectionManager;
import entity.Firm;
import impl.FirmDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/firms/update")
public class FirmUpdateServlet extends HttpServlet {
    private FirmDAO firmDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = ConnectionManager.getConnection();
            this.firmDAO = new FirmDAOImpl(connection);
        } catch (SQLException e) {
            throw new ServletException("Ошибка подключения к базе данных, e");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int firmId;
        try {
            firmId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID фирмы");
            return;
        }

        Firm firm = firmDAO.getFirmById(firmId);
        if (firm == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Фирма с ID " + firmId + " не найден");
            return;
        }

        req.setAttribute("firm", firm);
        req.getRequestDispatcher("/firm-update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            String firmName = request.getParameter("firmName");
            String firmAddress = request.getParameter("firmAddress");
            String firmPhone = request.getParameter("firmPhone");


            if (idParam == null || idParam.isEmpty()) {
                throw new IllegalArgumentException("ID is missing.");
            }
            int firmId = Integer.parseInt(idParam);

            Firm firm = new Firm();
            firm.setId(firmId);
            firm.setName(firmName);
            firm.setAddress(firmAddress);
            firm.setPhone(firmPhone);

            try {
                firmDAO.updateFirm(firm);
            } catch (NumberFormatException e) {
                System.err.println("Неправильный формат номера" + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неправильный формат номера");


                response.sendRedirect("/invoices");

            } catch (IllegalArgumentException e) {
                System.err.println("Invalid input: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неправильно введеные данные");
            }

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
