package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuMainActivity extends AppCompatActivity {
        public static final String EMAIL = "pt.ipleiria.estg.dei.amsi.mobilesportwine.EMAIL";
        private RecyclerView recyclerView;
        //private ReviewAdapter adapter;
       // private List<Review> reviewList;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_main);

            //Eu recupero o email enviado pela LoginActivity
            String email = getIntent().getStringExtra(EMAIL);
            // Faça algo com o email, como exibi-lo ou usá-lo em uma lógica
            if (email != null) {
                // Exemplo: Mostrar no Logcat
                System.out.println("Email recebido: " + email);
            } else {
                System.out.println("Nenhum email foi recebido.");
            }

            //inicializando o RecyclerView
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }