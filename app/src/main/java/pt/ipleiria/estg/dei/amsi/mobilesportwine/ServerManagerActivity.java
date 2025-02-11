package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ServerManagerActivity extends AppCompatActivity {

    private EditText etHostname;
    private static final String API_HOSTNAME = "API_HOSTNAME"; // Chave para salvar o hostname
    private String url = "51.20.254.239";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_manager);

        etHostname = findViewById(R.id.etHostname);

        // Carregar o hostname salvo, se existir
        SharedPreferences sharedPreferences = getSharedPreferences(API_HOSTNAME, Context.MODE_PRIVATE);
        String savedHostname = sharedPreferences.getString(API_HOSTNAME, null);
        if (savedHostname != null) {
            etHostname.setText(savedHostname);
        } else {
            etHostname.setText(url);
        }
    }

    public void onClickSaveHostname(View view) {
        String hostname = "http://"+etHostname.getText().toString() ;

        if (hostname.isEmpty()) {
            Toast.makeText(this, "Por favor, insira um hostname válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salvar o hostname no SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(API_HOSTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(API_HOSTNAME, hostname);
        editor.apply();

        Toast.makeText(this, "Hostname configurado com sucesso!", Toast.LENGTH_SHORT).show();

        // Voltar para a LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fecha a HostnameSetupActivity
    }
}