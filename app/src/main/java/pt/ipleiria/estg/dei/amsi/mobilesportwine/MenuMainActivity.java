package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.common.collect.Ordering;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.CarrinhoAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String EMAIL = "email";
    public static final int ADD = 100, EDIT = 200, DELETE = 300;
    public static final String OP_CODE = "OPERACAO_DETALHES";

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView loggedInAccount;
    private String email = "";

    private FragmentManager fragmentManager;

    private CarrinhoAdaptador carrinhoAdaptador;
    private ArrayList<ItemCarrinho> listaItens = new ArrayList<>();
    private ArrayList<Vinho> listaVinhos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        carregarCabecalho();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();


        carregarFragmentoInicial();

    }


    private void carregarCabecalho() {
        email = getIntent().getStringExtra("userEmail");

        SharedPreferences sharedPreferenceUser = getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);

        if (email != null) {
            SharedPreferences.Editor editorUser = sharedPreferenceUser.edit();
            editorUser.putString("loggedEmail", email);
            editorUser.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carrinho, menu); // Infla o menu da Toolbar
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.navLista) {
            fragment = new ListaVinhosFragment();
            setTitle(item.getTitle());
        }else if (item.getItemId() == R.id.navBlog) {
            fragment = new BlogFragment();
            setTitle(item.getTitle());

        } else if(item.getItemId() == R.id.navOrder){
            //Abrir OrdersActivity
            Intent intent = new Intent(this, OrdersActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.navEmail) {
            // System.out.println("-->Nav Email");
            // Toast.makeText(this, "-->Nav Email", Toast.LENGTH_SHORT).show();

        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void carregarFragmentoInicial() {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setCheckable(true);

        onNavigationItemSelected(item);
    }

    public void enviarEmail() {
        String subject = "AMSI 2020/21";
        String message = "Olá " + email + " isto é uma mensagem de teste";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else
            Toast.makeText(this, "Não teme email config.", Toast.LENGTH_SHORT);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navCart) {
            // Abre o CarrinhoFragment
            CarrinhoFragment fragment = new CarrinhoFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFragment, fragment)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}