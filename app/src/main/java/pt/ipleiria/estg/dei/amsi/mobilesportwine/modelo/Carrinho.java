package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

import java.util.ArrayList;

public class Carrinho {
    private int id;
    private int userId;
    private ArrayList<ItemCarrinho> items;

    public Carrinho(int id, int userId, ArrayList<ItemCarrinho> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public ArrayList<ItemCarrinho> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                '}';
    }
}