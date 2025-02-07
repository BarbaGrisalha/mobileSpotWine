package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.OrderAdapter;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Order;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.VinhoJsonParser;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;
    private static final String API_URL = "http://51.20.254.239:8080/api/invoice/my-invoices?access-token=gib1WP8VjzEZ1EfvLlEqWDbezvzqVfzY";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBarOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        Button btnBackToMain =findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(view -> {
            Intent intent = new Intent(OrdersActivity.this, MenuMainActivity.class);
            startActivity(intent);
            finish();
        });
        fetchOrders();

    }

    private void fetchOrders() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);

        //aqui buscando o raio da API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("DEBUG", "Resposta da API: " + response.toString());

                            //processamento da resposta com o VinhoJsonParser
                            JSONArray ordersArray = response.getJSONArray("orders");
                            orderList.clear();
                            orderList = VinhoJsonParser.parserJsonOrders(ordersArray);

                            //notificação ao adapter sobre os novos dados
                            orderAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e("JSON_ERROR", "ERRO AO PROCESSAR O JSON: " + e.getMessage());
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR","Erro na API: "+ error.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
        queue.add(jsonObjectRequest);
    }
}
