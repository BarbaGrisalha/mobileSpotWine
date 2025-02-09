package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

public class ItemCarrinho {
    private int id;
    private int productId;
    private String productName;
    private int quantity;
    private double price;
    private double subtotal;

    public ItemCarrinho(int id, int productId, String productName, int quantity, double price, double subtotal) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    // Novo construtor para adicionar itens ao carrinho
    public ItemCarrinho(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return "ItemCarrinho{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}