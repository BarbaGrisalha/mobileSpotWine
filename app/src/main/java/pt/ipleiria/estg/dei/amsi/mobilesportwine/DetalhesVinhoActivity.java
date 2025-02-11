package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.ReviewAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.ReviewListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Review;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class DetalhesVinhoActivity extends AppCompatActivity implements VinhoListener, ReviewListener {

    public static final String ID_VINHO = "ID_VINHO";

    private Vinho vinho;
    private RecyclerView rvReviews;

    private TextView tvName, tvDescription, tvPrice, tvCategory, tvStock;
    private ImageView imgCapa;

    private EditText etReview;
    private RatingBar ratingBar; // Adicionado o RatingBar
    private Button btnEnviarReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_vinho);

        rvReviews = findViewById(R.id.rvReviews);

        int id = getIntent().getIntExtra("vinhoId", -1);
        System.out.println("--> ID vinho dos detalhes: " + ID_VINHO);
        vinho = SingletonManager.getInstance(getApplicationContext()).getVinho(id);
        System.out.println("--> vinho dos detalhes: " + vinho);

        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvCategory = findViewById(R.id.tvCategory);
        tvStock = findViewById(R.id.tvStock);
        imgCapa = findViewById(R.id.imgCapaVinhoDetalhes);
        etReview = findViewById(R.id.etReview);
        ratingBar = findViewById(R.id.ratingBar); // Referência ao RatingBar
        btnEnviarReview = findViewById(R.id.btnEnviarReview);

        if (vinho != null) {
            carregarVinho();
        }

        SingletonManager.getInstance(getApplicationContext()).getReviewsAPI(id, getApplicationContext());
        SingletonManager.getInstance(getApplicationContext()).setVinhoListener(this);
        SingletonManager.getInstance(getApplicationContext()).setReviewsListener(this);

        // Configurar o clique do botão para enviar a review
        btnEnviarReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarReview();
            }
        });
    }

    private void carregarVinho() {
        setTitle(vinho.getName());
        tvName.setText(vinho.getName());
        tvDescription.setText(vinho.getDescription());
        tvPrice.setText(String.format("€ %.2f", vinho.getPrice()));
        tvCategory.setText(vinho.getCategory());
        tvStock.setText(vinho.getStock() + " unidades disponíveis");

        Glide.with(getApplicationContext())
                .load(vinho.getImage())
                .placeholder(R.drawable.wine_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCapa);
    }

    private void enviarReview() {
        // Obter o texto da avaliação
        String reviewText = etReview.getText().toString().trim();

        // Obter o rating do RatingBar
        int rating = (int) ratingBar.getRating();

        // Validar os campos
        if (reviewText.isEmpty()) {
            etReview.setError("Por favor, escreva uma avaliação.");
            return;
        }

        if (rating < 1 || rating > 5) {
            Toast.makeText(this, "A classificação deve ser entre 1 e 5 estrelas.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obter o ID do vinho
        int productId = getIntent().getIntExtra("vinhoId", -1);

        // Criar o objeto Review
        Review review = new Review(0, 0, productId, rating, reviewText, null); // userId e createdAt podem ser definidos no servidor

        // Enviar a review via SingletonManager
        SingletonManager.getInstance(getApplicationContext()).adicionarReviewAPI(review, productId, getApplicationContext());

        // Limpar os campos após o envio
        etReview.setText("");
        ratingBar.setRating(0);
    }

    @Override
    public void onRefreshDetalhes(int op) {
        // Atualizar detalhes do vinho, se necessário
    }

    @Override
    public void onRefreshReviews(ArrayList<Review> reviews, int id) {
        // Atualizar a lista de reviews
        ReviewAdaptador adapter = new ReviewAdaptador(reviews, id, DetalhesVinhoActivity.this);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(adapter);
    }
}