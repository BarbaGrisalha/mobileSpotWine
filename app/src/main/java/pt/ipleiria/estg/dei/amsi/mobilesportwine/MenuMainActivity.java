package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.LoginListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.CartModel;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoginListener {
    public static final String EMAIL = "email";
    public static final int ADD = 100, EDIT = 200, DELETE=300;
    public static final String OP_CODE = "OPERACAO_DETALHES";

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView loggedInAccount;
    private String email = "";

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        carregarCabecalho(); //TODO:criar método
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager= getSupportFragmentManager();

        carregarFragmentoInicial();
    }

    private void carregarCabecalho(){
        email = getIntent().getStringExtra("userEmail");

        SharedPreferences sharedPreferenceUser = getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);

        if(email != null) {
            SharedPreferences.Editor editorUser = sharedPreferenceUser.edit();
            editorUser.putString("loggedEmail", email);
            editorUser.apply();
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
>>>>>>> Stashed changes
        }

        View hView = navigationView.getHeaderView(0);
        TextView nav_tvEmail = hView.findViewById(R.id.tvEmail);
        nav_tvEmail.setText(email);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        if (item.getItemId() == R.id.navLista) {
            // System.out.println("-->Nav Estatico");
            // Toast.makeText(this, "-->Nav Estatico", Toast.LENGTH_SHORT).show();

            fragment = new ListaVinhosFragment();
            setTitle(item.getTitle());
        }
        else if (item.getItemId() == R.id.navEmail) {
            // System.out.println("-->Nav Email");
            // Toast.makeText(this, "-->Nav Email", Toast.LENGTH_SHORT).show();

            enviarEmail();
        }

        if(fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment,fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void carregarFragmentoInicial(){
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setCheckable(true);

        onNavigationItemSelected(item);
    }

    public void enviarEmail(){
        String subject = "AMSI 2020/21";
        String message = "Olá " + email + " isto é uma mensagem de teste";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else
            Toast.makeText(this, "Não teme email config.", Toast.LENGTH_SHORT);

    }

    @Override
    public void onValidateLogin(String token, String email, Context context) {

    }


}