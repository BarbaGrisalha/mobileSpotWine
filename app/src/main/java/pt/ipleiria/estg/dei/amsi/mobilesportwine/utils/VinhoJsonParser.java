package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Order;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class VinhoJsonParser {
    public static ArrayList<Vinho> parserJsonVinhos(JSONArray response){
        ArrayList<Vinho> vinhos = new ArrayList<>();

        try{
            for(int i = 0; i < response.length(); i++){
                JSONObject vinho = (JSONObject) response.get(i);

                int idVinho = vinho.getInt("id"); // Correção: era "product_id"
                String name = vinho.getString("name");
                String description = vinho.getString("description");
                String category = vinho.getString("category"); // Novo campo
                double price = vinho.getDouble("price"); // Correção: Converter de String para Double
                int stock = vinho.getInt("stock"); // Novo campo
                String image = vinho.getString("image"); // Correção: Adicionar imagem

                Vinho auxVinho = new Vinho(idVinho, name, description, category, price, stock, image);
                vinhos.add(auxVinho);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vinhos;
    }


    public static Vinho parserJsonVinho(String response){
        Vinho auxVinho = null;

        try{
            JSONObject vinho = new JSONObject(response);

            int idVinho = vinho.getInt("id"); // Correção
            String name = vinho.getString("name");
            String description = vinho.getString("description");
            String category = vinho.getString("category"); // Novo campo
            double price = vinho.getDouble("price"); // Correção
            int stock = vinho.getInt("stock"); // Novo campo
            String image = vinho.getString("image"); // Correção

            auxVinho = new Vinho(idVinho, name, description, category, price, stock, image);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return auxVinho;
    }


    //TODO: PARSERJSONLOGIN

    public static List<Order> parserJsonOrders(JSONArray ordersArray) {
        return null;//só para não se perder
    }
}
