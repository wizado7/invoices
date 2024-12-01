package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int id;
    private int firmId;
    private String firmName;
    private LocalDate invoiceDate;
    private double totalAmount;

    private List<InvoiceItem> items = new ArrayList<>();


    public Invoice() {}

    public Invoice(int firmId, LocalDate invoiceDate) {
        this.firmId = firmId;
        this.invoiceDate = invoiceDate;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", firmId=" + firmId +
                ", invoiceDate=" + invoiceDate +
                '}';
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirmId() {
        return firmId;
    }

    public void setFirmId(int firmId) {
        this.firmId = firmId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
    }
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
