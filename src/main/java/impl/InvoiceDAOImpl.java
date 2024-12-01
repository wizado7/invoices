package impl;

import Interfaces.DAL.InvoiceDAO;
import entity.Invoice;

import java.sql.SQLException;
import java.util.List;

public class InvoiceDAOImpl  implements InvoiceDAO {
    @Override
    public void addInvoice(Invoice invoice) {

    }

    @Override
    public Invoice getInvoiceById(int id) {
        return null;
    }

    @Override
    public List<Invoice> searchInvoices(String firmName, String invoiceDate) {
        return null;
    }

    @Override
    public List<Invoice> getInvoicesWithPagination(int limit, int offset) throws SQLException {
        return null;
    }

    @Override
    public int getTotalInvoiceCount() throws SQLException {
        return 0;
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return null;
    }

    @Override
    public void updateInvoice(Invoice invoice) {

    }

    @Override
    public void deleteInvoice(long id) {

    }

    @Override
    public List<Invoice> getInvoicesByFirmName(String firmName) {
        return null;
    }

    @Override
    public List<Invoice> getInvoicesByFirmNameAndDate(String firmName, String invoiceDate) {
        return null;
    }

    @Override
    public List<Invoice> getInvoicesByDate(String invoiceDate) {
        return null;
    }

    @Override
    public int getTotalSearchCount(String search) throws SQLException {
        return 0;
    }

    @Override
    public List<Invoice> searchInvoicesWithPagination(String search, int limit, int offset) throws SQLException {
        return null;
    }

    @Override
    public List<Invoice> searchInvoicesByDateAndName(String search, Integer day, Integer month, Integer year, int limit, int offset) throws SQLException {
        return null;
    }

    @Override
    public int getTotalCountByDateAndName(String search, Integer day, Integer month, Integer year) throws SQLException {
        return 0;
    }
}
