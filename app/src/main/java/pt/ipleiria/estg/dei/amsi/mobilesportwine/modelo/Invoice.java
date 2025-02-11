package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

import java.util.List;

public class Invoice {
    private int invoiceId;
    private String invoiceNumber;
    private String invoiceDate;
    private String totalAmount;
    private String status;
    private List<Vinho> vinhos; // Alterado para usar Vinho

    public Invoice(int invoiceId, String invoiceNumber, String invoiceDate, String totalAmount, String status, List<Vinho> vinhos) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.vinhos = vinhos;
    }

    // Getters
    public int getInvoiceId() { return invoiceId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public String getInvoiceDate() { return invoiceDate; }
    public String getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public List<Vinho> getVinhos() { return vinhos; } // Alterado para usar Vinho
}