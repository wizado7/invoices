package impl;

import Interfaces.DAL.ItemDAO;
import entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    private final Connection connection;

    public ItemDAOImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void addItem(Item item) {

    }

    @Override
    public Item getItemById(int id) {
        return null;
    }

    @Override
    public List<Item> getAllItems() throws SQLException {
        return null;
    }

    @Override
    public void updateItem(Item item) {

    }

    @Override
    public void deleteItem(long id) {

    }

    @Override
    public List<Item> getItemsByName(String name) {
        return null;
    }

    @Override
    public List<Item> getItemsByPrice(double price) {
        return null;
    }
}
