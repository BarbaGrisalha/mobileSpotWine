package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

public class Vinho {
    private int id, stock;
    private String name, description, category;// Novo campo
    private double price;// Novo campo
    private String image; // Novo campo

    public Vinho(int id, String name, String description, String category, double price, int stock, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImage() { return image; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImage(String image) { this.image = image; }
}
