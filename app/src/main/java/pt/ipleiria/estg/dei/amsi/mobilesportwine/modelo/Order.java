package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

public class Order {
    private int id;
    private String date;
    private double total;

    public Order(int id,String date, double total){
        this.id= id;
        this.date = date;
        this.total = total;
    }

    public int getId() {return id;}
    public String getDate(){return date;}
    public double getTotal(){return getTotal();}
}
