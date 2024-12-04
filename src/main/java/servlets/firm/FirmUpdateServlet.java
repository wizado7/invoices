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

        try (Connection connection = ConnectionManager.getConnection()) {
            FirmDAO firmDAO = new FirmDAOImpl(connection);


            Firm firm = firmDAO.getFirmById(firmId);
            if (firm == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Фирма с ID " + firmId + " не найден");
                return;
            }


            req.setAttribute("firm", firm);
            req.getRequestDispatcher("/firm-update.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка базы данных");
        }
    }

}
