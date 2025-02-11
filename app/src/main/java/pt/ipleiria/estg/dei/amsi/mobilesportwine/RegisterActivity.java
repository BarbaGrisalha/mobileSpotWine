package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etNif, etPhone;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializando os componentes
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etNif = findViewById(R.id.et_nif);
        etPhone = findViewById(R.id.et_phone_number);
        btnRegister = findViewById(R.id.btn_register);

        // Clique no botão de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String nif = etNif.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                // Validação dos campos
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || nif.isEmpty() || phone.isEmpty()) {
                    Snackbar.make(v, "Preencha todos os campos!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // Chamar API de registro
                SingletonManager.getInstance(getApplicationContext()).registerAPI(username, email, password, nif, phone, RegisterActivity.this);
            }
        });
    }

}
