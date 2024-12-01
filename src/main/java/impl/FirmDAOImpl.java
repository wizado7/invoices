package impl;

import Interfaces.DAL.FirmDAO;
import entity.Firm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FirmDAOImpl implements FirmDAO {

    private final Connection connection;

    public FirmDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addFirm(Firm firm) {
        String sql = "INSERT INTO firms (name, address, phone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firm.getName());
            stmt.setString(2, firm.getAddress());
            stmt.setString(3, firm.getPhone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Firm getFirmById(long id) {
        return null;
    }

    @Override
    public List<Firm> getAllFirms() {
        return null;
    }

    @Override
    public void updateFirm(Firm firm) {

    }

    @Override
    public void deleteFirm(long id) {

    }

    @Override
    public Firm getFirmByName(String firmName) {
        return null;
    }

    @Override
    public List<Firm> getFirmsByName(String name) {
        return null;
    }

    @Override
    public List<Firm> getFirmsByAddress(String address) {
        return null;
    }

    @Override
    public List<Firm> getFirmsByPhone(String phone) {
        return null;
    }
}
