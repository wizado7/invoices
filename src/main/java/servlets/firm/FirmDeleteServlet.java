package servlets.firm;

import Interfaces.DAL.FirmDAO;
import config.ConnectionManager;
import impl.FirmDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/firms/delete")
public class FirmDeleteServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            firmDAO.deleteFirm(id);

            resp.sendRedirect("/invoices");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка удаления фирмы");
        }
    }
}
