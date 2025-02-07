package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

public class Order {

    private int id;
    private String invoiceNumber;
    private String invoiceDate;
    private double totalAmount;
    private String status;

    public Order(int id, String invoiceNumber, String invoiceDate, double totalAmount, String status) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}
