package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class VinhoJsonParser {
    public static ArrayList<Vinho> parserJsonVinhos(JSONArray response){
        ArrayList<Vinho> vinhos = new ArrayList<>();

        try{
            for(int i = 0; i < response.length(); i++){
                JSONObject vinho = (JSONObject) response.get(i);
                int idVinho = vinho.getInt("product_id");
                String name = vinho.getString("name");
                String description = vinho.getString("description");
                Double price = vinho.getDouble("price");

                Vinho auxVinho = new Vinho(idVinho, name, description, price);
                vinhos.add(auxVinho);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        return vinhos;
    }

    public static Vinho parserJsonVinho(String response){
        Vinho auxVinho = null;

        try{
            JSONObject vinho = new JSONObject(response);
            int idVinho = vinho.getInt("product_id");
            String name = vinho.getString("name");
            String description = vinho.getString("description");
            Double price = vinho.getDouble("price");

            auxVinho = new Vinho(idVinho, name, description, price);

        }catch( JSONException e){
            e.printStackTrace();
        }

        return auxVinho;
    }

    //TODO: PARSERJSONLOGIN


    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
