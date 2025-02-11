package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.PostListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Post;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.ConnectivityJsonParser;

public class DetalhesPostActivity extends AppCompatActivity implements PostListener {

    public static final String DEFAULT_IMG = "http://51.20.254.239:8080/default_image.png";

    private Post post;
    private EditText etTitulo, etConteudo;
    private ImageView imgCapaPost;
    private FloatingActionButton fabGuardarPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_post);

        int postId = getIntent().getIntExtra("postId", -1);

        post = SingletonManager.getInstance(getApplicationContext()).getPost(postId);

        etTitulo = findViewById(R.id.etTitulo);
        etConteudo = findViewById(R.id.etConteudo);
        imgCapaPost = findViewById(R.id.imgCapaPost);
        fabGuardarPost = findViewById(R.id.fabGuardarPost);

        if (post != null) {
            carregarPost();
            fabGuardarPost.setImageResource(R.drawable.ic_action_save);
        } else {
            setTitle(getString(R.string.adicionar_post));
            fabGuardarPost.setImageResource(R.drawable.ic_action_adicionar);
        }

        fabGuardarPost.setOnClickListener(view -> {
            //TODO: CORRIGIR PARA PODER EDITAR SÓ O MEU POST
            if (post != null) {
                // Editar post
                if (!isPostValido()) {
                    Toast.makeText(DetalhesPostActivity.this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show();
                    return;
                }

                post.setTitle(etTitulo.getText().toString());
                post.setContent(etConteudo.getText().toString());

                SingletonManager.getInstance(getApplicationContext()).editarPostAPI(post, getApplicationContext());

            } else {
                // Criar novo post
                if (!isPostValido()) {
                    Toast.makeText(DetalhesPostActivity.this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show();
                    return;
                }

//                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                post = new Post(0, etTitulo.getText().toString(), etConteudo.getText().toString(), DEFAULT_IMG,NULL );

                SingletonManager.getInstance(getApplicationContext()).adicionarPostAPI(post, getApplicationContext());

            }
        });

        SingletonManager.getInstance(getApplicationContext()).setPostListener(this);
    }

    private boolean isPostValido() {
        return !etTitulo.getText().toString().trim().isEmpty() &&
                !etConteudo.getText().toString().trim().isEmpty();
    }

    private void carregarPost() {
        setTitle(post.getTitle());
        etTitulo.setText(post.getTitle());
        etConteudo.setText(post.getContent());

        Glide.with(getApplicationContext())
                .load("http://51.20.254.239:8080" + post.getImageUrl())
//                .placeholder(R.drawable.default_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCapaPost);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (post != null) {
            getMenuInflater().inflate(R.menu.menu_remover, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemRemover) {
            if (!ConnectivityJsonParser.isConnectionInternet(getApplicationContext())) {
                Toast.makeText(this, "Não tem ligação à rede", Toast.LENGTH_SHORT).show();
            } else {
                dialogRemover();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO: CORRIGIR PARA DELETAR SO O MEU POST
    private void dialogRemover() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.txt_titulo_remover_post)
                .setMessage("Pretende remover o Post?")
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) ->
                        SingletonManager.getInstance(getApplicationContext()).deletarPostAPI(post, getApplicationContext()))
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    @Override
    public void onRefreshDetalhesPost(int op) {
        Intent intent = new Intent();
        intent.putExtra(MenuMainActivity.OP_CODE, op);
        setResult(RESULT_OK, intent);
        finish();
    }
}

