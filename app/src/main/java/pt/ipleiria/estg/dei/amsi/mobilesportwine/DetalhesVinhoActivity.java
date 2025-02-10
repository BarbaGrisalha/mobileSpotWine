package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.ConnectivityJsonParser;

public class DetalhesVinhoActivity extends AppCompatActivity implements VinhoListener {

    public static final String ID_VINHO = "ID_VINHO";

    private Vinho vinho;

    private EditText etName, etDescription, etPrice, etCategory, etStock;
    private ImageView imgCapa;
    private FloatingActionButton fabGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_vinho);

        int id = getIntent().getIntExtra(ID_VINHO, 0);
        vinho = SingletonManager.getInstance(getApplicationContext()).getVinho(id);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etStock = findViewById(R.id.etStock);
        etPrice = findViewById(R.id.etPrice);
        imgCapa = findViewById(R.id.imgCapaVinho);

        fabGuardar = findViewById(R.id.fabGuardar);

        if(vinho != null){
            carregarVinho();
            fabGuardar.setImageResource(R.drawable.ic_action_save);
        }else{
            setTitle(getString(R.string.guardar));
            fabGuardar.setImageResource(R.drawable.ic_action_adicionar);

        }

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vinho != null){
                    //GUARDAR

                    if (!isVinhoValido()) {
                        // Se os campos não são válidos, mostra uma mensagem
                        Toast.makeText(DetalhesVinhoActivity.this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show();
                        return; // Sai do método sem salvar
                    }

                    vinho.setName(etName.getText().toString());
                    vinho.setDescription(etDescription.getText().toString());
                    vinho.setPrice(Double.parseDouble(etPrice.getText().toString()));
                    vinho.setCategory(etCategory.getText().toString());
                    vinho.setStock(Integer.parseInt(etStock.getText().toString()));

                    SingletonManager.getInstance(getApplicationContext()).editarVinhoAPI(vinho, getApplicationContext());


                }else{
                    //ADICIONAR
                    if (!isVinhoValido()) {
                        // Se os campos não são válidos, mostra uma mensagem
                        Toast.makeText(DetalhesVinhoActivity.this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show();
                        return; // Sai do método sem salvar
                    }

                    vinho = new Vinho(0,
                            etName.getText().toString(),
                            etDescription.getText().toString(),
                            etCategory.getText().toString(),
                            Double.parseDouble(etPrice.getText().toString()),
                            Integer.parseInt(etStock.getText().toString()),
                            ""
                    );


                    SingletonManager.getInstance(getApplicationContext()).adicionarVinhoAPI(vinho, getApplicationContext());
                }
            }
        });

        SingletonManager.getInstance(getApplicationContext()).setVinhoListener(this);

    }

    private boolean isVinhoValido() {
        // Verifica se os campos não estão vazios
        if (etName.getText().toString().trim().isEmpty()) return false;
        if (etDescription.getText().toString().trim().isEmpty()) return false;

        // Verifica se o ano é válido
        String precoTexto = etPrice.getText().toString().trim();
        if (precoTexto.isEmpty()) return false;

        try {
            int preco = Integer.parseInt(precoTexto);
            if (preco <= 0) return false; // preço não pode ser negativo ou zero
        } catch (NumberFormatException e) {
            return false; // O preço não é um número válido
        }

        // Todos os campos são válidos
        return true;
    }

    private void carregarVinho(){
        setTitle(vinho.getName());
        etDescription.setText(vinho.getDescription());
        etPrice.setText(""+vinho.getPrice());

        Glide.with(getApplicationContext())
                .load(vinho.getImage())
                .placeholder(R.drawable.wine_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCapa);  // Define a imagem da capa do livro
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(vinho != null){
            getMenuInflater().inflate(R.menu.menu_remover, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itemRemover){
            if(!ConnectivityJsonParser.isConnectionInternet(getApplicationContext())){
                Toast.makeText(this, "Não tem ligação a rede", Toast.LENGTH_SHORT);
            }else{
                dialogRemover();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogRemover() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.txt_titulo_remover_livro)
                .setMessage("Pretende remover o Livro?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SingletonManager.getInstance(getApplicationContext()).removerVinhoAPI(vinho, getApplicationContext());

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    @Override
    public void onRefreshDetalhes(int op) {
        Intent intent = new Intent();
        intent.putExtra(MenuMainActivity.OP_CODE, op);
        setResult(RESULT_OK, intent);
        finish();
    }

}


