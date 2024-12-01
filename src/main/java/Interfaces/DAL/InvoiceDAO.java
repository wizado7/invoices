package Interfaces.DAL;

import entity.Invoice;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceDAO {
    void addInvoice(Invoice invoice);
    Invoice getInvoiceById(int id);
    List<Invoice> searchInvoices(String firmName, String invoiceDate);


    List<Invoice> getInvoicesWithPagination(int limit, int offset) throws SQLException;

    int getTotalInvoiceCount() throws SQLException;

    List<Invoice> getAllInvoices();
    void updateInvoice(Invoice invoice);
    void deleteInvoice(long id);
    List<Invoice> getInvoicesByFirmName(String firmName);

    List<Invoice> getInvoicesByFirmNameAndDate(String firmName, String invoiceDate);

    List<Invoice> getInvoicesByDate(String invoiceDate);

    int getTotalSearchCount(String search) throws SQLException;

    List<Invoice> searchInvoicesWithPagination(String search, int limit, int offset) throws SQLException;


    List<Invoice> searchInvoicesByDateAndName(String search, Integer day, Integer month, Integer year, int limit, int offset) throws SQLException;

    int getTotalCountByDateAndName(String search, Integer day, Integer month, Integer year) throws SQLException;
}
