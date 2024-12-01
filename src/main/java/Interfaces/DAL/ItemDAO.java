package Interfaces.DAL;

import entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {
    void addItem(Item item);
    Item getItemById(int id);
    List<Item> getAllItems() throws SQLException;
    void updateItem(Item item);
    void deleteItem(long id);
    List<Item> getItemsByName(String name);
    List<Item> getItemsByPrice(double price);
}
