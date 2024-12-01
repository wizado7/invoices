package Interfaces.DAL;

import entity.InvoiceItem;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceItemDAO {
    void addInvoiceItem(InvoiceItem invoiceItem);
    List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId);
    void deleteInvoiceItemsByInvoiceId(int invoice);
    void updateQuantity(int invoiceId, int itemId, int quantity);
    void updateInvoiceItem(InvoiceItem invoiceItem);
    void deleteInvoiceItem(long id);
    void deleteInvoiceItem(int invoiceId, int itemId) throws SQLException;

    void updateInvoiceItem(int invoiceId, int id, int quantity) throws SQLException;
    InvoiceItem getInvoiceItemByInvoiceAndItemId(int invoiceId, int itemId);

}
