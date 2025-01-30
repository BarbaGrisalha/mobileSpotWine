package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.CartModel;

public class MenuMainActivity extends AppCompatActivity {

    private static final String URL = "http://51.20.254.239:8080/api/cart/6?access-token=PyrvurgIAjnzfFo8XZcROCBpFDH6gOtR";
    public static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

       //Referência ao Botao da API
        Button btnFetchData = findViewById(R.id.btnFetchData);

        //Adiciona o evento de clique no botão para chamar a API
        btnFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCartData();
            }
        });

        //Referencia ao botão de limpar
        Button btnClearData = findViewById(R.id.btnClearData);

        //Adiciona evento de clique para limpar os dados
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvApiResponse = findViewById(R.id.tvApiResponse);
                tvApiResponse.setText("");//aqui limpa texto
                Toast.makeText(MenuMainActivity.this,"Dados Apagados", Toast.LENGTH_SHORT).show();
            }
        });

        // Chama o método correto
        fetchCartDataXML();
    }

    private void fetchCartData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API_RESPONSE", "Resposta da API: " + response);
                        Toast.makeText(MenuMainActivity.this, "Dados Carregados com sucesso!!!", Toast.LENGTH_SHORT).show();
                        //atualiza a interface com os dados
                        TextView tvApiResponse = findViewById(R.id.tvApiResponse);
                        tvApiResponse.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_ERROR","Erro na requisição: "+ error.getMessage());
                        Toast.makeText(MenuMainActivity.this,"Erro ao conectar à API!",Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(stringRequest);
    }

    private void fetchCartDataXML() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            CartModel cartModel = parseXMLResponse(response);
                            //Formatar a exibição dos dados na textview
                            StringBuilder formattedResponse = new StringBuilder();
                            formattedResponse.append("Cart ID: ").append(cartModel.getCart().getId()).append("\n\n");

                            for (CartModel.Item item : cartModel.getItems()) {
                                formattedResponse.append("Produto: ").append(item.getProduct_name()).append("\n")
                                        .append("Quantidade: ").append(item.getQuantity()).append("\n")
                                        .append("Preço: ").append(item.getPrice()).append("\n")
                                        .append("Subtotal: ").append(item.getSubtotal()).append("\n\n");
                            }
                            //atualizamos a interface com os dados formatados
                            TextView tvApiResponse = findViewById(R.id.tvApiResponse);
                            tvApiResponse.setText(formattedResponse.toString());
                            Toast.makeText(MenuMainActivity.this,"Dados Carregados com Sucesso!!!",Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("XML_ERROR", "Erro ao processar XML: "+ e.getMessage());
                                Toast.makeText(MenuMainActivity.this,"Erro ao processar dados!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_ERROR", "Erro na requisição: " + error.getMessage());
                        Toast.makeText(MenuMainActivity.this, "Erro ao conectar à API!", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    private CartModel parseXMLResponse(String response) throws Exception {
        CartModel cartModel = new CartModel();
        List<CartModel.Item> items = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new java.io.StringReader(response));

        CartModel.Cart cart = null;
        CartModel.Item item = null;
        String tag = null;

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tag = parser.getName();
                    if ("cart".equals(tag)) {
                        cart = new CartModel.Cart();
                    } else if ("item".equals(tag)) {
                        item = new CartModel.Item();
                    }
                    break;

                case XmlPullParser.TEXT:
                    String text = parser.getText();
                    if (cart != null && tag != null) {
                        switch (tag) {
                            case "id": cart.setId(Integer.parseInt(text)); break;
                            case "user_id": cart.setUser_id(Integer.parseInt(text)); break;
                            case "session_id": cart.setSession_id(text.isEmpty() ? null : text); break;
                            case "created_at": cart.setCreated_at(text); break;
                            case "updated_at": cart.setUpdated_at(text); break;
                        }
                    } else if (item != null && tag != null) {
                        switch (tag) {
                            case "id": item.setId(Integer.parseInt(text)); break;
                            case "product_id": item.setProduct_id(Integer.parseInt(text)); break;
                            case "product_name": item.setProduct_name(text); break;
                            case "quantity": item.setQuantity(Integer.parseInt(text)); break;
                            case "price": item.setPrice(text); break;
                            case "subtotal": item.setSubtotal(Double.parseDouble(text)); break;
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tag = parser.getName();
                    if ("cart".equals(tag)) {
                        cartModel.setCart(cart);
                    } else if ("item".equals(tag)) {
                        items.add(item);
                    }
                    break;
            }
            eventType = parser.next();
        }
        cartModel.setItems(items);
        return cartModel;
    }

}