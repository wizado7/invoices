package impl;

import Interfaces.BLL.InvoiceService;
import Interfaces.DAL.InvoiceDAO;
import Interfaces.DAL.InvoiceItemDAO;
import entity.Invoice;
import entity.InvoiceItem;

import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceDAO invoiceDAO;
    private final InvoiceItemDAO invoiceItemDAO;


    public InvoiceServiceImpl(InvoiceDAO invoiceDAO, InvoiceItemDAO invoiceItemDAO) {
        this.invoiceDAO = invoiceDAO;
        this.invoiceItemDAO = invoiceItemDAO;


    }

    @Override
    public void createInvoice(Invoice invoice, List<InvoiceItem> items) {
        invoiceDAO.addInvoice(invoice);

        if (invoice.getId() > 0) {
            for (InvoiceItem item : items) {
                item.setInvoiceId(invoice.getId());
                invoiceItemDAO.addInvoiceItem(item);
            }
        } else {
            System.out.println("Ошибка: ID накладной не установлен.");
        }
    }

    @Override
    public Invoice getInvoiceById(int id) {
        return invoiceDAO.getInvoiceById(id);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceDAO.getAllInvoices();
    }

    @Override
    public void updateInvoice(Invoice invoice, List<InvoiceItem> items) {
        invoiceDAO.updateInvoice(invoice);
        for (InvoiceItem item : items) {
            invoiceItemDAO.updateInvoiceItem(item);
        }
    }

    @Override
    public void deleteInvoice(long id) {
        invoiceDAO.deleteInvoice(id);
    }
}
