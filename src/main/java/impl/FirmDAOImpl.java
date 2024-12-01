package impl;

import Interfaces.DAL.FirmDAO;
import entity.Firm;

import java.sql.*;
import java.util.ArrayList;
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
        String sql = "SELECT * FROM firms WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Firm(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Firm> getAllFirms() {
        List<Firm> firms = new ArrayList<>();
        String sql = "SELECT * FROM firms";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                firms.add(new Firm(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firms;
    }

    @Override
    public void updateFirm(Firm firm) {
        String sql = "UPDATE firms SET name = ?, address = ?, phone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firm.getName());
            stmt.setString(2, firm.getAddress());
            stmt.setString(3, firm.getPhone());
            stmt.setLong(4, firm.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFirm(long id) {
        String sql = "DELETE FROM firms WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Firm getFirmByName(String name) {
        String sql = "SELECT * FROM firms WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Firm firm = new Firm();
                firm.setId(rs.getInt("id"));
                firm.setName(rs.getString("name"));
                return firm;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Firm> getFirmsByField(String field, String value) {
        List<Firm> firms = new ArrayList<>();
        String query = "SELECT * FROM firms WHERE " + field + " LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + value + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Firm firm = new Firm();
                    firm.setId((int) rs.getLong("id"));
                    firm.setName(rs.getString("name"));
                    firm.setAddress(rs.getString("address"));
                    firm.setPhone(rs.getString("phone"));
                    firms.add(firm);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firms;
    }

    @Override
    public List<Firm> getFirmsByName(String name) {
        return getFirmsByField("name", name);
    }

    @Override
    public List<Firm> getFirmsByAddress(String address) {
        return getFirmsByField("address", address);
    }

    @Override
    public List<Firm> getFirmsByPhone(String phone) {
        return getFirmsByField("phone", phone);
    }
}

