package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Carrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;

public class CarrinhoJsonParser {
    public static Carrinho parseCarrinho(JSONObject response) {
        try {
            JSONObject cartObj = response.getJSONObject("cart");
            int cartId = cartObj.getInt("id");
            int userId = cartObj.getInt("user_id");

            JSONArray itemsArray = response.getJSONArray("items");
            ArrayList<ItemCarrinho> items = new ArrayList<>();

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObj = itemsArray.getJSONObject(i);
                int id = itemObj.getInt("id");
                int productId = itemObj.getInt("product_id");
                String productName = itemObj.getString("product_name");
                int quantity = itemObj.getInt("quantity");
                double price = itemObj.getDouble("price");
                double subtotal = itemObj.getDouble("subtotal");

                items.add(new ItemCarrinho(id, productId, productName, quantity, price, subtotal));
            }

            return new Carrinho(cartId, userId, items);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
