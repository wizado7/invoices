package Interfaces.BLL;

import entity.Invoice;
import entity.InvoiceItem;

import java.util.List;

public interface InvoiceService {
    void createInvoice(Invoice invoice, List<InvoiceItem> items);
    Invoice getInvoiceById(int id);
    List<Invoice> getAllInvoices();
    void updateInvoice(Invoice invoice, List<InvoiceItem> items);
    void deleteInvoice(long id);

}