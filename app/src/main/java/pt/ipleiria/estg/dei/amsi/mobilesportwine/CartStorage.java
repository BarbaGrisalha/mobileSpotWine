package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.CartModel;
//converter os produtos em Json, PARA PODER Armazená-los com string.
public class CartStorage {
    private static final String PREFS_NAME = "CartPrefs";
    private static final String CART_KEY = "cart_items";

    public static void saveCart(Context context, List<CartModel.Item> cartItems){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    public static List<CartModel.Item> loadCart(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CART_KEY, null);
        Type type = new TypeToken<ArrayList<CartModel.Item>>() {}.getType();
        return  json != null ? gson.fromJson(json,type) : new ArrayList<>();
    }

    public static void clearCart(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_KEY);
        editor.apply();
    }
}
