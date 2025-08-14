package Service;


import Model.Book;
import Model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceService {
    private List<Invoice> invoices = new ArrayList<>();
    private int invoiceCounter = 1;

    public Invoice createInvoice(Book book) {
        Invoice invoice = new Invoice(invoiceCounter++, book);
        invoices.add(invoice);
        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        return invoices;
    }
}
