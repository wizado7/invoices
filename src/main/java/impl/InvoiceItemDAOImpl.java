package impl;

import Interfaces.DAL.InvoiceItemDAO;
import entity.InvoiceItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InvoiceItemDAOImpl implements InvoiceItemDAO {
    private final Connection connection;

    public InvoiceItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addInvoiceItem(InvoiceItem invoiceItem) {

    }

    @Override
    public List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId) {
        return null;
    }

    @Override
    public void deleteInvoiceItemsByInvoiceId(int invoice) {

    }

    @Override
    public void updateQuantity(int invoiceId, int itemId, int quantity) {

    }

    @Override
    public void updateInvoiceItem(InvoiceItem invoiceItem) {

    }

    @Override
    public void deleteInvoiceItem(long id) {

    }

    @Override
    public void deleteInvoiceItem(int invoiceId, int itemId) throws SQLException {

    }

    @Override
    public void updateInvoiceItem(int invoiceId, int id, int quantity) throws SQLException {

    }

    @Override
    public InvoiceItem getInvoiceItemByInvoiceAndItemId(int invoiceId, int itemId) {
        return null;
    }
}
