package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuMainActivity extends AppCompatActivity {
    public static final String EMAIL = "pt.ipleiria.estg.dei.amsi.mobilesportwine.EMAIL";
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
    }
}