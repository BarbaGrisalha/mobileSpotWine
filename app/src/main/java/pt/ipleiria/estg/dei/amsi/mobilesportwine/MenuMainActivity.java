package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class MenuMainActivity extends AppCompatActivity {
    public static final String EMAIL = "pt.ipleiria.estg.dei.amsi.mobilesportwine.EMAIL";
    private RecyclerView recyclerView;
    private TextView connectionStatus;
    private Button btnCheckConnection;

    private static final String API_URL = "http://51.20.254.239:8080/api/cart/6?access-token=PyrvurgIAjnzfFo8XZcROCBpFDH6gOtR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        // Inicializa os componentes do layout
        connectionStatus = findViewById(R.id.connectionStatus);
        btnCheckConnection = findViewById(R.id.btnCheckConnection);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Exibe o email enviado pela LoginActivity (se aplicável)
        String email = getIntent().getStringExtra(EMAIL);
        if (email != null) {
            Toast.makeText(this, "Email recebido: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nenhum email foi recebido.", Toast.LENGTH_SHORT).show();
        }

        // Configura o botão para verificar conexão e realizar a requisição
        btnCheckConnection.setOnClickListener(v -> {
            boolean isConnected = NetworkUtils.isInternetAvailable(this);
            String connectionType = NetworkUtils.getConnectionType(this);

            if (isConnected) {
                connectionStatus.setText("Conectado via: " + connectionType);
                Toast.makeText(this, "Conectado via: " + connectionType, Toast.LENGTH_SHORT).show();
                fetchApiData(); // Faz a requisição Volley
            } else {
                connectionStatus.setText("Sem conexão com a Internet");
                Toast.makeText(this, "Sem conexão com a Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchApiData() {
        // Inicializa a fila de requisições do Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Cria a requisição JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Processar o objeto "cart"
                            JSONObject cartObject = response.getJSONObject("cart");
                            int cartId = cartObject.getInt("id");
                            int userId = cartObject.getInt("user_id");
                            String createdAt = cartObject.getString("created_at");
                            String updatedAt = cartObject.getString("updated_at");

                            Log.d("CART_INFO", "Cart ID: " + cartId + ", User ID: " + userId);
                            Log.d("CART_INFO", "Created At: " + createdAt + ", Updated At: " + updatedAt);

                            // Processar o array "items"
                            JSONArray itemsArray = response.getJSONArray("items");
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject itemObject = itemsArray.getJSONObject(i);
                                int itemId = itemObject.getInt("id");
                                String productName = itemObject.getString("product_name");
                                int quantity = itemObject.getInt("quantity");
                                String price = itemObject.getString("price");
                                double subtotal = itemObject.getDouble("subtotal");

                                Log.d("ITEM_INFO", "Item ID: " + itemId + ", Product Name: " + productName);
                                Log.d("ITEM_INFO", "Quantity: " + quantity + ", Price: " + price + ", Subtotal: " + subtotal);
                            }

                            // Exemplo de Toast para indicar sucesso
                            Toast.makeText(MenuMainActivity.this, "Dados da API carregados com sucesso!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.e("JSON_ERROR", "Erro ao processar JSON: " + e.getMessage());
                            Toast.makeText(MenuMainActivity.this, "Erro ao processar os dados.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_ERROR", "Erro na requisição: " + error.getMessage());
                        Toast.makeText(MenuMainActivity.this, "Erro ao conectar à API.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Adiciona a requisição à fila
        queue.add(jsonObjectRequest);
    }
}