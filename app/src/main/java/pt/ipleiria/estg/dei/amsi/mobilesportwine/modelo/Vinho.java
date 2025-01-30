package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

public class Vinho {
    private int id;
    private String name, description, capa;
    private Double price;

    //Contrutores
    public Vinho(int id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Métodos Getters
    public int getId() {
        return id;
    }

    public String getCapa() {
        return capa;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    //Métodos Setters
    public void setName(String titulo) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
