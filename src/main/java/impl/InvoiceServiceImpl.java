package impl;

import Interfaces.BLL.InvoiceService;
import entity.Invoice;
import entity.InvoiceItem;

import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public void createInvoice(Invoice invoice, List<InvoiceItem> items) {

    }

    @Override
    public Invoice getInvoiceById(int id) {
        return null;
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return null;
    }

    @Override
    public void updateInvoice(Invoice invoice, List<InvoiceItem> items) {

    }

    @Override
    public void deleteInvoice(long id) {

    }
}
