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

@WebServlet("/firms/create")
public class FirmCreateServlet extends HttpServlet {
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
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/firm-create.jsp").forward(request, response);
    }

    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String firmName = request.getParameter("firmName");
            String firmAddress = request.getParameter("firmAddress");
            String firmPhone = request.getParameter("firmPhone");

            if (firmName == null || firmAddress == null || firmPhone == null || firmName.isEmpty() || firmAddress.isEmpty() || firmPhone.isEmpty()) {
                request.setAttribute("error", "Все поля обязательны для заполнения");
                request.getRequestDispatcher("/firm-create.jsp").forward(request, response);
            }

            Firm newFirm = new Firm();
            newFirm.setName(firmName);
            newFirm.setAddress(firmAddress);
            newFirm.setPhone(firmPhone);

            firmDAO.addFirm(newFirm);
            response.sendRedirect("/invoices");
        } catch (Exception e) {
            throw new ServletException("Ошибка добавления новой фирмы");
        }
    }
}
