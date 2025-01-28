package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuMainActivity extends AppCompatActivity {
    public static final String EMAIL = "pt.ipleiria.estg.dei.amsi.mobilesportwine.EMAIL";
    private RecyclerView recyclerView;
    private TextView connectionStatus; // Para exibir o status da conexão
    private Button btnCheckConnection; // Botão para verificar a conexão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        // Inicializa os componentes do layout
        connectionStatus = findViewById(R.id.connectionStatus); // Certifique-se de que existe no XML
        btnCheckConnection = findViewById(R.id.btnCheckConnection); // Certifique-se de que existe no XML

        // Configura o RecyclerView (se necessário)
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Recupera o email enviado pela LoginActivity
        String email = getIntent().getStringExtra(EMAIL);
        if (email != null) {
            Toast.makeText(this, "Email recebido: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nenhum email foi recebido.", Toast.LENGTH_SHORT).show();
        }

        // Configura o botão para verificar a conexão
        btnCheckConnection.setOnClickListener(v -> {
            boolean isConnected = NetworkUtils.isInternetAvailable(this);
            String connectionType = NetworkUtils.getConnectionType(this);

            if (isConnected) {
                connectionStatus.setText("Conectado via: " + connectionType);
                Toast.makeText(this, "Conectado via: " + connectionType, Toast.LENGTH_SHORT).show();
            } else {
                connectionStatus.setText("Sem conexão com a Internet");
                Toast.makeText(this, "Sem conexão com a Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
