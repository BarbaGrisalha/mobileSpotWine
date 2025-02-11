package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.LoginListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private EditText userEmail;
    private EditText userPassword;
    private static final String API_HOSTNAME = "API_HOSTNAME"; // Chave para recuperar o hostname

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.etEmail);
        userPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        SingletonManager.getInstance(getApplicationContext()).setLoginListener(this);

        // Verificar se o hostname já foi configurado
        SharedPreferences sharedPreferences = getSharedPreferences(API_HOSTNAME, Context.MODE_PRIVATE);
        String hostname = sharedPreferences.getString(API_HOSTNAME, null);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        if (hostname == null) {
            // Se o hostname não foi configurado, redirecionar para a HostnameSetupActivity
            Toast.makeText(this, "Configure o hostname antes de fazer login.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ServerManagerActivity.class);
            startActivity(intent);
            finish(); // Fecha a LoginActivity
        }
    }

    public void onClickLogin(View view) {
        if (!isEmailValid(userEmail.getText().toString())) {
            Toast.makeText(this, "Endereço de e-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordValid()) {
            Toast.makeText(this, "Senha inválida (mín. 4 caracteres)", Toast.LENGTH_SHORT).show();
            return;
        }

        SingletonManager.getInstance(getApplicationContext()).loginAPI(
                userEmail.getText().toString(),
                userPassword.getText().toString(),
                getApplicationContext()
        );
    }

    public void onClickConfigureHostname(View view) {
        // Abrir a HostnameSetupActivity para configurar o hostname
        Intent intent = new Intent(this, ServerManagerActivity.class);
        startActivity(intent);
    }

    private boolean isEmailValid(String email) {
        return email != null && !email.isEmpty();
    }

    private boolean isPasswordValid() {
        return userPassword.getText().length() >= 4;
    }

    @Override
    public void onValidateLogin(String token, int userId, String email, Context context) {
        if (token != null && userId != -1) {
            SharedPreferences sharedPreferences = getSharedPreferences("AUTH_DATA", MODE_PRIVATE);
            SharedPreferences.Editor saveData = sharedPreferences.edit();

            saveData.putString("loggedEmail", email);
            saveData.putString("TOKEN", token);
            saveData.putInt("USER_ID", userId); // Armazena o ID do usuário
            saveData.apply();

            Intent toMenu = new Intent(this, MenuMainActivity.class);
            toMenu.putExtra("userEmail", email);
            startActivity(toMenu);
        } else {
            Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
        }
    }
}