package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Declaração dos componentes de entrada de dados da interface (campo de email e campo de senha)
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da Activity, vinculando a interface ao arquivo XML (activity_login.xml)
        setContentView(R.layout.activity_login);

        // Inicializa os campos de texto (EditText) associando-os aos IDs definidos no XML
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        //Definido o email e senha para desenvolvimento
        etEmail.setText("teste@gmail.com");
        etPassword.setText("12345678");
    }

    // Método para validar se o formato do email é válido
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false; // Retorna falso se o email for nulo
        }
        // Usa a classe Patterns para verificar se o email segue o padrão correto
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para validar se a senha é válida (deve ter pelo menos 4 caracteres)
    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false; // Retorna falso se a senha for nula
        }
        return password.length() >= 4; // A senha é válida se tiver 4 ou mais caracteres
    }

    // Método que será chamado quando o botão de login for clicado
    public void onClickLogin(View view) {
        // Obtém os valores de email e senha que o usuário digitou
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // Inicialmente, assumimos que os dados são válidos
        boolean isValid = true;

        // Validação do email
        if (isEmailValid(email)) {
            // Se o email for válido, mostramos um toast
            Toast.makeText(this, "Email válido", Toast.LENGTH_SHORT).show();
        } else {
            // Se o email for inválido, marcamos isValid como falso
            etEmail.setError(getString(R.string.email_inv_lido));  // Exibe o erro no campo de email
            isValid = false;  // Se o email é inválido, isValid será false
        }

        // Validação da senha
        if (isPasswordValid(password)) {
            // Se a senha for válida, mostramos um toast
            Toast.makeText(this, "Password válido", Toast.LENGTH_SHORT).show();
        } else {
            // Se a senha for inválida, marcamos isValid como falso
            etPassword.setError(getString(R.string.password_inv_lido));  // Exibe o erro no campo de senha
            isValid = false;  // Se a senha é inválida, isValid será false
        }

        // Se as duas validações forem bem-sucedidas (email e senha válidos), então isValid será true
        // Caso contrário, isValid será false e a MainActivity NÃO será iniciada
        if (isValid) {
            // Se isValid ainda for true, isso significa que tanto o email quanto a senha são válidos
            Intent intent = new Intent(this, MenuMainActivity.class);
            intent.putExtra(MenuMainActivity.EMAIL, email);  // Passa o email para a MainActivity
            startActivity(intent);  // Inicia a MainActivity
            finish();  // Finaliza a LoginActivity, removendo-a da pilha de atividades
        }
    }


    // O ciclo de vida da Activity inclui vários métodos como onStart, onResume, onPause, etc.
    // Estes métodos são chamados pelo sistema Android em diferentes momentos do ciclo de vida da Activity
    // Aqui eles estão apenas sendo sobrepostos, mas não têm lógica extra adicionada

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TagResume", "----> onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("----> onStart()"); // Apenas imprime uma mensagem no log para fins de teste
    }
}
