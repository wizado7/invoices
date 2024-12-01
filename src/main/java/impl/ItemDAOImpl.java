package impl;

import Interfaces.DAL.ItemDAO;
import entity.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    private final Connection connection;

    public ItemDAOImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void addItem(Item item) {
        String sql = "INSERT INTO items (name, price) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Item getItemById(int id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getInt("id"));
                item.setName(resultSet.getString("name"));
                item.setPrice(resultSet.getDouble("price"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT id, name, price FROM items";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public void updateItem(Item item) {
        String sql = "UPDATE items SET name = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setLong(3, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItem(long id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Item> getItemsByName(String name) {
        return getItemsByField("name", name);
    }

    @Override
    public List<Item> getItemsByPrice(double price) {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items WHERE price = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, price);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId((int) rs.getLong("id"));
                    item.setName(rs.getString("name"));
                    item.setPrice(rs.getDouble("price"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private List<Item> getItemsByField(String field, String value) {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items WHERE " + field + " LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + value + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId((int) rs.getLong("id"));
                    item.setName(rs.getString("name"));
                    item.setPrice(rs.getDouble("price"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
