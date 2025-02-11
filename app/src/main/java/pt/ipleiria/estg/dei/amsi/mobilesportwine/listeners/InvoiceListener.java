package pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners;

import java.util.ArrayList;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Invoice;

public interface InvoiceListener {
    void onRefreshInvoices(ArrayList<Invoice> invoices);
}