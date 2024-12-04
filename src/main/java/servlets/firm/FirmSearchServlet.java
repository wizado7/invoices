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
import java.util.List;

@WebServlet("/firms/search")
public class FirmSearchServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firmName = request.getParameter("firmName");
        String firmAddress = request.getParameter("firmAddress");
        String firmPhone = request.getParameter("firmPhone");

        List<Firm> firms;

        if (firmName != null && !firmName.isEmpty()) {
            firms = firmDAO.getFirmsByName(firmName);
        } else if (firmAddress != null && !firmAddress.isEmpty()) {
            firms = firmDAO.getFirmsByAddress(firmAddress);
        } else if (firmPhone != null && !firmPhone.isEmpty()) {
            firms = firmDAO.getFirmsByPhone(firmPhone);
        } else {
            firms = firmDAO.getAllFirms();
        }

        request.setAttribute("firms", firms);
        request.getRequestDispatcher("/firms.jsp").forward(request, response);
    }
}